package dk.aau.cs.qweb.airbase.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import dk.aau.cs.qweb.airbase.types.Tuple;

public class AirBase8Configurations {
	
	private static List<String> header = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;

		{
			add("station_european_code");
			add("component_code");
			add("component_caption");
			add("component_name");
			add("component_FWD");
			add("measurement_european_group_code");
			add("measurement_unit");
			add("measurement_group_start_date");
			add("measurement_group_end_date");
			add("measurement_latest_AIRBASE");
			add("measurement_european_code");
			add("measurement_start_date");
			add("measurement_end_date");
			add("measurement_automatic");
			add("measurement_technique_principle");
			add("measurement_equipment");
			add("integration_time_frequency");
			add("calibration_unit");
			add("height_sampling_point");
			add("length_sampling_line");
			add("location_sampling_point");
			add("sampling_time");
			add("sampling_time_unit");
			add("calibration_frequency");
			add("integration_time_unit");
			add("calibration_method");
			add("calibration_description");
		}
	};
	private File file;
	private LineIterator it;

	public AirBase8Configurations(String file) throws IOException {
		this.file = new File(file);
		 it = FileUtils.lineIterator(this.file, "UTF-8");
	}


	public static List<String> getHeader() {
		return header;
	}

	public boolean hasNext() {
		return it.hasNext();
	}

	public Tuple next() {
		String line = it.next();
		List<String> data = Arrays.asList(line.split("\\s*\t\\s*"));
		return new Tuple(data,header);
	}
}
