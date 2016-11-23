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
			put("component_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/code", level("http://qweb.cs.aau.dk/airbase/schema/component",true,"http://qweb.cs.aau.dk/airbase/schema/sensor",true)));
			put("component_caption",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/caption",level("http://qweb.cs.aau.dk/airbase/schema/component",false)));
			put("component_name",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/name",level("http://qweb.cs.aau.dk/airbase/schema/component",false)));
			put("component_FWD",					new ColumnMetadata());
			put("measurement_european_group_code",	new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/europeanGroupCode",level("http://qweb.cs.aau.dk/airbase/schema/sensor",true)));
			put("measurement_unit",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/unit",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("measurement_group_start_date",		new ColumnMetadata());
			put("measurement_group_end_date",		new ColumnMetadata());
			put("measurement_latest_AIRBASE",		new ColumnMetadata());
			put("measurement_european_code",		new ColumnMetadata());
			put("measurement_start_date",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/startDate",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("measurement_end_date",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/endDate",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("measurement_automatic",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/automaticMeasurement",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("measurement_technique_principle",	new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/measurementTechnique",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("measurement_equipment",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/equipment",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("integration_time_frequency",		new ColumnMetadata());
			put("calibration_unit",					new ColumnMetadata());
			put("height_sampling_point",			new ColumnMetadata());
			put("length_sampling_line",				new ColumnMetadata());
			put("location_sampling_point",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/samplingPoint",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("sampling_time",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/samplingTime",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("sampling_time_unit",				new ColumnMetadata());
			put("calibration_frequency",			new ColumnMetadata());
			put("integration_time_unit",			new ColumnMetadata());
			put("calibration_method",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/calibrationMethod",level("http://qweb.cs.aau.dk/airbase/schema/sensor",false)));
			put("calibration_description",			new ColumnMetadata());
			put("station_european_code",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/europeanCode",level("http://qweb.cs.aau.dk/airbase/schema/station",true,"http://qweb.cs.aau.dk/airbase/schema/sensor",true)));
			put("station_local_code",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localCode",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("country_iso_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/isoCode",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("country_name",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/country",level("http://qweb.cs.aau.dk/airbase/schema/country",true)));
			put("station_name",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/station",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_start_date",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/establishedDate",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_end_date",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/shutDownDate",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("type_of_station",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/type",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_ozone_classification",		new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/ozoneClassification",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_type_of_area",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/areaType",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_subcat_rural_back",		new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/ruralSubType",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("street_type",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/streetType",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_longitude_deg",			new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/longitudeDegree",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_latitude_deg",				new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/latitudeDegree",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_altitude",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/altitude",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("station_city",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/city",level("http://qweb.cs.aau.dk/airbase/schema/city",true)));
			put("lau_level1_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel1Code",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("lau_level2_code",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel2Code",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("lau_level2_name",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/localAdministrativeUnitLevel2Name",level("http://qweb.cs.aau.dk/airbase/schema/station",false)));
			put("EMEP_station",						new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/isEuropeanMonitoringEvaluationProgramme",level("http://qweb.cs.aau.dk/airbase/schema/station",false),new YesNo2TrueFalse()));
			put("statistics_period",				new ColumnMetadata());
			put("statistics_year",					new ColumnMetadata("http://qweb.cs.aau.dk/airbase/property/yearNum",level("http://qweb.cs.aau.dk/airbase/schema/year",true)));
			put("statistics_average_group",			new ColumnMetadata());
			put("statistic_shortname",				new ColumnMetadata());
			put("statistic_name",					new ColumnMetadata());
			put("statistic_value",					new ColumnMetadata("http://purl.org/linked-data/cube#Observation",level("http://qweb.cs.aau.dk/airbase/schema/value",true)));
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
