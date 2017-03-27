package dk.aau.cs.qweb.airbase.types;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.vocabulary.RDF;

import dk.aau.cs.qweb.airbase.Airbase2QB4OLAP;
import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.Qb4OLAP.CubeStructure;
import dk.aau.cs.qweb.airbase.Qb4OLAP.HierarchyStep;
import dk.aau.cs.qweb.airbase.callback.CallBack;
import dk.aau.cs.qweb.airbase.provenance.Provenance;

public class TripleContainer {

	private Tuple tuple;
	private Set<Quad> informationTriples = new HashSet<Quad>();
	private Set<Quad> metadataTriples = new HashSet<Quad>();
	private static int measureCounter = 1;
	
	public TripleContainer(Tuple tuple) throws FileNotFoundException, IOException {
		this.tuple = tuple;
		
		if (tupleIsAllowed()) {
			int index = 0;
			
			for (String predicateString : this.tuple.getHeader()) {
				
				if (Airbase2QB4OLAP.isPredicatePartOfCube(predicateString)) {
					String predicate = Airbase2QB4OLAP.getPredicate(predicateString);
					List<String> files = Airbase2QB4OLAP.getFiles(predicateString);
					if (predicate.equals("measure")) {
						predicate = "http://qweb.cs.aau.dk/airbase/schema/"+removeIllegalChars(tuple.getValue("component_caption"));
					}
					
					List<String> levels = Airbase2QB4OLAP.getLevels(predicateString); 
					
					for (String level : levels) {
						String literal = tuple.getData().get(index);
						String subject = createSubject(level);
						CallBack cleanFunction = Airbase2QB4OLAP.getCallbackFunctionRawPredicate(predicateString);
						Object object = new Object(literal);
						
						if (cleanFunction != null) {
							object = cleanFunction.callBackMethod(literal,tuple);
						}
						
						if (!object.isEmpty()) {
							Quad quad =  new Quad(subject,predicate,(object));
							
							String graphLabel = getGraphLabel(quad, level, files, tuple);
							quad.setGraphLabel(graphLabel);
							
							metadataTriples.addAll(createMetadata(subject, level));
							informationTriples.add(quad);
						}
					}
				}
				index++;
			}
		}
	}

	private String removeIllegalChars(String value) {
		value = value.replaceAll(",", "");
		value = value.replaceAll("\\(", "");
		value = value.replaceAll("\\)", "");
		value = value.replaceAll(" ", "_");
		return value;
	}

	private boolean tupleIsAllowed() {
		if (Config.getCurrentInputFilePath().contains("statistics")) {
			
			if (!tuple.getValue("statistic_shortname").equals("Mean")) {
				return false;
			} else if (Airbase2QB4OLAP.getAllowedComponents().contains(tuple.getValue("component_caption"))) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private String getGraphLabel(Quad quad, String level, Collection<String> files, Tuple tuple) {
		Provenance index = Provenance.getInstance();
		//System.out.println(quad);
		String provenanceIdentifier = index.getProvenanceIdentifier(quad, level, files, tuple);
		//System.out.println(index.getProvenanceGraph(provenanceIdentifier));
		return provenanceIdentifier;
	}

	private String createSubject(String level) {
		String subject = Config.getDataNamespace();
		if (level.equals("http://qweb.cs.aau.dk/airbase/schema/value")) {
			subject+="observation/"+Config.getCountryCode()+measureCounter;
			measureCounter++;
		} else {
			List<String> attributes = Airbase2QB4OLAP.getAttributesUsedInIRI(level);
			
			subject += removePrefix(level)+"/";
			for (String index : attributes) {
				subject += tuple.getValue(index)+"_";
			}
			subject = replacelastUnderscoreWithSlash(subject);
			subject = subject.replaceAll(" ", "_");
		}
		
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
		//level = cs.transformPrefixIntoFullURL(level);
		Set<Quad> quads = new HashSet<Quad>();
		
		if (level.equals("http://qweb.cs.aau.dk/airbase/schema/value")) { //Handel Observations
			Quad quad1 = new Quad(subject, RDF.type.toString() , new Object("http://purl.org/linked-data/cube#Observation"),Config.getMetadataGraphLabel());
			quads.add(quad1);
			
			Quad quad2 = new Quad(subject, RDF.type.toString() , new Object("http://purl.org/linked-data/cube#dataSet"),Config.getMetadataGraphLabel());
			quads.add(quad2);
			
			Quad year = new Quad(subject, "http://qweb.cs.aau.dk/airbase/schema/year", new Object (createSubject("http://qweb.cs.aau.dk/airbase/schema/year")),Config.getMetadataGraphLabel());
			quads.add(year);
			
			Quad sensor = new Quad(subject, "http://qweb.cs.aau.dk/airbase/schema/sensor", new Object (createSubject("http://qweb.cs.aau.dk/airbase/schema/sensor")),Config.getMetadataGraphLabel());
			quads.add(sensor);
			
			Quad station = new Quad(subject, "http://qweb.cs.aau.dk/airbase/schema/station", new Object (createSubject("http://qweb.cs.aau.dk/airbase/schema/station")),Config.getMetadataGraphLabel());
			quads.add(station);
			
		} else {
			Quad quad1 = new Quad(subject, "http://purl.org/qb4olap/cubes#memberOf" , new Object(level),Config.getMetadataGraphLabel());
			quads.add(quad1);
			for (HierarchyStep hs : cs.getHierarchyStepByParentLevel(level)) {
				String childLevel = createSubject(hs.getChildLevel());
				Quad quad2 = new Quad(childLevel, hs.getRollup() , new Object(subject) ,Config.getMetadataGraphLabel());
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
		Provenance index = Provenance.getInstance();
		return index.getProvenanceTriples();
	}
}
