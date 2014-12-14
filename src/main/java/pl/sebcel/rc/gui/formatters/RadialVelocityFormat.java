package pl.sebcel.rc.gui.formatters;

import java.text.DecimalFormat;

public class RadialVelocityFormat {

    private DecimalFormat f0 = new DecimalFormat("##0");
    private DecimalFormat f1 = new DecimalFormat("##0.0");
    private DecimalFormat f2 = new DecimalFormat("##0.00");

    public String format(double value) {

	double degreesPerSecond = value * 180 / Math.PI;

	if (Math.abs(value) < 1) {
	    return f2.format(degreesPerSecond) + " deg/s";
	}
	if (Math.abs(value) < 10) {
	    return f1.format(degreesPerSecond) + " deg/s";
	}

	return f0.format(degreesPerSecond) + " deg/s";
    }
}