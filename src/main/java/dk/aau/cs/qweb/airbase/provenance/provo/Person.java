package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;

import java.util.List;
import java.util.Map.Entry;

import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;

public class Person extends Agent implements PROV {

	public Person(String string) {
		subject = Config.getProvenanceGraphLabel()+"agent/"+string;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		list.addAll(super.getType());
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(FOAF.Person.toString()),Config.getProvenanceGraphLabel());
		
		for (Entry<String, Object> entry : customProperties.entrySet()) {
			list.add(new Quad(subject, entry.getKey(),entry.getValue(),Config.getProvenanceGraphLabel()));
		}
		
		list.add(agent);
		return list;
	}
}
