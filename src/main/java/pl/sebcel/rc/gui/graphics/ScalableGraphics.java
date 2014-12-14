package pl.sebcel.rc.gui.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class ScalableGraphics {

    private Graphics g;
    private IScaler s;

    public ScalableGraphics(Graphics g, IScaler s) {
	this.g = g;
	this.s = s;
    }

    public void fillRect(double xMin, double yMin, double xMax, double yMax) {
	g.fillRect(s.x(xMin), s.y(yMax), s.x(xMax) - s.x(xMin), -s.y(yMax) + s.y(yMin));
    }

    public void setColor(Color c) {
	g.setColor(c);
    }

    public void drawOval(double x, double y, double i, double j) {
	int width = s.width(i);
	int height = Math.abs(s.height(j));
	g.drawOval(s.x(x) - width / 2, s.y(y) - height / 2, width, height);
    }

    public void drawRect(double xMin, double yMin, double xMax, double yMax) {
	g.drawRect(s.x(xMin), s.y(yMax), s.x(xMax) - s.x(xMin), -s.y(yMax) + s.y(yMin));
    }

    public void drawLine(double xMin, double yMin, double xMax, double yMax) {
	g.drawLine(s.x(xMin), s.y(yMin), s.x(xMax), s.y(yMax));
    }

    public void fillOval(double x, double y, int i) {
	int tx = s.x(x - i);
	int ty = s.y(y + i);
	int rx = s.width(2 * i);
	int ry = s.width(2 * i);

	if (rx < 2) {
	    rx = 2;
	}
	if (ry < 2) {
	    ry = 2;
	}

	g.fillOval(tx, ty, rx, ry);
    }

    public void fillPolygon(ScalablePolygon p) {
	Polygon polygon = new Polygon();
	for (double[] point : p.getPoints()) {
	    polygon.addPoint(s.x(point[0]), s.y(point[1]));
	}
	g.fillPolygon(polygon);
    }

    public void drawPolygon(ScalablePolygon p) {
	Polygon polygon = new Polygon();
	for (double[] point : p.getPoints()) {
	    polygon.addPoint(s.x(point[0]), s.y(point[1]));
	}
	g.drawPolygon(polygon);
    }

    public Graphics getRawGraphics() {
	return g;
    }

}