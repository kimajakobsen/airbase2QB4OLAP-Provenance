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
import org.apache.commons.io.FilenameUtils;

import dk.aau.cs.qweb.airbase.database.Database;
import dk.aau.cs.qweb.airbase.input.FileStructure;
import dk.aau.cs.qweb.airbase.provenance.Provenance;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.TripleContainer;
import dk.aau.cs.qweb.airbase.types.Tuple;

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
		try {
			Config.setCurrentInputFilePath(singleDataFile);
			dbConnection.cleanWrite();
			fileStructure = new FileStructure(singleDataFile);
			Set<Quad> metadata = new LinkedHashSet<>();
			while (fileStructure.hasNext()) {
				Tuple tuple = (Tuple) fileStructure.next();
				String countryCode = tuple.getValue("country_iso_code");
				Config.setXMLfilePath(getXMLFile(countryCode));
				Config.setCountryCode(countryCode);
				TripleContainer triples = new TripleContainer(tuple);
				dbConnection.writeToDisk(triples.getInformationTriples());
				dbConnection.writeToDisk(triples.getProvenanceTriples());
				metadata.addAll(triples.getMetadataTriples());
				Provenance.getInstance().clearProvenance();
			}
			dbConnection.writeToDisk(metadata);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
