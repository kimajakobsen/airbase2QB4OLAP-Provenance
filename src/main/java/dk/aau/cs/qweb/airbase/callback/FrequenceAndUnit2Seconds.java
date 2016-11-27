package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class FrequenceAndUnit2Seconds implements CallBack {

	@Override
	public String callBackMethod(String string, Tuple tuple) {
		String timeUnit = tuple.getValue("sampling_time_unit");
		int value = Integer.valueOf(string);
		if (timeUnit.equals("natural day")) {
			return value*24*60*60+XSD.integerType;
		} else if (timeUnit.equals("minute")) {
			return value*60+XSD.integerType;
		} else if (timeUnit.equals("hour")) {
			return value*60*60+XSD.integerType;
		} else if (timeUnit.equals("second")) {
			return string+XSD.integerType;
		} else {
			throw new IllegalArgumentException("unknown time unit: "+timeUnit);
		}
	}

}
