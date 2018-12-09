package pl.sebcel.rc.gui.physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Shape {

    private List<Point> points = new ArrayList<Point>();
    private List<Edge> edges = new ArrayList<Edge>();
    
    public static Shape EMPTY = new Shape();

    public void addPoint(Point point) {
	points.add(point);
	calculateEdges();
    }

    private void calculateEdges() {
	edges.clear();

	if (points.size() < 2) {
	    return;
	}

	for (int i = 0; i < points.size(); i++) {
	    int j = i + 1;
	    if (j >= points.size()) {
		j = 0;
	    }
	    Point p1 = points.get(i);
	    Point p2 = points.get(j);

	    double dx = p2.getX() - p1.getX();
	    double dy = p2.getY() - p1.getY();

	    double length = Math.sqrt(dx * dx + dy * dy);
	    double angle = Math.atan2(dy, dx);
	    edges.add(new Edge(length, angle));
	}
    }

    public static Shape createTriangle(double width, double height) {
	Shape shape = new Shape();
	shape.addPoint(new Point(-width / 2, -height / 2));
	shape.addPoint(new Point(0, height / 2));
	shape.addPoint(new Point(width / 2, -height / 2));
	return shape;
    }

    public static Shape createRectangle(int width, int height) {
	Shape shape = new Shape();
	shape.addPoint(new Point(-width / 2, -height / 2));
	shape.addPoint(new Point(-width / 2, height / 2));
	shape.addPoint(new Point(width / 2, height / 2));
	shape.addPoint(new Point(width / 2, -height / 2));
	return shape;
    }

    public List<Point> getPoints() {
	return points;
    }

    public List<Edge> getEdges() {
	return edges;
    }

    public Shape append(Shape shape) {
	double thisMaxY = findMaxY(this);
	double thatMinY = findMinY(shape);
	double delta = thisMaxY - thatMinY;

	for (Point p : shape.getPoints()) {
	    points.add(new Point(p.getX(), p.getY() + delta));
	}

	Collections.sort(points, new Comparator<Point>() {

	    @Override
	    public int compare(Point o1, Point o2) {
		double angle1 = Math.atan2(o1.getY(), o1.getX());
		double angle2 = Math.atan2(o2.getY(), o2.getX());
		if (angle1 < angle2) {
		    return 1;
		}
		if (angle1 > angle2) {
		    return -1;
		}
		return 0;
	    }
	});
	calculateEdges();
	return this;
    }

    private double findMaxY(Shape shape) {
	List<Point> points = shape.getPoints();
	if (points.size() == 0) {
	    return 0;
	}
	double maxY = points.get(0).getY();

	if (points.size() > 0) {
	    for (int i = 1; i < points.size(); i++) {
		if (points.get(i).getY() > maxY) {
		    maxY = points.get(i).getY();
		}
	    }
	}
	return maxY;
    }

    private double findMinY(Shape shape) {
	List<Point> points = shape.getPoints();
	if (points.size() == 0) {
	    return 0;
	}
	double minY = points.get(0).getY();

	if (points.size() > 0) {
	    for (int i = 1; i < points.size(); i++) {
		if (points.get(i).getY() < minY) {
		    minY = points.get(i).getY();
		}
	    }
	}
	return minY;
    }

}