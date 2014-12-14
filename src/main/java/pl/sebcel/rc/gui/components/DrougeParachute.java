package pl.sebcel.rc.gui.components;

import static pl.sebcel.rc.gui.components.ParachuteState.DEPLOYED;
import static pl.sebcel.rc.gui.components.ParachuteState.READY;
import static pl.sebcel.rc.gui.components.ParachuteState.RELEASED;

import java.awt.Color;

import pl.sebcel.rc.gui.graphics.GraphicalObject;
import pl.sebcel.rc.gui.graphics.ScalableGraphics;
import pl.sebcel.rc.gui.graphics.ScalablePolygon;
import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.PhysicalEnvironment;
import pl.sebcel.rc.gui.physics.Shape;

public class DrougeParachute extends AbstractPhysicalObject implements GraphicalObject {

    private int deploymentStatus = 0;
    private int weaknessStatus = 0;

    private double lastVx;
    private double lastVy;
    private Shape shape = Shape.createTriangle(40, 2);

    private ParachuteState state = READY;

    public void deploy() {
	if (state == READY) {
	    state = DEPLOYED;
	}
    }

    public void release() {
	if (state == DEPLOYED) {
	    state = RELEASED;
	}
    }

    public ParachuteState getState() {
	return state;
    }

    @Override
    public void paint(ScalableGraphics g) {

	double x = getX();
	double y = getY();

	g.setColor(new Color(120, 120, 120));

	double alpha = Math.PI / 2 + Math.atan2(getVy(), getVx());

	ScalablePolygon p = new ScalablePolygon(x, y, alpha);
	p.addPoint(x, y);
	p.addPoint(x, y + deploymentStatus * 5);
	p.addPoint(x - deploymentStatus, y + deploymentStatus - weaknessStatus + deploymentStatus * 5);
	p.addPoint(x, y - weaknessStatus * 1.2 + deploymentStatus * 1.2 + deploymentStatus * 5);
	p.addPoint(x + deploymentStatus, y + deploymentStatus - weaknessStatus + deploymentStatus * 5);
	p.addPoint(x, y + deploymentStatus * 5);
	g.drawPolygon(p);
    }

    @Override
    public void tick(PhysicalConditions physicalConditions, PhysicalEnvironment physicalEnvironment, double deltaTime) {
	super.tick(physicalConditions, physicalEnvironment, deltaTime);
	if (state == DEPLOYED) {
	    if (deploymentStatus < 20) {
		deploymentStatus++;
	    }
	}
	if (state == RELEASED) {
	    if (weaknessStatus < 20) {
		weaknessStatus++;
	    }
	}

	double deltaVx = Math.abs(lastVx - getVx());
	double deltaVy = Math.abs(lastVy - getVy());

	if (isDeployed()) {
	    if (deltaVx > 20 || deltaVy > 20) {
		// release();
	    }
	}

	lastVx = getVx();
	lastVy = getVy();
    }

    @Override
    public double getMass() {
	return 100;
    }

    @Override
    public double getMomentOfInertia() {
	return Double.POSITIVE_INFINITY;
    }

    public boolean isDeployed() {
	return this.state == DEPLOYED;
    }

    public boolean isReleased() {
	return state == RELEASED;
    }

    public void reset() {
	state = READY;
	deploymentStatus = 0;
	weaknessStatus = 0;
    }

    @Override
    public Shape getShape() {
	return shape;
    }
}