package pl.sebcel.rc.gui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import pl.sebcel.rc.gui.physics.Constants;

public class ScalableGraphics {

    private Graphics g;
    private IScaler s;

    public ScalableGraphics(Graphics g, IScaler s) {
	this.g = g;
	this.s = s;
    }

    public void setColor(Color c) {
	g.setColor(c);
    }

    public void drawOval(double x, double y, double i, double j) {
	Point p = s.p(x, y);
	int r = s.d(2 * i);

	if (r < 2) {
	    r = 2;
	}

	g.drawOval(p.x - r / 2, p.y - r / 2, r, r);
    }

    public void fillOval(double x, double y, int i) {
	Point p = s.p(x, y);
	int r = s.d(2 * i);

	if (r < 2) {
	    r = 2;
	}

	g.fillOval(p.x - r / 2, p.y - r / 2, r, r);
    }

    public void fillPolygon(ScalablePolygon p) {
	Polygon polygon = new Polygon();
	for (double[] point : p.getPoints()) {
	    Point pt = s.p(point[0], point[1]);
	    polygon.addPoint(pt.x, pt.y);
	}
	g.fillPolygon(polygon);
    }

    public void drawPolygon(ScalablePolygon p) {
	Polygon polygon = new Polygon();
	for (double[] point : p.getPoints()) {
	    Point pt = s.p(point[0], point[1]);
	    polygon.addPoint(pt.x, pt.y);
	}
	g.drawPolygon(polygon);
    }

    public Graphics getRawGraphics() {
	return g;
    }
}