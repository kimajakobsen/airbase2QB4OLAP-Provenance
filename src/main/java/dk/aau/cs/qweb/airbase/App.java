package dk.aau.cs.qweb.airbase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import dk.aau.cs.qweb.airbase.database.Database;
import dk.aau.cs.qweb.airbase.parser.InputParser;
import dk.aau.cs.qweb.airbase.types.CubeStructure;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class App {

	public static void main(String[] args) {
		
		CommandLineParser parser = new DefaultParser();
		CubeStructure structure = null;
		List<String> files = new ArrayList<String>();
		// create the Options
		Options options = new Options();
		options.addOption("h", "help", false, "Display this message." );
		options.addOption("c", "config", true, "path to config file");
				
		try {
		    CommandLine line = parser.parse( options, args );
				    
		    if (line.hasOption( "help" )) {
		    	printHelp(null,options);
		    	System.exit(0);
			} 
			
		    if (line.hasOption("config")) {
		    	try (BufferedReader br = new BufferedReader(new FileReader(line.getOptionValue("config")))) {

					String fileLine;
					while ((fileLine = br.readLine()) != null) {
						if (fileLine.startsWith("cube-structure")) {
							structure = new CubeStructure(fileLine.split(" ")[1]);
						}
						else if (fileLine.startsWith("input-file")) {
							files.add(fileLine.split(" ")[1]);
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
		
		Database dbConnection = new Database();
		
		
		for (String file : files) {
			InputParser inputParser = new InputParser(file,structure);
			
			
			for (Tuple tuple : inputParser.getTuples()) {
				dbConnection.writeToDisk(tuple);
			}
		}
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
