package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class ProvenanceIdentifierEntity extends Entity {

	public ProvenanceIdentifierEntity(String string, String string2) {
		super(string, string2);
		
	}
	
	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad entity = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.Entity),Config.getProvenanceGraphLabel());
		Quad pi = new Quad(subject, RDF.type.toString(),new Object(Config.getNamespace()+"provenanceIdentifierEntity"),Config.getProvenanceGraphLabel());
		list.add(entity);
		list.add(pi);
		return list;
	}
}
