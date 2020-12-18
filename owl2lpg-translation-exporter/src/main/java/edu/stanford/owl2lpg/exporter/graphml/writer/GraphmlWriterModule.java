package edu.stanford.owl2lpg.exporter.graphml.writer;

import dagger.Module;
import dagger.Provides;
import edu.stanford.owl2lpg.exporter.common.writer.EdgeTracker;
import edu.stanford.owl2lpg.exporter.common.writer.HashSetEdgeTracker;
import edu.stanford.owl2lpg.exporter.common.writer.HashSetNodeTracker;
import edu.stanford.owl2lpg.exporter.common.writer.NodeTracker;
import edu.stanford.owl2lpg.translator.TranslationSessionScope;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Module
public class GraphmlWriterModule {

  @Nonnull
  private final Path outputPath;

  public GraphmlWriterModule(@Nonnull Path outputPath) {
      this.outputPath = (outputPath == null)
              ? Paths.get("").toAbsolutePath().normalize()
              : outputPath;
  }

  @Provides
  @TranslationSessionScope
  public GraphmlWriter provideGraphmlWriter() {
    var outputFilePath = outputPath.resolve("graph.graphml");
    var graph = TinkerGraph.open();
    return new GraphmlWriter(graph, outputFilePath);
  }

  @Provides
  @TranslationSessionScope
  public NodeTracker provideNodeTracker() {
    return new HashSetNodeTracker();
  }

  @Provides
  @TranslationSessionScope
  public EdgeTracker provideEdgeTracker() {
    return new HashSetEdgeTracker();
  }
}
