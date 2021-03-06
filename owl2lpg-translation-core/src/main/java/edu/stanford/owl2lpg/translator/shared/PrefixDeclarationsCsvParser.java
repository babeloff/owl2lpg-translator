package edu.stanford.owl2lpg.translator.shared;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class PrefixDeclarationsCsvParser {

  private static final Logger logger = LoggerFactory.getLogger(PrefixDeclarationsCsvParser.class);

  public ImmutableMap<String, String> getBuiltInPrefixes(String relativePathName) {
    var classLoader = getClass().getClassLoader();
    var inputStream = classLoader.getResourceAsStream(relativePathName);
    try (var in = new BufferedReader(new InputStreamReader(inputStream))) {
      return in.lines()
          .map(line -> line.split(","))
          .collect(ImmutableMap.toImmutableMap(
              arr -> arr[1],
              arr -> arr[0]
          ));
    } catch (Exception e) {
      logger.error("Could not load built-in prefix declarations", e);
      return ImmutableMap.of();
    }
  }
}
