package dk.aau.cs.qweb.airbase.types;

import java.util.ArrayList;
import java.util.List;

public class TripleContainer {

	private Tuple tuple;

	public TripleContainer(Tuple tuple) {
		this.tuple = tuple;
		List<Quad> triples = new ArrayList<Quad>();
		String subject = createSubject();
		int index = 0;
		
		for (String predicateString : this.tuple.getHeader()) {
			String predicate = CubeStructure.getPredicate(predicateString);
			
			if (!predicate.equals(null)) {
				String object = tuple.getData().get(index);
				Quad quad =  new Quad(subject,predicate,object);
				String graphLabel = getGraphLabel(quad);
				quad.setGraphLabel(graphLabel);
				triples.add(quad);
			}
			index++;
		}
		
	}

	private String getGraphLabel(Quad quad) {
		// TODO Auto-generated method stub
		return null;
	}

	private String createSubject() {
		// TODO Auto-generated method stub
		return null;
	}

}
