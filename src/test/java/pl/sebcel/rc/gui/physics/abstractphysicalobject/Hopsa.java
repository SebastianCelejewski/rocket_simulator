package pl.sebcel.rc.gui.physics.abstractphysicalobject;

import org.junit.Test;

import pl.sebcel.rc.gui.physics.KineticState;
import pl.sebcel.rc.gui.physics.PhysicalConditions;

public class Hopsa {

    private PhysicalConditions physicalConditions = new PhysicalConditions();

    @Test
    public void sasa() {
	TestPhysicalObject cut = new TestPhysicalObject();
	cut.setX(0);
	cut.setY(10);
	cut.setVx(100);
	cut.setVy(100);
	cut.setAlpha(0);
	cut.setOmega(0);
	cut.setMass(1);
	cut.setMomentOfInertia(1);

	double mainThrustForce = 1;
	double horizontalThrustForce = 0;
	double deltaTime = 0.1;
	KineticState kineticState = cut.calculateKineticState(mainThrustForce, horizontalThrustForce, physicalConditions, deltaTime);

	System.out.println(kineticState.getX());
	System.out.println(kineticState.getY());
	System.out.println(kineticState.getVx());
	System.out.println(kineticState.getVy());
	System.out.println(kineticState.getAlpha());
	System.out.println(kineticState.getOmega());
	System.out.println(kineticState.getDynamicPressureX());
	System.out.println(kineticState.getDynamicPressureY());
    }
}