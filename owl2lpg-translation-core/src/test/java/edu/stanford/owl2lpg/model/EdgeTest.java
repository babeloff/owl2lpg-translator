package edu.stanford.owl2lpg.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static edu.stanford.owl2lpg.translator.vocab.EdgeLabel.AXIOM;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(MockitoJUnitRunner.class)
public class EdgeTest {

  private Edge edge;

  @Mock
  private EdgeId edgeId;

  @Mock
  private Node fromNode, toNode;

  @Mock
  private Properties properties;

  @Before
  public void setUp() {
    edge = Edge.create(edgeId, fromNode, toNode, AXIOM, properties);
  }

  @Test
  public void shouldCreateEdge() {
    assertThat(edge, is(notNullValue()));
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenEdgeIdNull() {
    Edge.create(null, fromNode, toNode, AXIOM, properties);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenFromNodeNull() {
    Edge.create(edgeId, null, toNode, AXIOM, properties);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenToNodeNull() {
    Edge.create(edgeId, fromNode, null, AXIOM, properties);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenLabelNull() {
    Edge.create(edgeId, fromNode, toNode, null, properties);
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowNPEWhenPropertiesNull() {
    Edge.create(edgeId, fromNode, toNode, AXIOM, null);
  }

  @Test
  public void shouldGetFromNode() {
    var actualNode = edge.getFromNode();
    assertThat(actualNode, equalTo(fromNode));
  }

  @Test
  public void shouldGetToNode() {
    var actualNode = edge.getToNode();
    assertThat(actualNode, equalTo(toNode));
  }

  @Test
  public void shouldGetLabel() {
    var actualLabel = edge.getLabel();
    assertThat(actualLabel, equalTo(AXIOM));
  }

  @Test
  public void shouldGetProperties() {
    var actualProperties = edge.getProperties();
    assertThat(actualProperties, equalTo(properties));
  }
}