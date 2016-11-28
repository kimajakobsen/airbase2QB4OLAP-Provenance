package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;

import java.util.List;

import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.RDF;
import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;

public class Organization extends Agent implements PROV {
	
	public Organization(String string) {
		subject = Config.getNamespace()+"Organization/"+string;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		list.addAll(super.getType());
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(FOAF.Organization.toString()),Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}
}
