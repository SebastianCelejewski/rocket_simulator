package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.graphics.ScalablePolygon;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.Shape;

public class Tower extends AbstractPhysicalObject implements GraphicalObject {

    @Override
    public void paint(ScalableGraphics g) {
	g.setColor(Color.DARK_GRAY);

	ScalablePolygon p1 = new ScalablePolygon(getX(), 0, 0);
	p1.addPoint(getX() - 20, Constants.EARTH_RADIUS);
	p1.addPoint(getX() - 10, Constants.EARTH_RADIUS);
	p1.addPoint(getX() - 10, Constants.EARTH_RADIUS + 30);
	p1.addPoint(getX() - 20, Constants.EARTH_RADIUS + 30);
	g.fillPolygon(p1);

	ScalablePolygon p2 = new ScalablePolygon(getX(), 0, 0);
	p2.addPoint(getX() + 20, Constants.EARTH_RADIUS);
	p2.addPoint(getX() + 10, Constants.EARTH_RADIUS);
	p2.addPoint(getX() + 10, Constants.EARTH_RADIUS + 30);
	p2.addPoint(getX() + 20, Constants.EARTH_RADIUS + 30);
	g.fillPolygon(p2);
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
	return Shape.EMPTY;
    }

}