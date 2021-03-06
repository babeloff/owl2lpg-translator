package edu.stanford.owl2lpg.client.read.handlers;

import edu.stanford.owl2lpg.client.read.NodeIndex;
import edu.stanford.owl2lpg.client.read.NodeMapper;
import org.neo4j.driver.types.Node;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLFacetRestriction;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.vocab.OWLFacet;

import javax.inject.Inject;
import java.util.Set;

import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.ANNOTATION_ANNOTATION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.ANNOTATION_PROPERTY;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.ANNOTATION_SUBJECT;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.ANNOTATION_VALUE;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.AXIOM_ANNOTATION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.CLASS;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.CLASS_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.CONSTRAINING_FACET;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.DATATYPE;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.DATA_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.DATA_RANGE;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.DOMAIN;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.ENTITY;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.INDIVIDUAL;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.INVERSE_OBJECT_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.LITERAL;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.OBJECT_PROPERTY;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.OBJECT_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.RANGE;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.RESTRICTION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.RESTRICTION_VALUE;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SOURCE_INDIVIDUAL;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUB_ANNOTATION_PROPERTY;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUB_CLASS_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUB_DATA_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUB_OBJECT_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUPER_ANNOTATION_PROPERTY;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUPER_CLASS_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUPER_DATA_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.SUPER_OBJECT_PROPERTY_EXPRESSION;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.TARGET_INDIVIDUAL;
import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.TARGET_VALUE;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class NodeToOwlMapper {

  @Inject
  public NodeToOwlMapper() {
  }

  public OWLClass toClass(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var endNode = nodeIndex.getEndNode(mainNode, CLASS.name());
    return nodeMapper.toObject(endNode, nodeIndex, OWLClass.class);
  }

  public OWLClassExpression toClassExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var endNode = nodeIndex.getEndNode(mainNode, CLASS_EXPRESSION.name());
    return nodeMapper.toObject(endNode, nodeIndex, OWLClassExpression.class);
  }

  public Set<OWLClassExpression> toClassExprs(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var endNode = nodeIndex.getEndNodes(mainNode, CLASS_EXPRESSION.name());
    return nodeMapper.toObjects(endNode, nodeIndex, OWLClassExpression.class);
  }

  public OWLClassExpression toSubClassExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var endNode = nodeIndex.getEndNode(mainNode, SUB_CLASS_EXPRESSION.name());
    return nodeMapper.toObject(endNode, nodeIndex, OWLClassExpression.class);
  }

  public OWLClassExpression toSuperClassExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var endNode = nodeIndex.getEndNode(mainNode, SUPER_CLASS_EXPRESSION.name());
    return nodeMapper.toObject(endNode, nodeIndex, OWLClassExpression.class);
  }

  public OWLClassExpression toPropertyDomain(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var domainNode = nodeIndex.getEndNode(mainNode, DOMAIN.name());
    return nodeMapper.toObject(domainNode, nodeIndex, OWLClassExpression.class);
  }

  public OWLClassExpression toObjectPropertyRange(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var rangeNode = nodeIndex.getEndNode(mainNode, RANGE.name());
    return nodeMapper.toObject(rangeNode, nodeIndex, OWLClassExpression.class);
  }

  public OWLDataRange toDataPropertyRange(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var rangeNode = nodeIndex.getEndNode(mainNode, RANGE.name());
    return nodeMapper.toObject(rangeNode, nodeIndex, OWLDataRange.class);
  }

  public OWLObjectPropertyExpression toObjectProperty(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, OBJECT_PROPERTY.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLObjectProperty.class);
  }

  public OWLObjectPropertyExpression toObjectPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, OBJECT_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLObjectPropertyExpression.class);
  }

  public OWLObjectPropertyExpression toSubObjectPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUB_OBJECT_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLObjectPropertyExpression.class);
  }

  public OWLObjectPropertyExpression toSuperObjectPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUPER_OBJECT_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLObjectPropertyExpression.class);
  }

  public OWLObjectPropertyExpression toInverseObjectPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, INVERSE_OBJECT_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLObjectPropertyExpression.class);
  }

  public Set<OWLObjectPropertyExpression> toObjectPropertyExprs(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNodes = nodeIndex.getEndNodes(mainNode, OBJECT_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObjects(propertyNodes, nodeIndex, OWLObjectPropertyExpression.class);
  }

  public OWLDataPropertyExpression toDataPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, DATA_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLDataPropertyExpression.class);
  }

  public OWLDataPropertyExpression toSubDataPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUB_DATA_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLDataPropertyExpression.class);
  }

  public OWLDataPropertyExpression toSuperDataPropertyExpr(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUPER_DATA_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLDataPropertyExpression.class);
  }

  public Set<OWLDataPropertyExpression> toDataPropertyExprs(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNodes = nodeIndex.getEndNodes(mainNode, DATA_PROPERTY_EXPRESSION.name());
    return nodeMapper.toObjects(propertyNodes, nodeIndex, OWLDataPropertyExpression.class);
  }

  public OWLEntity toEntity(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var entityNode = nodeIndex.getEndNode(mainNode, ENTITY.name());
    return nodeMapper.toObject(entityNode, nodeIndex, OWLEntity.class);
  }

  public OWLAnnotationProperty toAnnotationProperty(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, ANNOTATION_PROPERTY.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLAnnotationProperty.class);
  }

  public OWLAnnotationProperty toSubAnnotationProperty(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUB_ANNOTATION_PROPERTY.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLAnnotationProperty.class);
  }

  public OWLAnnotationProperty toSuperAnnotationProperty(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var propertyNode = nodeIndex.getEndNode(mainNode, SUPER_ANNOTATION_PROPERTY.name());
    return nodeMapper.toObject(propertyNode, nodeIndex, OWLAnnotationProperty.class);
  }

  public OWLAnnotationSubject toAnnotationSubject(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var subjectNode = nodeIndex.getEndNode(mainNode, ANNOTATION_SUBJECT.name());
    return nodeMapper.toObject(subjectNode, nodeIndex, OWLAnnotationSubject.class);
  }

  public OWLAnnotationValue toAnnotationValue(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var valueNode = nodeIndex.getEndNode(mainNode, ANNOTATION_VALUE.name());
    if (valueNode == null) {
      System.out.println("==========================================================================");
      System.out.println("NULL NODE: " + mainNode);
      System.out.println("--------------------------------------------------------------------------");
    }
    return nodeMapper.toObject(valueNode, nodeIndex, OWLAnnotationValue.class);
  }

  public OWLLiteral toLiteral(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var literalNode = nodeIndex.getEndNode(mainNode, LITERAL.name());
    return nodeMapper.toObject(literalNode, nodeIndex, OWLLiteral.class);
  }

  public OWLLiteral toTargetValue(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var literalNode = nodeIndex.getEndNode(mainNode, TARGET_VALUE.name());
    return nodeMapper.toObject(literalNode, nodeIndex, OWLLiteral.class);
  }

  public OWLLiteral toRestrictionValue(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var literalNode = nodeIndex.getEndNode(mainNode, RESTRICTION_VALUE.name());
    return nodeMapper.toObject(literalNode, nodeIndex, OWLLiteral.class);
  }

  public Set<OWLLiteral> toLiterals(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var literalNodes = nodeIndex.getEndNodes(mainNode, LITERAL.name());
    return nodeMapper.toObjects(literalNodes, nodeIndex, OWLLiteral.class);
  }

  public OWLDatatype toDatatype(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var datatypeNode = nodeIndex.getEndNode(mainNode, DATATYPE.name());
    return nodeMapper.toObject(datatypeNode, nodeIndex, OWLDatatype.class);
  }

  public IRI toAnnotationPropertyDomain(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var domainNode = nodeIndex.getEndNode(mainNode, DOMAIN.name());
    return nodeMapper.toObject(domainNode, nodeIndex, IRI.class);
  }

  public IRI toAnnotationPropertyRange(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var rangeNode = nodeIndex.getEndNode(mainNode, RANGE.name());
    return nodeMapper.toObject(rangeNode, nodeIndex, IRI.class);
  }

  public OWLDataRange toDataRange(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var dataRangeNode = nodeIndex.getEndNode(mainNode, DATA_RANGE.name());
    return nodeMapper.toObject(dataRangeNode, nodeIndex, OWLDataRange.class);
  }

  public Set<OWLDataRange> toDataRanges(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var dataRangeNodes = nodeIndex.getEndNodes(mainNode, DATA_RANGE.name());
    return nodeMapper.toObjects(dataRangeNodes, nodeIndex, OWLDataRange.class);
  }

  public OWLIndividual toIndividual(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNode = nodeIndex.getEndNode(mainNode, INDIVIDUAL.name());
    return nodeMapper.toObject(individualNode, nodeIndex, OWLIndividual.class);
  }

  public OWLIndividual toSourceIndividual(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNode = nodeIndex.getEndNode(mainNode, SOURCE_INDIVIDUAL.name());
    return nodeMapper.toObject(individualNode, nodeIndex, OWLIndividual.class);
  }

  public OWLIndividual toTargetIndividual(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNode = nodeIndex.getEndNode(mainNode, TARGET_INDIVIDUAL.name());
    return nodeMapper.toObject(individualNode, nodeIndex, OWLIndividual.class);
  }

  public Set<OWLIndividual> toIndividuals(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNodes = nodeIndex.getEndNodes(mainNode, INDIVIDUAL.name());
    return nodeMapper.toObjects(individualNodes, nodeIndex, OWLIndividual.class);
  }

  public OWLNamedIndividual toNamedIndividual(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNode = nodeIndex.getEndNode(mainNode, INDIVIDUAL.name());
    return nodeMapper.toObject(individualNode, nodeIndex, OWLNamedIndividual.class);
  }

  public OWLAnonymousIndividual toAnonymousIndividual(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var individualNode = nodeIndex.getEndNode(mainNode, INDIVIDUAL.name());
    return nodeMapper.toObject(individualNode, nodeIndex, OWLAnonymousIndividual.class);
  }

  public Set<OWLAnnotation> toAxiomAnnotations(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var annotationNodes = nodeIndex.getEndNodes(mainNode, AXIOM_ANNOTATION.name());
    return nodeMapper.toObjects(annotationNodes, nodeIndex, OWLAnnotation.class);
  }

  public Set<OWLAnnotation> toAnnotationAnnotations(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var annotationNodes = nodeIndex.getEndNodes(mainNode, ANNOTATION_ANNOTATION.name());
    return nodeMapper.toObjects(annotationNodes, nodeIndex, OWLAnnotation.class);
  }

  public Set<OWLFacetRestriction> toFacetRestrictions(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var restrictionNodes = nodeIndex.getEndNodes(mainNode, RESTRICTION.name());
    return nodeMapper.toObjects(restrictionNodes, nodeIndex, OWLFacetRestriction.class);
  }

  public OWLFacet toConstrainingFacet(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var facetNode = nodeIndex.getEndNode(mainNode, CONSTRAINING_FACET.name());
    return nodeMapper.toObject(facetNode, nodeIndex, OWLFacet.class);
  }
}
