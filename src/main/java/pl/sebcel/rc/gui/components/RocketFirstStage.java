package pl.sebcel.rc.gui.components;

import java.awt.Color;

import pl.sebcel.rc.events.RocketStateChangedEvent;
import pl.sebcel.rc.gui.dto.RocketStatus;
import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.graphics.ScalablePolygon;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.Constants;
import pl.sebcel.rc.gui.physics.KineticState;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;
import pl.sebcel.rc.infrastructure.events.EventBus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;

public class RocketFirstStage extends AbstractPhysicalObject implements GraphicalObject {

    private double fuel;
    private boolean active = false;
    private boolean attached = true;
    private double throttle = 0;

    private EventBus eventBus;
    private int parentRocketId;
    private Shape shape = Shape.createRectangle(20, 100);

    public RocketFirstStage(int parentRocketId) {
	this.parentRocketId = parentRocketId;
	eventBus = EventBusFactory.getInstance();
	reset();
    }

    public void detach() {
	attached = false;
    }

    @Override
    public void paint(ScalableGraphics g) {
	double x = getX();
	double y = getY();
	double alpha = getAlpha();

	g.setColor(new Color(100, 100, 100));
	ScalablePolygon p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 10, y);
	p.addPoint(x - 10, y + 100);
	p.addPoint(x + 11, y + 100);
	p.addPoint(x + 11, y);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 10, y + 100);
	p.addPoint(x - 8, y + 105);
	p.addPoint(x + 9, y + 105);
	p.addPoint(x + 11, y + 100);
	g.setColor(new Color(150, 150, 150));
	g.fillPolygon(p);

	g.setColor(Color.black);
	p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 7, y - 5);
	p.addPoint(x + 7, y - 5);
	p.addPoint(x + 7, y);
	p.addPoint(x - 7, y);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addRectangle(x - 12, y + 94, x - 10, y + 96);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addRectangle(x + 11, y + 94, x + 13, y + 96);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addRectangle(x - 12, y + 4, x - 10, y + 6);
	g.fillPolygon(p);

	p = new ScalablePolygon(x, y, alpha);
	p.addRectangle(x + 11, y + 4, x + 13, y + 6);
	g.fillPolygon(p);
    }

    public void setFuel(double fuel) {
	this.fuel = fuel;
    }

    public double getFuel() {
	return fuel;
    }

    @Override
    public double getMass() {
	return 135218 + fuel;
    }

    @Override
    public double getMomentOfInertia() {
	return 135218000 + fuel * 50;
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
	if (Math.abs(s) > 10) {
	    // broken = true;
	    setVx(Math.signum(getVx()) * Math.random() * 20);
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);

	if (!attached) {
	    KineticState kineticStateBeforeCollisions = calculateKineticState(0, 0, physicalConditions, deltaTime);
	    KineticState kineticStateAfterCollisions = physicalEnvironment.adjustKineticState(kineticStateBeforeCollisions, getShape());

	    setX(kineticStateAfterCollisions.getX());
	    setY(kineticStateAfterCollisions.getY());
	    setVx(kineticStateAfterCollisions.getVx());
	    setVy(kineticStateAfterCollisions.getVy());
	    setAlpha(kineticStateAfterCollisions.getAlpha());
	    setOmega(kineticStateAfterCollisions.getOmega());

	    RocketStatus rocketState = new RocketStatus();
	    rocketState.setKineticState(kineticStateAfterCollisions);
	    rocketState.setFirstStageFuel(getFuel());
	    rocketState.setSecondStageFuel(0);
	    rocketState.setThirdStageFuel(0);
	    RocketStateChangedEvent rocketStatusChangedEvent = new RocketStateChangedEvent(parentRocketId + 10, rocketState);
	    eventBus.fireEvent(rocketStatusChangedEvent);
	}

	fuel -= 5 * throttle * Constants.F1_FUEL_BURN_SPEED * deltaTime;
	if (fuel < 0) {
	    fuel = 0;
	}
    }

    public double getThrustForce(double firstStageMainThrust, PhysicalConditions physicalConditions) {
	if (active && fuel > 0) {
	    return 5 * Constants.F1_ENGINE_THRUST_FORCE * firstStageMainThrust;
	} else {
	    return 0;
	}
    }

    public void setThrottle(double throttle) {
	this.throttle = throttle;
    }

    public double getHorizontalThrustForce(double horizontalThrustSettings, PhysicalConditions physicalConditions) {
	return 0.1 * Constants.F1_ENGINE_THRUST_FORCE * horizontalThrustSettings;
    }

    @Override
    public Shape getShape() {
	return shape;
    }

    public void reset() {
	active = true;
	attached = true;
    }
}