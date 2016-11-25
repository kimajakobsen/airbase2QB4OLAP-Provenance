package dk.aau.cs.qweb.airbase.types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.vocabulary.RDFS;

import dk.aau.cs.qweb.airbase.Airbase2QB4OLAP;
import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.Qb4OLAP.CubeStructure;
import dk.aau.cs.qweb.airbase.Qb4OLAP.HierarchyStep;
import dk.aau.cs.qweb.airbase.callback.CallBack;
import dk.aau.cs.qweb.airbase.provenance.ProvenanceIndex;

public class TripleContainer {

	private Tuple tuple;
	private Set<Quad> informationTriples = new HashSet<Quad>();
	private Set<Quad> metadataTriples = new HashSet<Quad>();

	public TripleContainer(Tuple tuple) throws FileNotFoundException, IOException {
		this.tuple = tuple;
		
		
		int index = 0;
		for (String predicateString : this.tuple.getHeader()) {
			
			if (Airbase2QB4OLAP.isPredicatePartOfCube(predicateString)) {
				String predicate = Airbase2QB4OLAP.getPredicate(predicateString);
				String object = tuple.getData().get(index);
				List<String> levels = Airbase2QB4OLAP.getLevels(predicateString); 
				for (String level : levels) {
					String subject = createSubject(level);
					
					CallBack cleanFunction = Airbase2QB4OLAP.getCallbackFunction(predicateString);
					if (cleanFunction != null) {
						object = cleanFunction.callBackMethod(object);
					}
					 
					Quad quad =  new Quad(subject,predicate,(object));
					
					String graphLabel = getGraphLabel(quad,level,tuple);
					quad.setGraphLabel(graphLabel);
					
					metadataTriples.addAll(createMetadata(subject, level));
					System.out.println("");
					System.out.println(level);
					System.out.println(predicateString);
					System.out.println(quad);
					informationTriples.add(quad);
				}
			}
			index++;
		}
	}

	private String getGraphLabel(Quad quad,String level, Tuple tuple) {
		String file = Config.getCurrentInputFilePath();
		ProvenanceIndex index = ProvenanceIndex.getInstance();
		String provenanceIdentifier = index.getProvenanceIdentifier(quad,level,file,tuple);
		return provenanceIdentifier;
	}

	private String createSubject(String level) {
		List<String> attributes = Airbase2QB4OLAP.getAttributesUsedInIRI(level);
		String subject = Config.getNamespace();
		subject += removePrefix(level)+"/";
		for (String index : attributes) {
			subject += tuple.getValue(index)+"_";
		}
		subject = replacelastUnderscoreWithSlash(subject);
		return subject;
	}

	private String removePrefix(String level) {
		String[] split =level.split("/");
		return split[split.length-1];
	}

	private String replacelastUnderscoreWithSlash(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='_') {
	      str = str.substring(0, str.length()-1);
	      str += "/";
	    }
	    return str;
	}
	
	private Set<Quad> createMetadata(String subject, String level) throws FileNotFoundException, IOException {
		CubeStructure cs = CubeStructure.getInstance();
		level = cs.transformPrefixIntoFullURL(level);
		Set<Quad> quads = new HashSet<Quad>();
		
		if (level.equals("http://qweb.cs.aau.dk/airbase/schema/value")) { //Handel Observations
			Quad quad1 = new Quad(subject, RDFS.Datatype.toString() , "http://purl.org/linked-data/cube#Observation",Config.getMetadataGraphLabel());
			quads.add(quad1);
			Quad quad2 = new Quad(subject, RDFS.Datatype.toString() , "http://purl.org/linked-data/cube#dataSet",Config.getMetadataGraphLabel());
			quads.add(quad2);
		} else {
			Quad quad1 = new Quad(subject, "http://purl.org/qb4olap/cubes#memberOf" , level,Config.getMetadataGraphLabel());
			quads.add(quad1);
			for (HierarchyStep hs : cs.getHierarchyStepByParentLevel(level)) {
				String childLevel = createSubject(hs.getChildLevel());
				Quad quad2 = new Quad(childLevel, hs.getRollup() , subject ,Config.getMetadataGraphLabel());
				quads.add(quad2);
			}
		}
		return quads;
	}

	
	public Set<Quad> getInformationTriples() {
		return informationTriples;
	}
	
	public Set<Quad> getMetadataTriples() {
		return metadataTriples;
	}
	
	public Set<Quad> getProvenanceTriples() {
		ProvenanceIndex index = ProvenanceIndex.getInstance();
		return index.getProvenanceTriples();
	}
}
