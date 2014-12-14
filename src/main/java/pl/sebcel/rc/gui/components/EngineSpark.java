package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;

public class EngineSpark extends AbstractPhysicalObject implements GraphicalObject {

    private double temperature;
    private int r = 255;
    private int g = 255;
    private int b = 255;

    public void initialize(double x, double y, double vx, double vy) {
	super.initialize(x, y, vx, vy, 0, 0);
	this.temperature = 1.0;
    }

    @Override
    public void paint(ScalableGraphics gr) {
	Color c = new Color(r, g, b, (int) (temperature * 255));
	gr.setColor(c);
	gr.fillOval(getX(), getY(), 1);
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);
	this.temperature = 0.9 * this.temperature;
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
