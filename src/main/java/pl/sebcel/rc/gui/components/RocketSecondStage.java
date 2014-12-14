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

public class RocketSecondStage extends AbstractPhysicalObject implements GraphicalObject {

    private double fuel;
    public int nozzleLenght = 0;
    private boolean active = false;
    private boolean attached = true;
    private double throttle = 0;
    private Shape shape = Shape.createRectangle(20, 40);

    public RocketSecondStage() {
	reset();
    }

    @Override
    public void paint(ScalableGraphics g) {
	double x = getX();
	double y = getY();
	double alpha = getAlpha();

	g.setColor(new Color(80, 80, 80));
	ScalablePolygon p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 8, y);
	p.addPoint(x - 8, y + 40);
	p.addPoint(x + 9, y + 40);
	p.addPoint(x + 9, y);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 8, y + 40);
	p.addPoint(x - 6, y + 45);
	p.addPoint(x + 7, y + 45);
	p.addPoint(x + 9, y + 40);
	g.setColor(new Color(150, 150, 150));
	g.fillPolygon(p);

	g.setColor(Color.black);
	if (nozzleLenght > 0) {
	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x - 7, y - nozzleLenght);
	    p.addPoint(x + 7, y - nozzleLenght);
	    p.addPoint(x + 7, y);
	    p.addPoint(x - 7, y);
	    g.fillPolygon(p);
	}
    }

    public void ignite() {
	active = true;
    }

    public void detach() {
	attached = false;
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
	if (active) {
	    if (nozzleLenght < 5) {
		nozzleLenght++;
	    }
	}

	fuel -= 5 * throttle * Constants.J2_ENGINE_BURN_SPEED * deltaTime;
	if (fuel < 0) {
	    fuel = 0;
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
	return 36000 + fuel;
    }

    @Override
    public double getMomentOfInertia() {
	return 25218000 + fuel * 50;
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
	nozzleLenght = 0;
	active = false;
	attached = true;
    }

    public double getThrustForce(double secondStageMainThrust, PhysicalConditions physicalConditions) {
	if (active && fuel > 0) {
	    return 5 * getForce(physicalConditions.getAirDensity(getY())) * secondStageMainThrust;
	} else {
	    return 0;
	}
    }

    public double getForce(double airPressure) {
	return Constants.J2_ENGINE_THRUST_IN_VACUUM - airPressure * (Constants.J2_ENGINE_THRUST_IN_VACUUM - Constants.J2_ENGINE_THRUST_AT_SEA_LEVEL) / Constants.AIR_PRESSURE_AT_SEA_LEVEL;
    }

    public void setThrottle(double throttle) {
	this.throttle = throttle;
    }

    public double getHorizontalThrustForce(double horizontalThrustSettings, PhysicalConditions physicalConditions) {
	return 0.5 * getForce(physicalConditions.getAirDensity(getY())) * horizontalThrustSettings;
    }

    @Override
    public Shape getShape() {
	return shape;
    }

}