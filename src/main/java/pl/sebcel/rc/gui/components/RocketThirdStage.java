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

public class RocketThirdStage extends AbstractPhysicalObject implements GraphicalObject {

    private double fuel;
    public int nozzleLenght = 0;
    public int solarCellsSpan = 0;
    private boolean active = false;
    private boolean attached = true;
    private double throttle = 0;
    private int parentRocketId;
    private EventBus eventBus;
    private Shape shape = Shape.createRectangle(12, 20);

    public RocketThirdStage(int parentRocketId) {
	eventBus = EventBusFactory.getInstance();
	this.parentRocketId = parentRocketId;
	reset();
    }

    public void ignite() {
	active = true;
    }

    public void detach() {
	attached = false;
    }

    @Override
    public void paint(ScalableGraphics g) {
	double x = getX();
	double y = getY();
	double alpha = getAlpha();

	// Body
	g.setColor(new Color(80, 80, 80));
	ScalablePolygon p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x - 6, y);
	p.addPoint(x + 6, y);
	p.addPoint(x + 6, y + 20);
	p.addPoint(x - 6, y + 20);
	g.fillPolygon(p);

	// Nozzle
	g.setColor(Color.black);
	if (nozzleLenght > 0) {
	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x - 3, y - nozzleLenght);
	    p.addPoint(x + 3, y - nozzleLenght);
	    p.addPoint(x + 3, y);
	    p.addPoint(x - 3, y);
	    g.fillPolygon(p);
	}

	// Solar cells
	g.setColor(new Color(200, 200, 200));
	if (solarCellsSpan > 0) {
	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x - 7 - solarCellsSpan / 4, y + 5);
	    p.addPoint(x - 7, y + 5);
	    p.addPoint(x - 7, y + 15);
	    p.addPoint(x - 7 - solarCellsSpan / 4, y + 15);
	    g.drawPolygon(p);

	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x + 6, y + 5);
	    p.addPoint(x + 6 + solarCellsSpan / 4, y + 5);
	    p.addPoint(x + 6 + solarCellsSpan / 4, y + 15);
	    p.addPoint(x + 6, y + 15);
	    g.drawPolygon(p);

	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x - 7 - solarCellsSpan / 4 - solarCellsSpan / 3, y + 5);
	    p.addPoint(x - 7 - solarCellsSpan / 3, y + 5);
	    p.addPoint(x - 7 - solarCellsSpan / 3, y + 15);
	    p.addPoint(x - 7 - solarCellsSpan / 4 - solarCellsSpan / 3, y + 15);
	    g.drawPolygon(p);

	    p = new ScalablePolygon(x, y, alpha);
	    p.addPoint(x + 6 + solarCellsSpan / 3, y + 5);
	    p.addPoint(x + 6 + solarCellsSpan / 4 + solarCellsSpan / 3, y + 5);
	    p.addPoint(x + 6 + solarCellsSpan / 4 + solarCellsSpan / 3, y + 15);
	    p.addPoint(x + 6 + solarCellsSpan / 3, y + 15);
	    g.drawPolygon(p);

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
	    RocketStateChangedEvent rocketStatusChangedEvent = new RocketStateChangedEvent(parentRocketId + 30, rocketState);
	    eventBus.fireEvent(rocketStatusChangedEvent);
	}

	if (active) {
	    if (nozzleLenght < 5) {
		nozzleLenght++;
	    }
	    if (solarCellsSpan < 30) {
		solarCellsSpan++;
	    }
	}

	fuel -= throttle * Constants.J2_ENGINE_BURN_SPEED * deltaTime;
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
	return 2218 + fuel;
    }

    @Override
    public double getMomentOfInertia() {
	return 521800 + fuel * 50;
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
	solarCellsSpan = 0;
	active = false;
	attached = true;
    }

    public double getThrustForce(double secondStageMainThrust, PhysicalConditions physicalConditions) {
	if (active && fuel > 0) {
	    return getForce(physicalConditions.getAirDensity(getY())) * secondStageMainThrust;
	} else {
	    return 0;
	}
    }

    public double getHorizontalThrustForce(double horizontalThrustSettings, PhysicalConditions physicalConditions) {
	return 0.1 * getForce(physicalConditions.getAirDensity(getY())) * horizontalThrustSettings;
    }

    public double getForce(double airPressure) {
	return Constants.J2_ENGINE_THRUST_IN_VACUUM - airPressure * (Constants.J2_ENGINE_THRUST_IN_VACUUM - Constants.J2_ENGINE_THRUST_AT_SEA_LEVEL) / Constants.AIR_PRESSURE_AT_SEA_LEVEL;
    }

    public void setThrottle(double throttle) {
	this.throttle = throttle;
    }

    @Override
    public Shape getShape() {
	return shape;
    }
}