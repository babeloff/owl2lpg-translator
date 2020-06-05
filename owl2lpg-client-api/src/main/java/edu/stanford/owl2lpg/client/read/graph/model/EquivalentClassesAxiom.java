package edu.stanford.owl2lpg.client.read.graph.model;

import com.google.common.collect.ImmutableSet;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.session.Session;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@NodeEntity(label = "EquivalentClasses")
public class EquivalentClassesAxiom extends ClassAxiom<OWLEquivalentClassesAxiom> {

  @Relationship(type = "CLASS_EXPRESSION")
  private Set<ClassExpression> classExpressions;

  private EquivalentClassesAxiom() {
  }

  @Nullable
  public ImmutableSet<ClassExpression> getEquivalentClasses() {
    if (Objects.isNull(classExpressions)) {
      return null;
    } else {
      return ImmutableSet.copyOf(classExpressions);
    }
  }

  public OWLEquivalentClassesAxiom toOwlObject(OWLDataFactory dataFactory, Session session) {
    try {
      return dataFactory.getOWLEquivalentClassesAxiom(
          classExpressions.stream()
              .map(ce -> ce.toOwlObject(dataFactory, session))
              .collect(Collectors.toSet()));
    } catch (NullPointerException e) {
      var object = session.load(getClass(), getId(), 2);
      return object.toOwlObject(dataFactory, session);
    }
  }
}
