package edu.stanford.owl2lpg.translator;

import dagger.Component;
import edu.stanford.owl2lpg.translator.shared.OntologyObjectSerializerModule;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
@Component(modules = {
    TranslatorModule.class,
    OntologyObjectSerializerModule.class})
@TranslationSessionScope
public interface TranslatorComponent {

  AxiomTranslator getAxiomTranslator();

  OntologyTranslator getOntologyTranslator();
}
