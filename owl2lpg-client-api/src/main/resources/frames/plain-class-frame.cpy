MATCH (project)-[:BRANCH]->(branch)-[:ONTOLOGY_DOCUMENT]->(document)-[:AXIOM]->(axiom)
MATCH (entity:Class { iri: $subjectIri })-[:IS_SUBJECT_OF]->(axiom)
OPTIONAL MATCH (entity)-[:SUB_CLASS_OF]->(parent:Class)
OPTIONAL MATCH (entity)-[property:RELATED_TO]->(object)
WHERE project.projectId = $projectId
AND branch.branchId = $branchId
AND document.ontologyDocumentId = $ontoDocId

RETURN { type: "ClassFrame",
       subject: { type: "owl:Class", iri: entity.iri },
       parents: COLLECT(DISTINCT({ type: "owl:Class", iri: parent.iri })),
       propertyValues:
       COLLECT(DISTINCT(
          CASE WHEN 'Class' IN LABELS(object) AND property.type = 'ObjectProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:ObjectProperty", iri: property.iri },
               value: { type: "owl:Class", iri: object.iri }
             }
          WHEN 'NamedIndividual' IN LABELS(object) AND property.type = 'ObjectProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:ObjectProperty", iri: property.iri },
               value: { type: "owl:NamedIndividual", iri: object.iri }
             }
          WHEN 'Datatype' IN LABELS(object) AND property.type = 'DataProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:DataProperty", iri: property.iri },
               value: { type: "owl:Datatype", iri: object.iri }
             }
          WHEN 'Literal' IN LABELS(object) AND property.type = 'DataProperty' THEN
             { type: "PropertyClassValue",
               property: { type: "owl:DataProperty", iri: property.iri },
               value: { type: object.datatype, value: object.lexicalForm, lang: object.language }
             }
          WHEN 'Literal' IN LABELS(object) AND property.type = 'AnnotationProperty' THEN
             { type: "PropertyAnnotationValue",
               property: { type: "owl:AnnotationProperty", iri: property.iri },
               value:
               CASE WHEN object.datatype = 'http://www.w3.org/1999/02/22-rdf-syntax-ns#PlainLiteral' THEN
                  { value: object.lexicalForm, lang: object.language }
               ELSE
                  { type: object.datatype, value: object.lexicalForm }
               END
             }
          WHEN 'IRI' IN LABELS(object) AND property.type = 'AnnotationProperty' THEN
             { type: "PropertyAnnotationValue",
               property: { type: "owl:AnnotationProperty", iri: property.iri },
               value: object.iri
             }
          END
       ))} AS result