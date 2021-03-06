package edu.stanford.owl2lpg.client.read.handlers;

import edu.stanford.owl2lpg.client.read.NodeIndex;
import edu.stanford.owl2lpg.client.read.NodeMapper;
import edu.stanford.owl2lpg.translator.vocab.NodeLabels;
import org.neo4j.driver.types.Node;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDatatypeRestriction;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class OwlDatatypeRestrictionNodeHandler implements NodeHandler<OWLDatatypeRestriction> {

  @Nonnull
  private final OWLDataFactory dataFactory;

  @Nonnull
  private final NodeToOwlMapper nodeToOwlMapper;

  @Inject
  public OwlDatatypeRestrictionNodeHandler(@Nonnull OWLDataFactory dataFactory,
                                           @Nonnull NodeToOwlMapper nodeToOwlMapper) {
    this.dataFactory = checkNotNull(dataFactory);
    this.nodeToOwlMapper = checkNotNull(nodeToOwlMapper);
  }

  @Override
  public String getLabel() {
    return NodeLabels.DATATYPE_RESTRICTION.getMainLabel();
  }

  @Override
  public OWLDatatypeRestriction handle(Node mainNode, NodeIndex nodeIndex, NodeMapper nodeMapper) {
    var datatype = nodeToOwlMapper.toDatatype(mainNode, nodeIndex, nodeMapper);
    var facetRestrictions = nodeToOwlMapper.toFacetRestrictions(mainNode, nodeIndex, nodeMapper);
    return dataFactory.getOWLDatatypeRestriction(datatype, facetRestrictions);
  }
}
