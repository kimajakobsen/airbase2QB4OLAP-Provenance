package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import dk.aau.cs.qweb.airbase.types.Object;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.provenance.ProvenanceIndex;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Agent implements PROV {
	protected String subject = "";
	private Map<String,String> customProperties = new HashMap<String,String>();
	private String atLocation = "";
	
	public Agent(String string) {
		subject = Config.getProvenanceGraphLabel()+"agent/"+string;
	}
	
	protected Agent() {}

	@Override
	public Set<Quad> getQuads() {
		Set<Quad> quads = new HashSet<Quad>();
		if (ProvenanceIndex.contains(subject)) {
			return quads;
		} else {
			ProvenanceIndex.add(subject);
		}
		
		
		
		quads.addAll(getType());
		
		for (Entry<String, String> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),new Object(entry.getValue()),Config.getProvenanceGraphLabel()));
		}
		
		if (atLocation.isEmpty()) {
			quads.add(getAtLocation());
		}
		
		return quads;
	}
	
	private Quad getAtLocation() {
		return new Quad(subject,PROVvocabulary.atLocation,new Object(Config.getProvenanceGraphLabel()));
	}

	@Override
	public void setCustomProperty(String key, String value) {
		customProperties.put(key, value);
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.Agent),Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}

	public void atLocation(String string) {
		this.atLocation = string;
	}

}
