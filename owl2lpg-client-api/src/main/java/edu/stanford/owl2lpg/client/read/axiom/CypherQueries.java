package edu.stanford.owl2lpg.client.read.axiom;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class CypherQueries {

  private static final String AXIOM_SUBJECT_QUERY_FILE = "axioms/frame-axioms.cpy";
  private static final String SHORT_FORMS_QUERY_FILE = "axioms/short-forms.cpy";

  public static final String AXIOM_SUBJECT_QUERY = readResource(AXIOM_SUBJECT_QUERY_FILE);
  public static final String SHORT_FORMS_QUERY = readResource(SHORT_FORMS_QUERY_FILE);

  private static String readResource(String fileName) {
    try {
      return Resources.toString(Resources.getResource(fileName), Charsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
