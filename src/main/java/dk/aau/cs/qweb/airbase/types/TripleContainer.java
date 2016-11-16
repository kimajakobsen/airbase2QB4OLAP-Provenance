package dk.aau.cs.qweb.airbase.types;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.qweb.airbase.Config;

public class TripleContainer {

	private Tuple tuple;

	public TripleContainer(Tuple tuple) {
		this.tuple = tuple;
		List<Quad> informationTriples = new ArrayList<Quad>();
		List<Quad> metadataTriples = new ArrayList<Quad>();
		List<Quad> provenanceTriples = new ArrayList<Quad>();
		
		int index = 0;
		
		for (String predicateString : this.tuple.getHeader()) {
			
			if (CubeStructure.isPredicatePartOfCube(predicateString)) {
				String predicate = CubeStructure.getPredicate(predicateString);
				String subject = createSubject(predicate);
				String object = tuple.getData().get(index);
				
				Quad quad =  new Quad(subject,predicate,object);
				String graphLabel = getGraphLabel(quad);
				quad.setGraphLabel(graphLabel);
				
				informationTriples.add(quad);
			}
			index++;
		}
	}

	private String getGraphLabel(Quad quad) {
		// TODO Auto-generated method stub
		return null;
	}

	private String createSubject(String predicate) {
		List<Integer> indexes = CubeStructure.getPrimaryKeyIndexes(tuple.getHeader(),predicate);
		String subject = Config.getNamespace();
		for (Integer index : indexes) {
			subject += tuple.getData().get(index)+"_";
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
