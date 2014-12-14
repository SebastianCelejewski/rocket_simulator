package pl.sebcel.rc.gui.formatters;

import java.text.DecimalFormat;

public class AngleFormat {

    private DecimalFormat f0 = new DecimalFormat("##0");

    public String format(double value) {
	int degrees = (int) (value * 180 / Math.PI);
	return f0.format(degrees);
    }
}
