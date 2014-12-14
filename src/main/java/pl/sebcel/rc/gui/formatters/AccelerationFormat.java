package pl.sebcel.rc.gui.formatters;

import java.text.DecimalFormat;

public class AccelerationFormat {

    private DecimalFormat f0 = new DecimalFormat("##0");
    private DecimalFormat f1 = new DecimalFormat("##0.0");
    private DecimalFormat f2 = new DecimalFormat("##0.00");

    public String format(double value, boolean vertical) {
	String result = getRaw(value);
	result += getG(value, vertical);
	return result;
    }

    public String formatSimple(double value) {
	return getRaw(value);
    }

    private String getG(double value, boolean vertical) {
	double g = 0;
	if (vertical) {
	    g = 9.81;
	}
	if (Math.abs(value) < 10) {
	    return " (" + f1.format((value + g) / 9.81) + " g)";
	} else {
	    return " (" + f0.format((value + g) / 9.81) + " g)";
	}
    }

    private String getRaw(double value) {
	if (Math.abs(value) < 10) {
	    return f2.format(value) + " m/s\u00b2";
	}
	if (Math.abs(value) < 100) {
	    return f1.format(value) + " m/s\u00b2";
	}
	if (Math.abs(value) < 1000) {
	    return f0.format(value) + " m/s\u00b2";
	}
	if (Math.abs(value) < 10000) {
	    return f2.format(value / 1000) + " km/s\u00b2";
	}
	if (Math.abs(value) < 100000) {
	    return f1.format(value / 1000) + " km/s\u00b2";
	}
	return f0.format(value / 1000) + " km/s\u00b2";
    }
}
