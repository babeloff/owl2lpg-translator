package edu.stanford.owl2lpg.client.read.ontology.impl;

import edu.stanford.owl2lpg.client.read.annotation.OntologyAnnotationsAccessor;
import edu.stanford.owl2lpg.client.read.axiom.AxiomAccessor;
import edu.stanford.owl2lpg.client.read.entity.EntityAccessor;
import edu.stanford.owl2lpg.client.read.ontology.OntologyAccessor;
import edu.stanford.owl2lpg.client.read.ontology.ProjectAccessor;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.OntologyDocumentId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntologyID;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class OntologyAccessorImpl implements OntologyAccessor {

  @Nonnull
  private final ProjectAccessor projectAccessor;

  @Nonnull
  private final OntologyAnnotationsAccessor ontologyAnnotationsAccessor;

  @Nonnull
  private final AxiomAccessor axiomAccessor;

  @Nonnull
  private final EntityAccessor entityAccessor;

  @Inject
  public OntologyAccessorImpl(@Nonnull ProjectAccessor projectAccessor,
                              @Nonnull OntologyAnnotationsAccessor ontologyAnnotationsAccessor,
                              @Nonnull AxiomAccessor axiomAccessor,
                              @Nonnull EntityAccessor entityAccessor) {
    this.projectAccessor = checkNotNull(projectAccessor);
    this.ontologyAnnotationsAccessor = checkNotNull(ontologyAnnotationsAccessor);
    this.axiomAccessor = checkNotNull(axiomAccessor);
    this.entityAccessor = checkNotNull(entityAccessor);
  }

  @Nonnull
  @Override
  public OWLOntologyID getOntologyId(@Nonnull ProjectId projectId,
                                     @Nonnull BranchId branchId,
                                     @Nonnull OntologyDocumentId ontoDocId) {
    return projectAccessor.getOntologyDocumentIdMap(projectId, branchId).get(ontoDocId);
  }

  @Nonnull
  @Override
  public Set<OWLAnnotation> getOntologyAnnotations(@Nonnull ProjectId projectId,
                                                   @Nonnull BranchId branchId,
                                                   @Nonnull OntologyDocumentId ontoDocId) {
    return ontologyAnnotationsAccessor.getOntologyAnnotations(projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public Set<OWLAxiom> getAllAxioms(@Nonnull ProjectId projectId,
                                    @Nonnull BranchId branchId,
                                    @Nonnull OntologyDocumentId ontoDocId) {
    return axiomAccessor.getAllAxioms(projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public <E extends OWLAxiom> Set<E> getAxiomsByType(@Nonnull AxiomType<E> axiomType,
                                                     @Nonnull ProjectId projectId,
                                                     @Nonnull BranchId branchId,
                                                     @Nonnull OntologyDocumentId ontoDocId) {
    return axiomAccessor.getAxiomsByType(axiomType, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public Set<OWLEntity> getAllEntities(@Nonnull ProjectId projectId,
                                       @Nonnull BranchId branchId,
                                       @Nonnull OntologyDocumentId ontoDocId) {
    return entityAccessor.getAllEntities(projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public <E extends OWLEntity> Set<E> getEntitiesByType(@Nonnull EntityType<E> entityType,
                                                        @Nonnull ProjectId projectId,
                                                        @Nonnull BranchId branchId,
                                                        @Nonnull OntologyDocumentId ontoDocId) {
    return entityAccessor.getEntitiesByType(entityType, projectId, branchId, ontoDocId);
  }

  @Nonnull
  @Override
  public Set<OWLEntity> getEntitiesByIri(@Nonnull IRI iri,
                                         @Nonnull ProjectId projectId,
                                         @Nonnull BranchId branchId,
                                         @Nonnull OntologyDocumentId ontoDocId) {
    return entityAccessor.getEntitiesByIri(iri, projectId, branchId, ontoDocId);
  }
}