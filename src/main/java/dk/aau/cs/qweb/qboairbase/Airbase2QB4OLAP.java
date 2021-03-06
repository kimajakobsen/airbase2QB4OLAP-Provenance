package dk.aau.cs.qweb.qboairbase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dk.aau.cs.qweb.qboairbase.callback.CallBack;
import dk.aau.cs.qweb.qboairbase.callback.Date2Date;
import dk.aau.cs.qweb.qboairbase.callback.Decimal2Decimal;
import dk.aau.cs.qweb.qboairbase.callback.FrequenceAndUnit2Seconds;
import dk.aau.cs.qweb.qboairbase.callback.Integer2Integer;
import dk.aau.cs.qweb.qboairbase.callback.String2String;
import dk.aau.cs.qweb.qboairbase.callback.YesNo2TrueFalse;
import dk.aau.cs.qweb.qboairbase.types.ColumnMetadata;
import dk.aau.cs.qweb.qboairbase.types.Tuple;

public class Airbase2QB4OLAP {
	private static Map<String,ColumnMetadata> columns = new HashMap<String,ColumnMetadata>() {
		private static final long serialVersionUID = 4261847242888256230L;
		{
			put("component_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/code", level("http://qweb.cs.aau.dk/airbase/schema/component",true,"http://qweb.cs.aau.dk/airbase/schema/sensor",true), new Integer2Integer()));
			put("component_caption",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/caption",level("http://qweb.cs.aau.dk/airbase/schema/component",false),new String2String()));
			put("component_name",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/name",level("http://qweb.cs.aau.dk/airbase/schema/component",false),new String2String()));
			put("component_FWD",					new ColumnMetadata());
			put("measurement_european_group_code",	new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/europeanGroupCode",level("http://qweb.cs.aau.dk/airbase/schema/sensor",true),new Integer2Integer()));
			put("measurement_unit",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/unit",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false),new String2String()));
			put("measurement_group_start_date",		new ColumnMetadata());
			put("measurement_group_end_date",		new ColumnMetadata());
			put("measurement_latest_AIRBASE",		new ColumnMetadata());
			put("measurement_european_code",		new ColumnMetadata());
			put("measurement_start_date",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/startDate",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new Date2Date()));
			put("measurement_end_date",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/endDate",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new Date2Date()));
			put("measurement_automatic",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/automaticMeasurement",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new YesNo2TrueFalse()));
			put("measurement_technique_principle",	new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/measurementTechnique",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new String2String()));
			put("measurement_equipment",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/equipment",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new String2String()));
			put("integration_time_frequency",		new ColumnMetadata());
			put("calibration_unit",					new ColumnMetadata());
			put("height_sampling_point",			new ColumnMetadata());
			put("length_sampling_line",				new ColumnMetadata());
			put("location_sampling_point",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/samplingPoint",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new String2String()));
			put("sampling_time",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/samplingTime",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new FrequenceAndUnit2Seconds()));
			put("sampling_time_unit",				new ColumnMetadata());
			put("calibration_frequency",			new ColumnMetadata());
			put("integration_time_unit",			new ColumnMetadata());
			put("calibration_method",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/calibrationMethod",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new String2String()));
			put("calibration_description",			new ColumnMetadata());
			put("station_surrogate_code",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/europeanCode",level("http://qweb.cs.aau.dk/airbase/schema/station",true), new String2String()));			
			put("station_european_code",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/europeanCode",level("http://qweb.cs.aau.dk/airbase/schema/sensor",true), new String2String()));
			put("station_local_code",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localCode",level("http://qweb.cs.aau.dk/airbase/schema/station",true), new String2String()));
			put("country_iso_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/isoCode",level("http://qweb.cs.aau.dk/airbase/schema/country",false), new String2String()));
			put("country_name",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/country",level("http://qweb.cs.aau.dk/airbase/schema/country",true), new String2String()));
			put("station_name",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/station",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("station_start_date",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/establishedDate",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Date2Date()));
			put("station_end_date",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/shutDownDate",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Date2Date()));
			put("type_of_station",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/type",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("station_ozone_classification",		new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/ozoneClassification",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("station_type_of_area",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/areaType",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("station_subcat_rural_back",		new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/ruralSubType",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("street_type",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/streetType",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("station_longitude_deg",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/longitudeDegree",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Decimal2Decimal()));
			put("station_latitude_deg",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/latitudeDegree",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Decimal2Decimal()));
			put("station_altitude",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/altitude",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Integer2Integer()));
			put("station_city",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/city",level("http://qweb.cs.aau.dk/airbase/schema/city",true), new String2String()));
			put("lau_level1_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel1Code",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Integer2Integer()));
			put("lau_level2_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel2Code",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new Integer2Integer()));
			put("lau_level2_name",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel2Name",level("http://qweb.cs.aau.dk/airbase/schema/station",false), new String2String()));
			put("EMEP_station",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/isEuropeanMonitoringEvaluationProgramme",level("http://qweb.cs.aau.dk/airbase/schema/station",false),new YesNo2TrueFalse()));
			put("statistics_period",				new ColumnMetadata());
			put("statistics_year",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/yearNum",level("http://qweb.cs.aau.dk/airbase/schema/year",true), new Integer2Integer()));
			put("statistics_average_group",			new ColumnMetadata());
			put("statistic_shortname",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/statisticShortName",level("http://qweb.cs.aau.dk/airbase/schema/sensor",true), new String2String()));
			put("statistic_name",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/statisticName",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false), new String2String()));
			put("statistic_value",					new ColumnMetadata("measure",level("http://qweb.cs.aau.dk/airbase/schema/value",true), new Decimal2Decimal()));
			put("statistics_percentage_valid",		new ColumnMetadata());
			put("statistics_number_valid",			new ColumnMetadata());
			put("statistics_calculated",			new ColumnMetadata());
		}
		
		private Map<String, Boolean> level(String string, boolean b) {
			HashMap<String, Boolean> map = new HashMap<String,Boolean>();
			map.put(string,b);
			return map;
		}
		
		private Map<String, Boolean> level(String string1, boolean b1, String string2, boolean b2) {
			HashMap<String, Boolean> map = new HashMap<String,Boolean>();
			map.put(string1,b1);
			map.put(string2,b2);
			return map;
		}
	};
	private static List<String> allowedComponents = new ArrayList<String>() {
		private static final long serialVersionUID = -8808761432851313811L;
		{
			add("SO2");
			add("SPM");
			add("PM10");
			add("BS");
			add("O3");
			add("NO2");
			add("NOX");
			add("CO");
			add("Pb");
			add("Hg");
			add("Cd");
			add("Ni");
			add("As");
			add("C6H6");
			add("PM2.5");
		}
	};
	
	public static String getPredicate(String predicateString) {
		return Airbase2QB4OLAP.columns.get(predicateString).getName();
	}
	
	public static boolean isPredicatePartOfCube(String predicateString) {
		return Airbase2QB4OLAP.columns.get(predicateString).isPartOfCube();
	}

	public static List<String> getLevels(String predicate) {
		List<String> levels = new ArrayList<String>();
		
		ColumnMetadata test = columns.get(predicate);
		Map<String, Boolean> test1 = test.getLevels();
		test1.keySet();
		
		Set<String> thing = columns.get(predicate).getLevels().keySet();
		for (String level : thing) {
			levels.add(level);
		}
		
		return levels;
		
	}

	public static List<String> getAttributesUsedInIRI(String level) {
		List<String> keys = new ArrayList<String>();
		for(Entry<String,ColumnMetadata> entry : columns.entrySet()){
			Set<String> levels = entry.getValue().getLevels().keySet();
			if (levels.contains(level)) {
				if (entry.getValue().getLevels().get(level)) {
					keys.add(entry.getKey());
				}
			}
	    }
		return keys;
	}
	
	public static String getColumnName(String predicate){
		for (Entry<String, ColumnMetadata> entry : columns.entrySet()) {
			if (entry.getValue().getName().equals(predicate)) {
				return entry.getKey();
			}
		}
		return "";
	}
	
	public static CallBack getCallBackFunctionPredicate(String predicate) {
		for (Entry<String, ColumnMetadata> entry : columns.entrySet()) {
			if (entry.getValue().getName().equals(predicate)) {
				return entry.getValue().getCallBackFunction();
			}
		}
		return null;
	}
	
	public static CallBack getCallbackFunctionRawPredicate(String predicate) {
		return getCallBackFunctionPredicate(getPredicate(predicate));
	}
	
	public static List<String> getAllowedComponents() {
		return allowedComponents; 
	}

	public static List<String> getFiles(String predicateString) {
		return Arrays.asList("stations", "statistics", "measurement_configurations");
	}
	
	public static String removePrefix(String level) {
		String[] split =level.split("/");
		return split[split.length-1];
	}
	
	public static String removeIllegalChars(String value) {
		String result = value.replaceAll(",", "");
		result = result.replaceAll("\\(", "");
		result = result.replaceAll("\\)", "");
		result = result.replaceAll(" ", "_");
		result = result.replaceAll(">", "gt");
		result = result.replaceAll("<", "lt");
		return result;
	}


	public static String getSuffixUsedInIRI(String level, Tuple tuple) {
		List<String> attributes = Airbase2QB4OLAP.getAttributesUsedInIRI(level);
		String suffix = "";
		for (String index : attributes) {
			suffix += tuple.getValue(index)+"_";
		}
		suffix = replacelastUnderscoreWithSlash(suffix);
		if (suffix.equals("/"))
			return null;
		
		suffix = removeIllegalChars(suffix);
		return suffix;
	}
	
	private static String replacelastUnderscoreWithSlash(String str) {
	    if (str != null && str.length() > 0 && str.charAt(str.length()-1)=='_') {
	      str = str.substring(0, str.length()-1);
	      str += "/";
	    }
	    return str;
	}
}
