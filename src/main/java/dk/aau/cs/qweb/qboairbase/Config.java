package dk.aau.cs.qweb.qboairbase;

public class Config {

	private static String cubeStructurePath = "src/test/resources/Ontology/Airbase.ttl";
	private static String namespace = "http://qweb.cs.aau.dk/airbase/";
	private static String metadataGraphLabel = namespace+"metadata/";
	private static String provenanceGraphLabel = namespace+"provenance/";
	private static String countryCode; 
	private static String file;
	private static String xmlFile;
	private static String dbLocation;
	private static String dbType;
	private static boolean dbCleanWrite;
	private static String dataFolder;

	public static String getNamespace() {
		return namespace;
	}

	public static String getCubeStructureFilePath() {
		return cubeStructurePath;
	}

	public static void setCubeStructurePath(String cubeStructurePath) {
		Config.cubeStructurePath = cubeStructurePath;
	}

	public static String getMetadataGraphLabel() {
		return metadataGraphLabel ;
	}

	public static void setCurrentInputFilePath(String file) {
		Config.file = file;
	}
	
	public static String getCurrentInputFilePath() {
		return file;
	}

	public static String getProvenanceGraphLabel() {
		return provenanceGraphLabel;
	}

	public static String getXMLfilePath() {
		return xmlFile;
	}

	public static void setXMLfilePath(String path) {
		xmlFile = path;
	}

	public static String getDBLocation() {
		return dbLocation;
	}

	public static void setDBLocation(String dbLocation) {
		Config.dbLocation = dbLocation;
	}

	public static String getDbType() {
		return dbType;
	}

	public static void setDbType(String dbType) {
		Config.dbType = dbType;
	}

	public static boolean isDbCleanWrite() {
		return dbCleanWrite;
	}

	public static void setDbCleanWrite(String dbCleanWrite) {
		if (dbCleanWrite.equals("true")) {
			Config.dbCleanWrite = true;
		} else {
			Config.dbCleanWrite = false;
		}
	}
	
	public static void setCountryCode(String string) {
		countryCode = string;
	}

	public static String getCountryCode() {
		return countryCode;
	}

	public static String getDataNamespace() {
		return namespace+"data/";
	}

	public static String getCurrentInputFileName() {
		String[] slashSplit = file.split("/");
		String[] dotSplit = slashSplit[slashSplit.length-1].split("\\.");
		return dotSplit[0];
	}

	public static void setDataFolder(String string) {
		dataFolder = string;
	}
	
	public static String getDataFolder() {
		return dataFolder;
	}
}
