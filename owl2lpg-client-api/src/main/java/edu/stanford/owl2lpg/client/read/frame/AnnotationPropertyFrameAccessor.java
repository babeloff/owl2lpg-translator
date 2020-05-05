package edu.stanford.owl2lpg.client.read.frame;

import edu.stanford.bmir.protege.web.shared.frame.AnnotationPropertyFrame;
import edu.stanford.owl2lpg.client.read.FrameAccessor;
import edu.stanford.owl2lpg.client.read.statement.GraphResult;
import edu.stanford.owl2lpg.client.shared.Arguments;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class AnnotationPropertyFrameAccessor extends FrameAccessor<AnnotationPropertyFrame> {

  @Override
  protected String getCypherQuery(Arguments arguments) {
    return null;
  }

  @Override
  protected AnnotationPropertyFrame getFrame(GraphResult result) {
    return null;
  }
}
