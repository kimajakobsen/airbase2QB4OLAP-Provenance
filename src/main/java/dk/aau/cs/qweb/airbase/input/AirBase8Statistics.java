package dk.aau.cs.qweb.airbase.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class AirBase8Statistics{

	private static List<String> header = new ArrayList<String>() {
		private static final long serialVersionUID = 2L;

		{			
			add("station_european_code");
			add("component_code");
			add("component_name");
			add("component_caption");
			add("measurement_unit");
			add("measurement_european_group_code");
			add("statistics_period");
			add("statistics_year");
			add("statistics_average_group");
			add("statistic_shortname");
			add("statistic_name");
			add("statistic_value");
			add("statistics_percentage_valid");
			add("statistics_number_valid");
			add("statistics_calculated");
		}
	};

	public AirBase8Statistics(String file) {
		// TODO Auto-generated constructor stub
	}

	public Iterator<Tuple> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<String> getHeader() {
		return header;
	}
}
