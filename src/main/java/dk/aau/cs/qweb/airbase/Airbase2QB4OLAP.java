package dk.aau.cs.qweb.airbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import dk.aau.cs.qweb.airbase.callback.YesNo2TrueFalse;
import dk.aau.cs.qweb.airbase.types.ColumnMetadata;

public class Airbase2QB4OLAP {
	
	private static Map<String,ColumnMetadata> columns = new HashMap<String,ColumnMetadata>() {
		private static final long serialVersionUID = 4261847242888256230L;
		{
			put("component_code",					new ColumnMetadata("property:code", level("schema:component",true,"schema:sensor",true)));
			put("component_caption",				new ColumnMetadata("property:caption",level("schema:component",false)));
			put("component_name",					new ColumnMetadata("property:name",level("schema:component",false)));
			put("component_FWD",					new ColumnMetadata());
			put("measurement_european_group_code",	new ColumnMetadata("property:europeanGroupCode",level("schema:sensor",true)));
			put("measurement_unit",					new ColumnMetadata("property:unit",level("schema:sensor",false)));
			put("measurement_group_start_date",		new ColumnMetadata());
			put("measurement_group_end_date",		new ColumnMetadata());
			put("measurement_latest_AIRBASE",		new ColumnMetadata());
			put("measurement_european_code",		new ColumnMetadata());
			put("measurement_start_date",			new ColumnMetadata("property:startDate",level("schema:sensor",false)));
			put("measurement_end_date",				new ColumnMetadata("property:endDate",level("schema:sensor",false)));
			put("measurement_automatic",			new ColumnMetadata("property:automaticMeasurement",level("schema:sensor",false)));
			put("measurement_technique_principle",	new ColumnMetadata("property:measurementTechnique",level("schema:sensor",false)));
			put("measurement_equipment",			new ColumnMetadata("property:equipment",level("schema:sensor",false)));
			put("integration_time_frequency",		new ColumnMetadata());
			put("calibration_unit",					new ColumnMetadata());
			put("height_sampling_point",			new ColumnMetadata());
			put("length_sampling_line",				new ColumnMetadata());
			put("location_sampling_point",			new ColumnMetadata("property:samplingPoint",level("schema:sensor",false)));
			put("sampling_time",					new ColumnMetadata("property:samplingTime",level("schema:sensor",false)));
			put("sampling_time_unit",				new ColumnMetadata());
			put("calibration_frequency",			new ColumnMetadata());
			put("integration_time_unit",			new ColumnMetadata());
			put("calibration_method",				new ColumnMetadata("property:calibrationMethod",level("schema:sensor",false)));
			put("calibration_description",			new ColumnMetadata());
			put("station_european_code",			new ColumnMetadata("property:europeanCode",level("schema:station",true)));
			put("station_local_code",				new ColumnMetadata("property:localCode",level("schema:station",false)));
			put("country_iso_code",					new ColumnMetadata("property:isoCode",level("schema:station",false)));
			put("country_name",						new ColumnMetadata("property:country",level("schema:country",true)));
			put("station_name",						new ColumnMetadata("property:station",level("schema:station",false)));
			put("station_start_date",				new ColumnMetadata("property:establishedDate",level("schema:station",false)));
			put("station_end_date",					new ColumnMetadata("property:shutDownDate",level("schema:station",false)));
			put("type_of_station",					new ColumnMetadata("property:type",level("schema:station",false)));
			put("station_ozone_classification",		new ColumnMetadata("property:ozoneClassification",level("schema:station",false)));
			put("station_type_of_area",				new ColumnMetadata("property:areaType",level("schema:station",false)));
			put("station_subcat_rural_back",		new ColumnMetadata("property:ruralSubType",level("schema:station",false)));
			put("street_type",						new ColumnMetadata("property:streetType",level("schema:station",false)));
			put("station_longitude_deg",			new ColumnMetadata("property:longitudeDegree",level("schema:station",false)));
			put("station_latitude_deg",				new ColumnMetadata("property:latitudeDegree",level("schema:station",false)));
			put("station_altitude",					new ColumnMetadata("property:altitude",level("schema:station",false)));
			put("station_city",						new ColumnMetadata("property:city",level("schema:city",true)));
			put("lau_level1_code",					new ColumnMetadata("property:localAdministrativeUnitLevel1Code",level("schema:station",false)));
			put("lau_level2_code",					new ColumnMetadata("property:localAdministrativeUnitLevel2Code",level("schema:station",false)));
			put("lau_level2_name",					new ColumnMetadata("property:localAdministrativeUnitLevel2Name",level("schema:station",false)));
			put("EMEP_station",						new ColumnMetadata("property:isEuropeanMonitoringEvaluationProgramme",level("schema:station",false),new YesNo2TrueFalse()));
			put("statistics_period",				new ColumnMetadata());
			put("statistics_year",					new ColumnMetadata("property:yearNum",level("schema:year",false)));
			put("statistics_average_group",			new ColumnMetadata());
			put("statistic_shortname",				new ColumnMetadata());
			put("statistic_name",					new ColumnMetadata());
			put("statistic_value",					new ColumnMetadata("http://purl.org/linked-data/cube#Observation",level("schema:value",true)));
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
}
