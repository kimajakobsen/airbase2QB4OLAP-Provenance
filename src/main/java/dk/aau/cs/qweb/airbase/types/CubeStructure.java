package dk.aau.cs.qweb.airbase.types;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.Qb4OLAP.Attribute;
import dk.aau.cs.qweb.airbase.Qb4OLAP.Dimension;
import dk.aau.cs.qweb.airbase.Qb4OLAP.Hierarchy;
import dk.aau.cs.qweb.airbase.Qb4OLAP.HierarchyStep;
import dk.aau.cs.qweb.airbase.Qb4OLAP.Level;
import dk.aau.cs.qweb.airbase.Qb4OLAP.Measure;

public class CubeStructure {
	private List<String> prefix = new ArrayList<String>();
	private String dataStructureDefinition;
	private List<Measure> meassures = new ArrayList<Measure>();
	private List<Dimension> dimensions = new ArrayList<Dimension>();
	private List<Hierarchy> hierarchies = new ArrayList<Hierarchy>();
	private List<HierarchyStep> hierarchySteps = new ArrayList<HierarchyStep>();
	private List<Level> levels = new ArrayList<Level>();
	private List<String> rollupProperties = new ArrayList<String>();
	private List<Attribute> attributes = new ArrayList<Attribute>();
	private String lineBuffer = "";
	private String title ="";
	
	private static CubeStructure instance = null;
	private CubeStructure() throws FileNotFoundException, IOException {
		String path = Config.getCubeStructureFilePath();
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	lineBuffer += line+" ";
		        if (endOfStatement(line)) {
					parseTriplesStatement(lineBuffer);
					lineBuffer = "";
				}
		    }
		}
		
		
	}
	private void parseTriplesStatement(String line) {
		line = line.trim(); //TODO ensure that spaces between elements are not trimmed
		if (line.startsWith("@prefix")) {
			addPrefix(line);
		} else if (line.isEmpty()) {
			//line is empty
		} else if (line.startsWith("#")) {
			//Comment
		} else if (line.contains("[") && line.contains("]")) {
			parseLineWithSquareBrackets(line);
		} else if (line.startsWith("_:")) {
			parseLineWithBlankNode(line);
		} else if (prefix.contains(line.split(":")[0])) {
			parseLine(line);
		} else {
			throw new IllegalStateException("the Cube Structure file contains an line that cannot be passed: "+ line);
		}
	}
	
	private void parseLine(String line) {
		String[] colonSplit = line.split(";");
		colonSplit = transformPrefixIntoFullURL(colonSplit);
		String subject = "";
		String predicate = "";
		String object = "";
		String type = "";
		
		for (String colonSplitLine : colonSplit) {
			
			for (String commaSplitLine : colonSplitLine.split(",")) {
				String[] elements = commaSplitLine.split(" ");

				if (elements.length == 3) {
					subject = elements[0];
					predicate = elements[1];
					object = elements[2];
				} else if (elements.length == 2) {
					predicate = elements[1];
					object = elements[2];
				} else if (elements.length == 1) {
					object = elements[2];
				} else {
					throw new IllegalArgumentException("unexpected number of elements in triple: "+ elements.toString());
				}
					
				if (predicate.equals("a")) {
					type = elements[2];
				}
				addTripleToStructure(subject,predicate,object,type);
			}
		}
	}

	private String[] transformPrefixIntoFullURL(String[] colonSplit) {
		// TODO Auto-generated method stub
		return null;
	}
	private void addTripleToStructure(String subject, String predicate, String object, String type) {
		if (type.equals("http://purl.org/linked-data/cube#DataStructureDefinition")) {
			if (predicate.equals("a")) {
				dataStructureDefinition = subject;
			}
		} else if (type.equals("http://purl.org/linked-data/cube#DataSet")) {
			if (predicate.equals("http://purl.org/dc/terms/title")) {
				title = object;
			}
		} else if (type.equals("http://purl.org/linked-data/cube#MeasureProperty")) {
			boolean exists = false;
			for (Measure measure : meassures) {
				if (measure.equals(subject)) {
					if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
						measure.setLabel(object);
					} else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#range")) {
						measure.setRange(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				Measure measure = new Measure(subject);
				meassures.add(measure);
			}
		} else if (type.equals("http://purl.org/linked-data/cube#DimensionProperty")) {
			boolean exists = false;
			for (Dimension dimension : dimensions) {
				if (dimension.equals(subject)) {
					if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
						dimension.setLabel(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#hasHierarchy")) {
						dimension.addHierarchy(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				Dimension dimension = new Dimension(subject);
				dimensions.add(dimension);
			}
		} else if (type.equals("http://purl.org/qb4olap/cubes#Hierarchy")) {
			boolean exists = false;
			for (Hierarchy hierarchy : hierarchies) {
				if (hierarchy.equals(subject)) {
					if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
						hierarchy.setLabel(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#inDimension")) {
						hierarchy.addInDimension(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#hasLevel")) {
						hierarchy.addLevel(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				Hierarchy hierarchy = new Hierarchy(subject);
				hierarchies.add(hierarchy);
			}
		} else if (type.equals("http://purl.org/qb4olap/cubes#HierarchyStep")) {
			boolean exists = false;
			for (HierarchyStep hierarchyStep : hierarchySteps) {
				if (hierarchyStep.equals(subject)) {
					if (predicate.equals("http://purl.org/qb4olap/cubes#childLevel")) {
						hierarchyStep.setChildLevel(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#inHierarchy")) {
						hierarchyStep.setHierarchy(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#parentLevel")) {
						hierarchyStep.setParentLevel(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#pcCardinality")) {
						hierarchyStep.setCardinality(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#rollup")) {
						hierarchyStep.setRollup(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				HierarchyStep hierarchyStep = new HierarchyStep(subject);
				hierarchySteps.add(hierarchyStep);
			}
		} else if (type.equals("http://purl.org/qb4olap/cubes#RollupProperty")) {
			boolean exists = false;
			for (String rollupProperty : rollupProperties) {
				if (rollupProperty.equals(subject)) {
					exists = true;
				}
			}
			if (!exists) {
				rollupProperties.add(subject);
			}
		} else if (type.equals("http://purl.org/qb4olap/cubes#LevelProperty")) {
			boolean exists = false;
			for (Level level : levels) {
				if (level.equals(subject)) {
					if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
						level.setLabel(object);
					} else if (predicate.equals("http://purl.org/qb4olap/cubes#hasAttribute")) {
						level.addAttribute(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				HierarchyStep hierarchyStep = new HierarchyStep(subject);
				hierarchySteps.add(hierarchyStep);
			}
		} else if (type.equals("http://purl.org/qb4olap/cubes#LevelAttribute")) {
			boolean exists = false;
			for (Attribute attribute : attributes) {
				if (attribute.equals(subject)) {
					if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#label")) {
						attribute.setLabel(object);
					} else if (predicate.equals("http://www.w3.org/2000/01/rdf-schema#range")) {
						attribute.setRange(object);
					}
					exists = true;
				}
			}
			if (!exists) {
				Attribute attribute = new Attribute(subject);
				attributes.add(attribute);
			}
		} else {
			throw new IllegalArgumentException("Not able to parse the line: "+ subject + " " +predicate+" " +object);
		}
	}
	private void parseLineWithBlankNode(String line) {
		parseLine(line);
	}
	
	private void parseLineWithSquareBrackets(String line) {
		// TODO Auto-generated method stub
		
	}
	private void addPrefix(String line) {
		prefix.add(line);
	}
	private boolean endOfStatement(String line) {
		return line.matches(".+\\.\\s*") ? true : false;
	}
	
	public static CubeStructure getInstance() throws FileNotFoundException, IOException {
		if(instance == null) {
			instance = new CubeStructure();
		}
		return instance;
	}

	public List<Integer> getPrimaryKeyIndexes(List<String> list, String predicate) {
		
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return title;
	}
	
	
	public String getDataStructureDefinition() {
		return dataStructureDefinition;
	}
}
