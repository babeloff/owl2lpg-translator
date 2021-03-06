package edu.stanford.owl2lpg.client.write.handlers.impl;

import edu.stanford.owl2lpg.client.write.GraphWriter;
import edu.stanford.owl2lpg.client.write.TranslationTranslator;
import edu.stanford.owl2lpg.translator.AnnotationObjectTranslator;
import edu.stanford.owl2lpg.translator.shared.BranchId;
import edu.stanford.owl2lpg.translator.shared.OntologyDocumentId;
import edu.stanford.owl2lpg.translator.shared.ProjectId;
import org.semanticweb.owlapi.model.OWLAnnotation;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class AddOntologyAnnotationHandler {

  @Nonnull
  private final GraphWriter graphWriter;

  @Nonnull
  private final AnnotationObjectTranslator annotationTranslator;

  @Nonnull
  private final TranslationTranslator translationTranslator;

  @Inject
  public AddOntologyAnnotationHandler(@Nonnull GraphWriter graphWriter,
                                      @Nonnull AnnotationObjectTranslator annotationTranslator,
                                      @Nonnull TranslationTranslator translationTranslator) {
    this.graphWriter = checkNotNull(graphWriter);
    this.annotationTranslator = checkNotNull(annotationTranslator);
    this.translationTranslator = checkNotNull(translationTranslator);
  }

  public void handle(@Nonnull ProjectId projectId,
                     @Nonnull BranchId branchId,
                     @Nonnull OntologyDocumentId documentId,
                     @Nonnull OWLAnnotation annotation) {
    var translation = annotationTranslator.translate(annotation);
    var createQuery = translationTranslator.translateToCypherCreateQuery(projectId, branchId, documentId, translation);
    createQuery.forEach(graphWriter::execute);
  }
}
