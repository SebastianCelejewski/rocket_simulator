package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.Shape;

public class Tower extends AbstractPhysicalObject implements GraphicalObject {

    @Override
    public void paint(ScalableGraphics g) {
	g.setColor(Color.DARK_GRAY);

	g.fillRect(getX() - 20, Constants.EARTH_RADIUS, getX() - 10, Constants.EARTH_RADIUS + 30);
	g.fillRect(getX() + 10, Constants.EARTH_RADIUS, getX() + 20, Constants.EARTH_RADIUS + 30);
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