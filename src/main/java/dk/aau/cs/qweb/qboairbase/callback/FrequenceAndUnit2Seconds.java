package dk.aau.cs.qweb.qboairbase.callback;

import dk.aau.cs.qweb.qboairbase.types.Object;
import dk.aau.cs.qweb.qboairbase.types.Tuple;
import dk.aau.cs.qweb.qboairbase.vocabulary.XSD;

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
		}  else if (timeUnit.equals("")) {
			return new Object("");
		} else if (timeUnit.equals("week")) {
			return new Object(value*7*24*60*60,XSD.integerType);
		} else {
			System.out.println(tuple);
			throw new IllegalArgumentException("unknown time unit: "+timeUnit);
		}
	}
}
