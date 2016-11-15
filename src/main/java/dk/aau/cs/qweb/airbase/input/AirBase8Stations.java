package dk.aau.cs.qweb.airbase.input;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class AirBase8Stations {
	
	private static List<String> header = new ArrayList<String>() {
		private static final long serialVersionUID = 3L;

		{
			add("station_european_code");
			add("station_local_code");
			add("country_iso_code");
			add("country_name");
			add("station_name");
			add("station_start_date");
			add("station_end_date");
			add("type_of_station");
			add("station_ozone_classification");
			add("station_type_of_area");
			add("station_subcat_rural_back");
			add("street_type");
			add("station_longitude_deg");
			add("station_latitude_deg");
			add("station_altitude");
			add("station_city");
			add("lau_level1_code");
			add("lau_level2_code");
			add("lau_level2_name");
			add("EMEP_station");
		}
	};

	public AirBase8Stations(String file) {
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
