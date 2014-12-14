package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.graphics.ScalablePolygon;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.KineticState;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;

public class SpaceshipLander extends AbstractPhysicalObject implements GraphicalObject {

    private double fuel;
    private boolean attached = true;
    private Shape shape = Shape.createTriangle(12, 20);

    public void ignite() {
    }

    public void detach() {
	attached = false;
    }

    @Override
    public void paint(ScalableGraphics g) {
	double x = getX();
	double y = getY();
	double alpha = getAlpha();

	g.setColor(new Color(120, 120, 120));

	// Lander
	ScalablePolygon p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 6, y);
	p.addPoint(x, y + 20);
	p.addPoint(x + 6, y);
	g.fillPolygon(p);
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);

	if (isJustCreated()) {
	    return;
	}
	if (!attached) {
	    KineticState kineticStateBeforeCollisions = calculateKineticState(0, 0, physicalConditions, deltaTime);
	    KineticState kineticStateAfterCollisions = physicalEnvironment.adjustKineticState(kineticStateBeforeCollisions, getShape());

	    setX(kineticStateAfterCollisions.getX());
	    setY(kineticStateAfterCollisions.getY());
	    setVx(kineticStateAfterCollisions.getVx());
	    setVy(kineticStateAfterCollisions.getVy());
	    setAlpha(kineticStateAfterCollisions.getAlpha());
	    setOmega(kineticStateAfterCollisions.getOmega());
	}
    }

    public void setFuel(double fuel) {
	this.fuel = fuel;
    }

    public double getFuel() {
	return fuel;
    }

    @Override
    public double getMass() {
	return 521 + fuel;
    }

    @Override
    public double getMomentOfInertia() {
	return 52180 + fuel * 50;
    }

    public void burnFuel(double fuelBurned) {
	fuel -= fuelBurned;
	if (fuel < 0) {
	    fuel = 0;
	}
    }

    public double getVerticalAirResistanceRatio() {
	if (getVy() > 0) {
	    return 0.5;
	} else {
	    return 0.1;
	}
    }

    public double getHorizontalAirResistanceRatio() {
	return 0.5;
    }

    @Override
    public boolean shock(double s) {
	if (s > 10) {
	    setVx(Math.signum(getVx()) * Math.random() * 20);
	    return true;
	} else {
	    return false;
	}
    }

    public void reset() {
	attached = true;
    }

    public double getThrustForce(double thirdStageMainThrust) {
	return 0;
    }

    public void setThrottle(double throttle) {
    }

    public double getHorizontalThrustForce(double horizontalThrustSettings, PhysicalConditions physicalConditions) {
	return 0.001 * Constants.J2_ENGINE_THRUST_IN_VACUUM * horizontalThrustSettings;
    }

    @Override
    public Shape getShape() {
	return shape;
    }
}