package dk.aau.cs.qweb.airbase.provenance.provo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.provenance.Provenance;
import dk.aau.cs.qweb.airbase.provenance.ProvenanceSignature;
import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Activity implements PROV {
	private String subject = "";
	private String shortName = "";
	private List<Entity> used = new ArrayList<Entity>();
	private List<Agent> wasAssociatedWith = new ArrayList<Agent>();
	private Map<String,Object> customProperties = new HashMap<String,Object>();
	private static int counter = 1;
	
	public Activity(String name) {
		shortName = Config.getCountryCode()+counter;
		subject = Config.getNamespace()+name+"/"+shortName;
		counter++;
	}
	
	public Activity(String name, ProvenanceSignature signature) {
		shortName = signature.getFileName()+signature.getTuple().getLineCount();
		subject = Config.getNamespace()+name+"/"+ shortName;
	}

	public Activity(String string, String rawDataFileName) {
		shortName = string+"/"+rawDataFileName;
		subject = Config.getNamespace() + shortName;
	}

	public void used(Entity entity) {
		used.add(entity);
	}

	public void wasAssociatedWith(Agent thisSoftware) {
		wasAssociatedWith.add(thisSoftware);
	}

	@Override
	public Set<Quad> getQuads() {
		Set<Quad> quads = new HashSet<Quad>();
		if (Provenance.getInstance().subjectExists(subject)) {
			return quads;
		}
		
		quads.addAll(getType());
		
		for (Entry<String, Object> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),(entry.getValue()),Config.getProvenanceGraphLabel()));
		}
		
		for (Entity entity : used) {
			quads.add(new Quad(subject, PROVvocabulary.used,new Object(entity.getSubject()),Config.getProvenanceGraphLabel()));
			quads.addAll(entity.getQuads());
		}
		
		for (Agent agent : wasAssociatedWith) {
			quads.add(new Quad(subject, PROVvocabulary.wasAssociatedWith,new Object(agent.getSubject()),Config.getProvenanceGraphLabel()));
			quads.addAll(agent.getQuads());
		}
		
		Provenance.getInstance().registerSubject(subject);
		
		return quads;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),new Object(PROVvocabulary.Activity),Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}

	@Override
	public void setCustomProperty(String property, Object value) {
		customProperties.put(property, value);
	}

	public String getSubject() {
		return subject;
	}

	public String getShortName() {
		return shortName;
	}

}
