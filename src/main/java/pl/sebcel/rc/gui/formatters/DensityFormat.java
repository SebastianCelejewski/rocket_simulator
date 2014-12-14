package pl.sebcel.rc.gui.formatters;

import java.text.DecimalFormat;

public class DensityFormat {

    private DecimalFormat f0 = new DecimalFormat("##0");
    private DecimalFormat f1 = new DecimalFormat("##0.0");
    private DecimalFormat f2 = new DecimalFormat("##0.00");

    public String format(double value) {
	if (Math.abs(value) < 1) {
	    return f2.format(value) + " kg/m\u00b3";
	}
	if (Math.abs(value) < 10) {
	    return f1.format(value) + " kg/m\u00b3";
	}
	if (Math.abs(value) < 1000) {
	    return f0.format(value) + " kg/m\u00b3";
	}

	return f0.format(value / 1000) + " kg/m\u00b3";
    }
}
