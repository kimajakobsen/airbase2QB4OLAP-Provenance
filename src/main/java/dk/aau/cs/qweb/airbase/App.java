package dk.aau.cs.qweb.airbase;

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
import org.apache.jena.vocabulary.OWL;

import dk.aau.cs.qweb.airbase.database.Database;
import dk.aau.cs.qweb.airbase.input.FileStructure;
import dk.aau.cs.qweb.airbase.provenance.Provenance;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.TripleContainer;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.DBpedia;
import dk.aau.cs.qweb.airbase.vocabulary.Yago;

public class App {

	public static void main(String[] args) {
		String singleDataFile = null;
		CommandLineParser parser = new DefaultParser();
		List<File> files = new ArrayList<File>();
		// create the Options
		Options options = new Options();
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("i", "input-folder", true, "path to folder");
		options.addOption("c", "config", true, "path to config file");
				
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
			while (fileStructure.hasNext()) {
				Tuple tuple = (Tuple) fileStructure.next();
				String countryCode = tuple.getValue("country_iso_code");
				countryName = tuple.getValue("country_name");
				Config.setXMLfilePath(getXMLFile(countryCode));
				Config.setCountryCode(countryCode);
				TripleContainer triples = new TripleContainer(tuple);
				dbConnection.writeToDisk(triples.getInformationTriples());
				dbConnection.writeToDisk(triples.getProvenanceTriples());
				metadata.addAll(triples.getMetadataTriples());
				attributes.addAll(triples.getAttributeTriples());
				Provenance.getInstance().clearProvenance();
			}
			dbConnection.writeToDisk(metadata);
			dbConnection.writeToDisk(getCountryLinks(countryName));
			dbConnection.writeToDisk(attributes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Data generation for file " + singleDataFile + " took " + ((System.currentTimeMillis() - times) / 1000) + "s"); 
	}
	
	/**
	 * Get the links of the country to the
	 * @return
	 */
	private static Set<Quad> getCountryLinks(String countryName) {
		Set<Quad> result = new LinkedHashSet<>(2);
		String subject = "<" + Config.getNamespace() + "/country/" + countryName + "/>";
		Quad dbpedia = new Quad(subject, OWL.sameAs.toString(), new dk.aau.cs.qweb.airbase.types.Object(DBpedia.Resource + countryName), Config.getMetadataGraphLabel());
		Quad yago = new Quad(subject, OWL.sameAs.toString(),  new dk.aau.cs.qweb.airbase.types.Object(Yago.Resource + countryName), Config.getMetadataGraphLabel());
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
