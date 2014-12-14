package pl.sebcel.rc.gui.physics.abstractphysicalobject;

import pl.sebcel.rc.gui.physics.AbstractPhysicalObject;
import pl.sebcel.rc.gui.physics.KineticState;
import pl.sebcel.rc.gui.physics.PhysicalConditions;
import pl.sebcel.rc.gui.physics.Shape;

public class TestPhysicalObject extends AbstractPhysicalObject {

    private double mass;
    private double momentOfInertia;

    public void setMass(double mass) {
	this.mass = mass;
    }

    public void setMomentOfInertia(double momentOfInertia) {
	this.momentOfInertia = momentOfInertia;
    }

    @Override
    public double getMass() {
	return mass;
    }

    @Override
    protected double getMomentOfInertia() {
	return momentOfInertia;
    }

    @Override
    public KineticState calculateKineticState(double mainThrustForce, double horizontalThrustForce, PhysicalConditions physicalConditions, double deltaTime) {
	return super.calculateKineticState(mainThrustForce, horizontalThrustForce, physicalConditions, deltaTime);
    }

    @Override
    public Shape getShape() {
	// TODO Auto-generated method stub
	return null;
    }
}