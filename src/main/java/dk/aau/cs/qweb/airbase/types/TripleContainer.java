package dk.aau.cs.qweb.airbase.types;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.qweb.airbase.Airbase2QB4OLAP;
import dk.aau.cs.qweb.airbase.Config;

public class TripleContainer {

	private Tuple tuple;

	public TripleContainer(Tuple tuple) {
		this.tuple = tuple;
		List<Quad> informationTriples = new ArrayList<Quad>();
		List<Quad> metadataTriples = new ArrayList<Quad>();
		List<Quad> provenanceTriples = new ArrayList<Quad>();
		
		int index = 0;
		System.out.println(tuple);
		for (String predicateString : this.tuple.getHeader()) {
			
			if (Airbase2QB4OLAP.isPredicatePartOfCube(predicateString)) {
				String predicate = Airbase2QB4OLAP.getPredicate(predicateString);
				String object = tuple.getData().get(index);
				List<String> levels = Airbase2QB4OLAP.getLevels(predicateString); 
				for (String level : levels) {
					String subject = createSubject(level);
					Quad quad =  new Quad(subject,predicate,object);
					System.out.println(quad);
					String graphLabel = getGraphLabel(quad);
					quad.setGraphLabel(graphLabel);
					
					informationTriples.add(quad);
				}
			}
			index++;
		}
	}

	private String getGraphLabel(Quad quad) {
		// TODO Auto-generated method stub
		return null;
	}

	private String createSubject(String level) {
		List<String> attributes = Airbase2QB4OLAP.getAttributesUsedInIRI(level);
		String subject = Config.getNamespace();
		for (String index : attributes) {
			subject += tuple.getValue(index)+"_";
		}
		subject = replacelastUnderscoreWithSlash(subject);
		return subject;
	}

	private String replacelastUnderscoreWithSlash(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='_') {
	      str = str.substring(0, str.length()-1);
	      str += "/";
	    }
	    return str;
	}

}
