package pl.sebcel.rc.gui.graphics;

import java.util.ArrayList;
import java.util.List;

public class ScalablePolygon {

    private List<double[]> points = new ArrayList<double[]>();
    private double refX;
    private double refY;
    private double alpha;

    public ScalablePolygon(double refX, double refY, double alpha) {
	this.refX = refX;
	this.refY = refY;
	this.alpha = alpha;
    }

    public void addPoint(double x, double y) {
	double[] point = new double[2];
	point[0] = refX + (x - refX) * Math.cos(alpha) - (y - refY) * Math.sin(alpha);
	point[1] = refY + (x - refX) * Math.sin(alpha) + (y - refY) * Math.cos(alpha);

	points.add(point);
    }

    public List<double[]> getPoints() {
	return points;
    }

    public void setReference(double x, double y, double alpha) {
	this.refX = x;
	this.refY = y;
	this.alpha = alpha;
    }

    public void addRectangle(double xMin, double yMin, double xMax, double yMax) {
	addPoint(xMin, yMin);
	addPoint(xMin, yMax);
	addPoint(xMax, yMax);
	addPoint(xMin, yMax);
    }
}