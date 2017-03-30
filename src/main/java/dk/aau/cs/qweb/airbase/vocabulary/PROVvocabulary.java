package dk.aau.cs.qweb.airbase.vocabulary;

public class PROVvocabulary {
	//Namespaces
	public static String provNS = "http://www.w3.org/ns/prov#";
	
	//Prov types
	public static String Entity = provNS+"Entity";
	public static String Activity = provNS+"Activity";
	public static String Agent = provNS+"Agent";
	public static String Bundle = provNS+"Bundle";
	public static String SoftwareAgent = provNS+"SoftwareAgent";
	
	//prov properties
	public static String wasAttributedTo = provNS+"wasAttributedTo";
	public static String used = provNS+"used";
	public static String generated = provNS+"generated";
	public static String startedAtTime = provNS+"startedAtTime";
	public static String endedAtTime = provNS+"endedAtTime";
	public static String wasGeneratedBy = provNS+"wasGeneratedBy";
	public static String generatedAtTime = provNS+"generatedAtTime";
	public static String atLocation = provNS+"atLocation";
	public static String wasAssociatedWith = provNS+"wasAssociatedWith";
	public static String wasDerivedFrom = provNS+"wasDerivedFrom";
}
