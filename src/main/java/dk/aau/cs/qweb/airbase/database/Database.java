package dk.aau.cs.qweb.airbase.database;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.impl.StatementImpl;
import org.apache.jena.tdb.TDBFactory;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.TripleContainer;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class Database {

	public void writeToDisk(TripleContainer triples) {
		Set<Quad> all = new HashSet<Quad>();
		all.addAll(triples.getInformationTriples());
		all.addAll(triples.getMetadataTriples());
		all.addAll(triples.getProvenanceTriples());
		
		Dataset dataset = TDBFactory.createDataset(Config.getDBLocation()) ;
		dataset.begin(ReadWrite.WRITE) ;
		
		Model model = ModelFactory.createDefaultModel(); 
		
		for (Quad quad : all) {
			ModelFactory.createDefaultModel();
			Resource subject = ResourceFactory.createResource(quad.getSubject());
			Property predicate = ResourceFactory.createProperty(quad.getPredicate());
			RDFNode object;
			
			if (quad.getObject().hasType()) {
				if (quad.getObject().getType().equals(XSD.integerType)) {
					object = model.createTypedLiteral(new Integer(quad.getObject().getliteral()));
				} else if (quad.getObject().getType().equals(XSD.decimalType)) {
					object = model.createTypedLiteral(new Double(quad.getObject().getliteral()));
				} else if (quad.getObject().getType().equals(XSD.booleanType)) {
					object = model.createTypedLiteral(new Boolean(quad.getObject().getliteral()));
				} else if (quad.getObject().getType().equals(XSD.dateType)) {
					object = model.createTypedLiteral(quad.getObject().getliteral(),XSDDatatype.XSDdate);
				} else {
					object = model.createTypedLiteral(new String(quad.getObject().getliteral()));
				}
			} else {
				object = ResourceFactory.createPlainLiteral(quad.getObject().getliteral());
			}
			Statement statement = new StatementImpl(subject, predicate, object);
			model.add(statement);
			dataset.addNamedModel(quad.getGraphLabel(), model);
		}
		model.close();
		dataset.commit();
		dataset.end();
	}
}
