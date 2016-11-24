package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Software extends Agent implements PROV {

	public Software(String string) {
		subject = Config.getProvenanceGraphLabel()+"software/"+string;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		list.addAll(super.getType());
		Quad agent = new Quad(subject, RDF.type.toString(),PROVvocabulary.SoftwareAgent,Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}
	
	
}
