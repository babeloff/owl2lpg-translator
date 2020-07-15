package edu.stanford.owl2lpg.client.read.shortform;

import com.google.common.collect.*;
import edu.stanford.bmir.protege.web.server.shortform.*;
import edu.stanford.bmir.protege.web.shared.pagination.Page;
import edu.stanford.bmir.protege.web.shared.pagination.PageRequest;
import edu.stanford.bmir.protege.web.shared.shortform.DictionaryLanguage;
import edu.stanford.owl2lpg.client.read.frame.Parameters;
import edu.stanford.owl2lpg.model.BranchId;
import edu.stanford.owl2lpg.model.ProjectId;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.semanticweb.owlapi.model.EntityType;
import org.semanticweb.owlapi.model.OWLEntity;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static edu.stanford.owl2lpg.client.util.Resources.read;

/**
 * @author Josef Hardi <josef.hardi@stanford.edu> <br>
 * Stanford Center for Biomedical Informatics Research
 */
public class Neo4jSearchableMultiLingualShortFormDictionary implements SearchableMultiLingualShortFormDictionary {

  private static final String SEARCHABLE_SHORT_FORMS_QUERY_FILE = "shortforms/searchable-short-forms-dictionary.cpy";
  private static final String SEARCHABLE_SHORT_FORMS_QUERY = read(SEARCHABLE_SHORT_FORMS_QUERY_FILE);

  @Nonnull
  private final ProjectId projectId;

  @Nonnull
  private final BranchId branchId;

  @Nonnull
  private final Session session;

  @Nonnull
  private final Neo4jFullTextIndexName annotationValueFullTextIndexName;

  @Nonnull
  private final Neo4jNodeTranslator nodeTranslator;

  @Inject
  public Neo4jSearchableMultiLingualShortFormDictionary(@Nonnull ProjectId projectId,
                                                        @Nonnull BranchId branchId,
                                                        @Nonnull Session session,
                                                        @Nonnull Neo4jFullTextIndexName annotationValueFullTextIndexName,
                                                        @Nonnull Neo4jNodeTranslator nodeTranslator) {
    this.projectId = checkNotNull(projectId);
    this.branchId = checkNotNull(branchId);
    this.session = checkNotNull(session);
    this.annotationValueFullTextIndexName = checkNotNull(annotationValueFullTextIndexName);
    this.nodeTranslator = checkNotNull(nodeTranslator);
  }

  @Nonnull
  @Override
  public Page<EntityShortFormMatches> getShortFormsContaining(@Nonnull List<SearchString> searchStrings,
                                                              @Nonnull Set<EntityType<?>> entityTypes,
                                                              @Nonnull List<DictionaryLanguage> languages,
                                                              @Nonnull PageRequest pageRequest) {
    var shortFormMatchMultiMap = getShortFormsContaining(searchStrings, entityTypes, pageRequest);
    var result = languages
        .stream()
        .flatMap(language -> shortFormMatchMultiMap.get(language).stream())
        .distinct()
        .collect(ImmutableList.toImmutableList());
    return new Page<>(pageRequest.getPageNumber(),
        pageRequest.getPageSize(),
        result,
        result.size());
  }

  @Nonnull
  public Multimap<DictionaryLanguage, EntityShortFormMatches> getShortFormsContaining(List<SearchString> searchStrings,
                                                                                      Set<EntityType<?>> entityTypes,
                                                                                      PageRequest pageRequest) {
    var args = Parameters.forShortFormsContaining(
        projectId, branchId, annotationValueFullTextIndexName, searchStrings, pageRequest);
    var output = session.readTransaction(tx -> {
      var entityShortFormMatchMap = Maps.<DictionaryLanguage, Multimap<OWLEntity, ShortFormMatch>>newHashMap();
      var result = tx.run(SEARCHABLE_SHORT_FORMS_QUERY, args);
      while (result.hasNext()) {
        var row = result.next().asMap();
        var entityNode = (Node) row.get("entity");
        var propertyNode = (Node) row.get("annotationProperty");
        var literalNode = (Node) row.get("value");
        var entity = nodeTranslator.getOwlEntity(entityNode);
        if (entityTypes.contains(entity.getEntityType())) {
          var shortForm = nodeTranslator.getShortForm(literalNode);
          var language = nodeTranslator.getDictionaryLanguage(propertyNode, literalNode);
          var shortFormMatch = getShortFormMatch(entity, shortForm, searchStrings, language);
          var shortFormMultimap = entityShortFormMatchMap.get(language);
          if (shortFormMultimap == null) {
            shortFormMultimap = HashMultimap.create();
            entityShortFormMatchMap.put(language, shortFormMultimap);
          }
          shortFormMultimap.put(entity, shortFormMatch);
        }
      }
      return entityShortFormMatchMap;
    });
    return toMultimap(output);
  }

  @Nonnull
  private static Multimap<DictionaryLanguage, EntityShortFormMatches>
  toMultimap(Map<DictionaryLanguage, Multimap<OWLEntity, ShortFormMatch>> output) {
    var entityShortFormMatchesMultimap = HashMultimap.<DictionaryLanguage, EntityShortFormMatches>create();
    for (var language : output.keySet()) {
      var shortFormMatchMultimap = output.get(language);
      for (var entity : shortFormMatchMultimap.keySet()) {
        var shortFormMatches = shortFormMatchMultimap.get(entity);
        var entityShortFormMatches = EntityShortFormMatches.get(entity, ImmutableList.copyOf(shortFormMatches));
        entityShortFormMatchesMultimap.put(language, entityShortFormMatches);
      }
    }
    return entityShortFormMatchesMultimap;
  }

  @Nonnull
  private ShortFormMatch getShortFormMatch(OWLEntity entity, String shortForm,
                                           List<SearchString> searchStrings,
                                           DictionaryLanguage languange) {
    var matchPositions = Lists.<ShortFormMatchPosition>newArrayList();
    for (var ss : searchStrings) {
      var searchString = ss.getSearchString();
      var matchIndex = shortForm.toLowerCase().indexOf(searchString);
      matchPositions.add(ShortFormMatchPosition.get(matchIndex, matchIndex + searchString.length() - 1));
    }
    return ShortFormMatch.get(entity, shortForm, languange, ImmutableList.copyOf(matchPositions));
  }
}
