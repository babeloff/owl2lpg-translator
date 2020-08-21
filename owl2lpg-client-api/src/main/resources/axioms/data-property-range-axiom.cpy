MATCH (:Project {projectId:$projectId})-[:BRANCH]->(:Branch {branchId:$branchId})-[:ONTOLOGY_DOCUMENT]->(o:OntologyDocument {ontologyDocumentId:$ontoDocId})
MATCH (o)<-[:AXIOM_OF]-(n:DataPropertyRange)-[:AXIOM_SUBJECT]->(:DataProperty {iri:$entityIri})
MATCH p=(n)-[* {structuralSpec:true}]->(m)
WHERE apoc.coll.duplicates(NODES(p)) = []
RETURN p