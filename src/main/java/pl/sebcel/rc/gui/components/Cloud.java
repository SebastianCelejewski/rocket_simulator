package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.Constants;

public class Cloud implements GraphicalObject {

    private int[] x = new int[10];
    private int[] y = new int[10];

    public Cloud() {

	int xx = (int) ((Math.tan(Math.random() * Math.PI - Math.PI / 2) * 1000) - 500);
	int yy = (int) (10 * Math.pow(10, 1 / Math.random()) + Constants.EARTH_RADIUS + 500);
	if (yy > Constants.EARTH_RADIUS + 100000) {
	    yy = (int) Constants.EARTH_RADIUS + 100000;
	}

	for (int i = 0; i < 10; i++) {
	    x[i] = xx + (int) (Math.random() * 40);
	    y[i] = yy + (int) (Math.random() * 10);
	}
    }

    @Override
    public void paint(ScalableGraphics g) {
	g.setColor(new Color(255, 255, 255, 100));
	for (int i = 0; i < 10; i++) {
	    g.fillOval(x[i], y[i], 50);
	}
    }
}