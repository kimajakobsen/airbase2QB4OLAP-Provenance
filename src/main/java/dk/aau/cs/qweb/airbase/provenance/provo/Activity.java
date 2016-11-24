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
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.vocabulary.PROVvocabulary;

public class Activity implements PROV {
	private String subject = "";
	private List<Entity> used = new ArrayList<Entity>();
	private List<Agent> wasAssociatedWith = new ArrayList<Agent>();
	private Map<String,String> customProperties = new HashMap<String,String>();
	
	public Activity(String name) {
		subject = Config.getNamespace()+name;
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
		quads.addAll(getType());
		
		for (Entry<String, String> entry : customProperties.entrySet()) {
			quads.add(new Quad(subject, entry.getKey(),entry.getValue(),Config.getProvenanceGraphLabel()));
		}
		
		for (Entity entity : used) {
			quads.addAll(entity.getQuads());
		}
		
		for (Agent agent : wasAssociatedWith) {
			quads.addAll(agent.getQuads());
		}
		
		return quads;
	}

	@Override
	public List<Quad> getType() {
		List<Quad> list = new ArrayList<Quad>();
		Quad agent = new Quad(subject, RDF.type.toString(),PROVvocabulary.Activity,Config.getProvenanceGraphLabel());
		list.add(agent);
		return list;
	}

	@Override
	public void setCustomProperty(String property, String value) {
		customProperties.put(property, value);
	}

}
