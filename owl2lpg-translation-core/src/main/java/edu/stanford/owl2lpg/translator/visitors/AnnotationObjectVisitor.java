package edu.stanford.owl2lpg.translator.visitors;

import com.google.common.collect.ImmutableList;
import edu.stanford.owl2lpg.model.Edge;
import edu.stanford.owl2lpg.model.Node;
import edu.stanford.owl2lpg.model.NodeId;
import edu.stanford.owl2lpg.model.Properties;
import edu.stanford.owl2lpg.model.StructuralEdgeFactory;
import edu.stanford.owl2lpg.model.Translation;
import edu.stanford.owl2lpg.translator.AnnotationObjectTranslator;
import edu.stanford.owl2lpg.translator.AnnotationValueTranslator;
import edu.stanford.owl2lpg.translator.AxiomTranslator;
import edu.stanford.owl2lpg.translator.PropertyExpressionTranslator;
import edu.stanford.owl2lpg.translator.shared.OntologyObjectDigester;
import edu.stanford.owl2lpg.translator.vocab.NodeLabels;
import org.jetbrains.annotations.NotNull;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationObjectVisitorEx;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLSubAnnotationPropertyOfAxiom;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.owl2lpg.translator.vocab.PropertyFields.DIGEST;

/**
 * A visitor that contains the implementation to translate the OWL 2 annotations.
 *
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class AnnotationObjectVisitor implements OWLAnnotationObjectVisitorEx<Translation> {

  @Nonnull
  private final StructuralEdgeFactory structuralEdgeFactory;

  @Nonnull
  private final PropertyExpressionTranslator propertyExprTranslator;

  @Nonnull
  private final AnnotationValueTranslator annotationValueTranslator;

  @Nonnull
  private final AnnotationObjectTranslator annotationObjectTranslator;

  @Nonnull
  private final AxiomTranslator axiomTranslator;

  @Nonnull
  private final OntologyObjectDigester ontologyObjectDigester;

  @Inject
  public AnnotationObjectVisitor(@Nonnull StructuralEdgeFactory structuralEdgeFactory,
                                 @Nonnull PropertyExpressionTranslator propertyExprTranslator,
                                 @Nonnull AnnotationValueTranslator annotationValueTranslator,
                                 @Nonnull AnnotationObjectTranslator annotationObjectTranslator,
                                 @Nonnull AxiomTranslator axiomTranslator,
                                 @Nonnull OntologyObjectDigester ontologyObjectDigester) {
    this.structuralEdgeFactory = checkNotNull(structuralEdgeFactory);
    this.propertyExprTranslator = checkNotNull(propertyExprTranslator);
    this.annotationValueTranslator = checkNotNull(annotationValueTranslator);
    this.annotationObjectTranslator = checkNotNull(annotationObjectTranslator);
    this.axiomTranslator = checkNotNull(axiomTranslator);
    this.ontologyObjectDigester = checkNotNull(ontologyObjectDigester);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLAnnotation annotation) {
    var mainNode = createAnnotationNode(annotation);
    var translations = new ImmutableList.Builder<Translation>();
    var edges = new ImmutableList.Builder<Edge>();
    var annotationPropertyTranslation = propertyExprTranslator.translate(annotation.getProperty());
    translations.add(annotationPropertyTranslation);
    edges.add(structuralEdgeFactory.getAnnotationPropertyEdge(mainNode, annotationPropertyTranslation.getMainNode()));
    var annotationValueTranslation = annotationValueTranslator.translate(annotation.getValue());
    translations.add(annotationValueTranslation);
    edges.add(structuralEdgeFactory.getAnnotationValueEdge(mainNode, annotationValueTranslation.getMainNode()));
    for (var ann : annotation.getAnnotations()) {
      var translation = annotationObjectTranslator.translate(ann);
      translations.add(translation);
      edges.add(structuralEdgeFactory.getAnnotationAnnotationEdge(mainNode, translation.getMainNode()));
    }
    return Translation.create(annotation, mainNode,
        edges.build(),
        translations.build());
  }

  @NotNull
  private Node createAnnotationNode(@Nonnull OWLAnnotation annotation) {
    var digestString = ontologyObjectDigester.getDigest(annotation);
    var nodeId = NodeId.create(digestString);
    return Node.create(nodeId, NodeLabels.ANNOTATION, Properties.of(DIGEST, digestString));
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull IRI iri) {
    return annotationValueTranslator.translate(iri);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLAnonymousIndividual individual) {
    return annotationValueTranslator.translate(individual);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLLiteral literal) {
    return annotationValueTranslator.translate(literal);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLAnnotationAssertionAxiom axiom) {
    return axiomTranslator.translate(axiom);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLSubAnnotationPropertyOfAxiom axiom) {
    return axiomTranslator.translate(axiom);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLAnnotationPropertyDomainAxiom axiom) {
    return axiomTranslator.translate(axiom);
  }

  @Nonnull
  @Override
  public Translation visit(@Nonnull OWLAnnotationPropertyRangeAxiom axiom) {
    return axiomTranslator.translate(axiom);
  }
}
