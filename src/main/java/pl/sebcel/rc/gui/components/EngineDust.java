package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;

public class EngineDust extends AbstractPhysicalObject implements GraphicalObject {

    private double temperature;

    public void initialize(double x, double y, double vx, double vy) {
	setX(x);
	setY(y);
	setVx(vx);
	setVy(vy);
	this.temperature = 1.0;
    }

    public void reset() {
	initialize(0, 0, 0, 0);
	this.temperature = 0;
    }

    @Override
    public void paint(ScalableGraphics g) {
	Color c = new Color(10, 10, 10, (int) (temperature * 255));
	g.setColor(c);
	int radius = radius(temperature);
	g.fillOval(getX(), getY(), radius/2);
    }

    private int radius(double temperature) {
	return 26 - (int) (25 * Math.cos((1 - temperature) * Math.PI / 2));
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);
	this.temperature = 0.95 * this.temperature;
    }

    @Override
    public double getMass() {
	return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getMomentOfInertia() {
	return Double.POSITIVE_INFINITY;
    }

    @Override
    public Shape getShape() {
	// TODO Auto-generated method stub
	return null;
    }
}
