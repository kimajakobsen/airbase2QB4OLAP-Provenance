package dk.aau.cs.qweb.airbase.vocabulary;

public class XSD {
	//Namespaces
	public static String xsdNS = "http://www.w3.org/ns/prov#";
	
	public static String booleanType = "^^"+"<"+xsdNS+"boolean"+">";
	public static String stringType = "^^"+"<"+xsdNS+"string"+">";
	public static String integerType = "^^"+"<"+xsdNS+"integer"+">";
	public static String decimalType = "^^"+"<"+xsdNS+"decimal"+">";
	public static String dateType = "^^"+"<"+xsdNS+"date"+">";
}
