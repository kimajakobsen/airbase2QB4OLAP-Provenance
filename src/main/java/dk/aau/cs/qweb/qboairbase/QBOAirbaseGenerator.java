package dk.aau.cs.qweb.qboairbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.jena.vocabulary.OWL;

import dk.aau.cs.qweb.qboairbase.database.Database;
import dk.aau.cs.qweb.qboairbase.input.FileStructure;
import dk.aau.cs.qweb.qboairbase.provenance.Provenance;
import dk.aau.cs.qweb.qboairbase.types.Quad;
import dk.aau.cs.qweb.qboairbase.types.QuadContainer;
import dk.aau.cs.qweb.qboairbase.types.Tuple;
import dk.aau.cs.qweb.qboairbase.vocabulary.DBpedia;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;
import dk.aau.cs.qweb.qboairbase.vocabulary.Yago;

/**
 * Main class that implements the QBOAirbase generation for a single country.
 * @author galarraga
 *
 */
public class QBOAirbaseGenerator {
	
	/**
	 * Entry point
	 * @param args
	 */
	public static void main(String[] args) {
		String singleDataFile = null;
		CommandLineParser parser = new DefaultParser();
		List<File> files = new ArrayList<File>();
		// create the Options
		Options options = new Options();
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("i", "input-folder", true, "path to folder");
		options.addOption("c", "config", true, "path to config file");
			
		// Parse the input arguments
		try {
		    CommandLine line = parser.parse( options, args );
				    
		    if (line.hasOption( "help" )) {
		    	printHelp(null,options);
		    	System.exit(0);
			} 
		    
		    if (line.hasOption( "input-folder" )) {
		    	files.add(new File(line.getOptionValue("input-folder")));
			} 
			
		    if (line.hasOption("config")) {
		    	try (BufferedReader br = new BufferedReader(new FileReader(line.getOptionValue("config")))) {

					String fileLine;
					while ((fileLine = br.readLine()) != null) {
						if (fileLine.startsWith("cubeStructure")) {
							Config.setCubeStructurePath(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("dbLocation")) {
							Config.setDBLocation(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("dbType")) {
							Config.setDbType(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("clean")) {
							Config.setDbCleanWrite(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("datafolder")) {
							Config.setDataFolder(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("input")) {
							singleDataFile = fileLine.split(" ")[1];
						}
					}
				}
		    }
		}
		catch( ParseException exp ) {
			printHelp(exp, options);
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		
		// Parse the input files		
		Database dbConnection = Database.build();
		assert(singleDataFile != null);
		FileStructure fileStructure;
		long times = System.currentTimeMillis();
		try {
			Config.setCurrentInputFilePath(singleDataFile);
			dbConnection.cleanWrite();
			fileStructure = new FileStructure(singleDataFile);
			Set<Quad> metadata = new LinkedHashSet<>();
			Set<Quad> attributes = new LinkedHashSet<>();
			String countryName = null;
			metadata.add(getMetadataGraphDescription());
			while (fileStructure.hasNext()) {
				Tuple tuple = (Tuple) fileStructure.next();
				String countryCode = tuple.getValue("country_iso_code");
				countryName = tuple.getValue("country_name");
				Config.setXMLfilePath(getXMLFile(countryCode));
				Config.setCountryCode(countryCode);
				// This object takes care of generating the quadruples associated
				// to a given tuple of the original Airbase dataset
				QuadContainer quadsContainer = new QuadContainer(tuple);				
				dbConnection.writeToDisk(quadsContainer.getInformationTriples());
				dbConnection.writeToDisk(quadsContainer.getProvenanceTriples());
				metadata.addAll(quadsContainer.getMetadataTriples());
				attributes.addAll(quadsContainer.getAttributeTriples());				
				Provenance.getInstance().clearProvenance();
			}
			
			dbConnection.writeToDisk(metadata);
			dbConnection.writeToDisk(getCountryLinks(countryName, Config.getCountryCode()));
			dbConnection.writeToDisk(attributes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data generation for file " + singleDataFile + " took " + ((System.currentTimeMillis() - times) / 1000) + "s"); 
	}
	
	/**
	 * Generates the quad 
	 * <http://qweb.cs.aau.dk/airbase/metadata> <http://qweb.cs.aau.dk/airbase/isMetadaGraph> "True"^^xsd:boolean <http://qweb.cs.aau.dk/airbase/provenance>
	 * needed, for example, to exclude the metadata graph from queries (as this graph is extremely large).
	 * @return
	 */
	private static Quad getMetadataGraphDescription() {
		String property = Config.getNamespace() + "isMetadataGraph/";
		return new Quad(Config.getMetadataGraphLabel(), property, new dk.aau.cs.qweb.qboairbase.types.Object("True", XSD.booleanType), Config.getProvenanceGraphLabel());
	}

	/**
	 * Get the links of the country to the provided country.
	 * @return
	 */
	private static Set<Quad> getCountryLinks(String countryName, String countryCode) {
		String input = null;
		if (countryCode.equals("MK")) {
			input = "Republic of Macedonia";
		} else {
			input = countryCode;
		}
		String countryNameWikified = WordUtils.capitalizeFully(input).replaceAll(" ", "_").replace("/", "");
		Set<Quad> result = new LinkedHashSet<>(2);
		String subject = Config.getNamespace() + "country/" + countryNameWikified;
		Quad dbpedia = new Quad(subject, OWL.sameAs.toString(), new dk.aau.cs.qweb.qboairbase.types.Object(DBpedia.Resource + countryNameWikified), Config.getMetadataGraphLabel());
		Quad yago = new Quad(subject, OWL.sameAs.toString(),  new dk.aau.cs.qweb.qboairbase.types.Object(Yago.Resource + countryNameWikified), Config.getMetadataGraphLabel());
		result.add(dbpedia);
		result.add(yago);
		return result;
	}

	private static String getXMLFile(String countryCode) {
		return Config.getDataFolder() + "/" + "AirBase_" + countryCode + "_v8/" + countryCode + "_meta.parsed.xml";
	}

	private static void printHelp(ParseException exp, Options options) {
		String header = "";
		HelpFormatter formatter = new HelpFormatter();
		if (exp != null) {
			header = "Unexpected exception:" + exp.getMessage();
		}
		formatter.printHelp("Provenance Enabled Cubes App", header, options, null, true);
	}

}
