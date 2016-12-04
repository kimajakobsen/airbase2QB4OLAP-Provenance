package dk.aau.cs.qweb.airbase.database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import dk.aau.cs.qweb.airbase.Config;
import dk.aau.cs.qweb.airbase.types.Quad;
import dk.aau.cs.qweb.airbase.types.TripleContainer;

public class TurtleFile extends Database {

	@Override
	public void writeToDisk(TripleContainer triples) {
		Set<Quad> all = new HashSet<Quad>();
		all.addAll(triples.getInformationTriples());
		all.addAll(triples.getMetadataTriples());
		all.addAll(triples.getProvenanceTriples());
		
		if (all.size() > 0) {
			System.out.println("writing "+all.size()+" triples to "+Config.getDBLocation()+Config.getCurrentInputFileName()+".nq");
			try(FileWriter fw = new FileWriter(Config.getDBLocation()+Config.getCurrentInputFileName()+".nq", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
				{
					for (Quad quad : all) {
						out.println(quad.toString());
					}
				} catch (IOException e) {
				}
		}
	}

	@Override
	public void cleanWrite() throws IOException {
		File file = new File(Config.getDBLocation()+Config.getCurrentInputFileName()+".nq");
		if (file.exists() && Config.isDbCleanWrite()) {
			FileUtils.forceDelete(file);
		}
		
	}

}
