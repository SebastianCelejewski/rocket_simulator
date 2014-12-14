package pl.sebcel.rc.gui.physics;

public class Edge {
    private double length;
    private double angle;

    public Edge(double length, double angle) {
	this.length = length;
	this.angle = angle;
    }

    public double getLength() {
	return length;
    }

    public double getAngle() {
	return angle;
    }
}