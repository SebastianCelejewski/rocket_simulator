package pl.sebcel.rc.gui.formatters;

import java.text.DecimalFormat;

public class MassFormat {

    private DecimalFormat f0 = new DecimalFormat("##0");
    private DecimalFormat f1 = new DecimalFormat("##0.0");
    private DecimalFormat f2 = new DecimalFormat("##0.00");

    public String format(double value) {
	if (value < 1) {
	    return f2.format(value) + " kg";
	}
	if (value < 10) {
	    return f1.format(value) + " kg";
	}
	if (value < 1000) {
	    return f0.format(value) + " kg";
	}
	if (value < 10000) {
	    return f2.format(value / 1000) + " T";
	}
	if (value < 100000) {
	    return f1.format(value / 1000) + " T";
	}

	return f0.format(value / 1000) + " T";
    }
}
