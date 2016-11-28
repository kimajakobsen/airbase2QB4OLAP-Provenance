package dk.aau.cs.qweb.airbase.callback;

import dk.aau.cs.qweb.airbase.types.Object;
import dk.aau.cs.qweb.airbase.types.Tuple;
import dk.aau.cs.qweb.airbase.vocabulary.XSD;

public class FrequenceAndUnit2Seconds implements CallBack {

	@Override
	public Object callBackMethod(String string, Tuple tuple) {
		String timeUnit = tuple.getValue("sampling_time_unit");
		int value = Integer.valueOf(string);
		if (timeUnit.equals("natural day")) {
			return new Object(value*24*60*60,XSD.integerType);
		} else if (timeUnit.equals("minute")) {
			return new Object(value*60,XSD.integerType);
		} else if (timeUnit.equals("hour")) {
			return new Object(value*60*60,XSD.integerType);
		} else if (timeUnit.equals("second")) {
			return new Object(string,XSD.integerType);
		} else {
			throw new IllegalArgumentException("unknown time unit: "+timeUnit);
		}
	}
}
