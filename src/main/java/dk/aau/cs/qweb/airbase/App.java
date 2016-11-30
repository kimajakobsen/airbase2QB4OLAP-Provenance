package dk.aau.cs.qweb.airbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FilenameUtils;

import dk.aau.cs.qweb.airbase.database.Database;
import dk.aau.cs.qweb.airbase.input.FileStructure;
import dk.aau.cs.qweb.airbase.types.TripleContainer;
import dk.aau.cs.qweb.airbase.types.Tuple;

public class App {

	public static void main(String[] args) {
		
		CommandLineParser parser = new DefaultParser();
		List<File> files = new ArrayList<File>();
		Database dbConnection = new Database();
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
						if (fileLine.startsWith("cubeStructure")) {
							Config.setCubeStructurePath(fileLine.split(" ")[1]);
						} else if (fileLine.startsWith("dbLocation")) {
							Config.setDBLocation(fileLine.split(" ")[1]);
						}
						else if (fileLine.startsWith("datafolder")) {
							File folder = new File(fileLine.split(" ")[1]);
							System.out.println(folder);
							for (final File fileEntry : folder.listFiles()) {
						        if (fileEntry.isDirectory()) {
						        	files.add(fileEntry);
						        } 
						    }
						} 
					}
				}
		    }
			dbConnection.clearDB();
		}
		catch( ParseException exp ) {
			printHelp(exp, options);
		} 
		catch (Exception exp) {
			exp.printStackTrace();
		}
		
		for (File folder : files) {
			List<String> csvFiles = new ArrayList<String>();
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					for (final File XMLFile : fileEntry.listFiles()) {
						Config.setXMLfilePath(XMLFile.toString());
					}
				} else if (FilenameUtils.getExtension(fileEntry.toString()).equals("csv")) {
					csvFiles.add(fileEntry.toString());
				} 
			}
			
			for (String file : csvFiles) {
				FileStructure fileStructure;
				try {
					fileStructure = new FileStructure(file);
					System.out.println(file);
					while (fileStructure.hasNext()) {
						Config.setCurrentInputFilePath(file);
						Tuple tuple = (Tuple) fileStructure.next();
						TripleContainer triples = new TripleContainer(tuple);
						
						dbConnection.writeToDisk(triples);
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
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
