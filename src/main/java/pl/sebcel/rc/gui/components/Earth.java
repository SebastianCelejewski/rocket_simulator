package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.Constants;

public class Earth implements GraphicalObject {

    @Override
    public void paint(ScalableGraphics g) {
	g.setColor(Color.GREEN);

	double x = 0;
	double y = 0;
	double R = Constants.EARTH_RADIUS;

	for (int a = 255; a >= 0; a -= 25) {
	    Color c = new Color(0, 0, 255 - a);
	    g.setColor(c);
	    g.fillOval(x, y, (int) (R + ((double) a * 1000 / 2)));
	}

	g.setColor(Color.GREEN);
	g.fillOval(x, y, (int) R);

    }
}