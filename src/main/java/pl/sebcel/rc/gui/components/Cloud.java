package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;

public class Cloud implements GraphicalObject {

    private int[] x = new int[10];
    private int[] y = new int[10];

    public Cloud() {

	int xx = (int) ((Math.random() * 600) - 300);
	int yy = (int) (5000 * Math.random()) + 300;

	for (int i = 0; i < 10; i++) {
	    x[i] = xx + (int) (Math.random() * 40);
	    y[i] = yy + (int) (Math.random() * 10);
	}
    }

    @Override
    public void paint(ScalableGraphics g) {
	g.setColor(new Color(255, 255, 255, 100));
	for (int i = 0; i < 10; i++) {
//	    g.fillOval(x[i], y[i], 50);
	}
    }
}