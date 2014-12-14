package pl.sebcel.rc.gui.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;

import pl.sebcel.rc.events.RocketStateChangedEvent;
import pl.sebcel.rc.gui.components.ParachuteState;
import pl.sebcel.rc.gui.dto.RocketStatus;
import pl.sebcel.rc.gui.formatters.AccelerationFormat;
import pl.sebcel.rc.gui.formatters.AngleFormat;
import pl.sebcel.rc.gui.formatters.DensityFormat;
import pl.sebcel.rc.gui.formatters.DistanceFormat;
import pl.sebcel.rc.gui.formatters.ForceFormat;
import pl.sebcel.rc.gui.formatters.MassFormat;
import pl.sebcel.rc.gui.formatters.RadialVelocityFormat;
import pl.sebcel.rc.gui.formatters.VelocityFormat;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;
import pl.sebcel.rc.infrastructure.events.EventListener;

public class ControlsDisplay extends JComponent implements EventListener<RocketStateChangedEvent> {

    public enum Status {
	KINETIC_STATE_SURFACE_RELATIVE, KINETIC_STATE_SPACE_RELATIVE, INTERNAL_STATE, ENVIRONMENT_CONDITIONS;
    }

    private static final long serialVersionUID = 1L;
    private int rocketID;
    private RocketStatus rocketStatus;
    private Status displayStatus = Status.KINETIC_STATE_SURFACE_RELATIVE;

    private Font regular = new Font("Times", Font.PLAIN, 12);
    private Font bold = new Font("Times", Font.BOLD, 12);
    private DistanceFormat df = new DistanceFormat();
    private VelocityFormat vf = new VelocityFormat();
    private AccelerationFormat af = new AccelerationFormat();
    private MassFormat mf = new MassFormat();
    private AngleFormat alphaF = new AngleFormat();
    private RadialVelocityFormat omegaF = new RadialVelocityFormat();
    private ForceFormat ff = new ForceFormat();
    private DensityFormat densF = new DensityFormat();

    public ControlsDisplay(int rocketID) {
	this.rocketID = rocketID;
	EventBusFactory.getInstance().subscribeToEvent(RocketStateChangedEvent.class, this);
    }

    public void setDisplayStatus(Status displayStatus) {
	this.displayStatus = displayStatus;
    }

    @Override
    public void eventFired(RocketStateChangedEvent event) {
	if (event.getRocketId() == this.rocketID) {
	    this.rocketStatus = event.getRocketState();
	}
    }

    private double total(double x, double y) {
	return Math.sqrt(x * x + y * y);
    }

    @Override
    public void paint(Graphics g) {
	if (rocketStatus == null) {
	    return;
	}
	this.setEnabled(false);

	g.setColor(new Color(255, 255, 255, 100));
	g.fillRect(10, 18, 350, 100);

	if (displayStatus == Status.KINETIC_STATE_SURFACE_RELATIVE) {
	    renderKineticStateSurfaceRelative(g);
	}

	if (displayStatus == Status.KINETIC_STATE_SPACE_RELATIVE) {
	    renderKineticStateSpaceRelative(g);
	}

	if (displayStatus == Status.INTERNAL_STATE) {
	    renderInternalState(g);
	}

	if (displayStatus == Status.ENVIRONMENT_CONDITIONS) {
	    renderEnvironmentConditions(g);
	}

	renderHelp(g);
    }

    private void renderInternalState(Graphics g) {

	String status = rocketStatus.getRocketLifeCycle().getDescription();
	if (rocketStatus.getDrougeParachuteState() == ParachuteState.DEPLOYED) {
	    status += " + drouge parachute deployed";
	}

	if (rocketStatus.getDragParachuteState() == ParachuteState.DEPLOYED) {
	    status += " + drag parachute deployed";
	}

	if (rocketStatus.isKillRotation()) {
	    status += " + kill rotation";
	}

	g.setColor(Color.black);
	g.setFont(bold);
	g.drawString("Current display: Rocket internal state", 12, 30);

	g.setFont(bold);
	g.drawString("Status", 12, 45);
	g.setFont(regular);
	g.drawString(status, 90, 45);

	g.setFont(bold);
	g.drawString("Model", 12, 60);
	g.setFont(regular);
	if (rocketStatus.isSimplifiedModel()) {
	    g.drawString("Simplified", 90, 60);
	} else {
	    g.drawString("Full", 90, 60);
	}

	g.setFont(bold);
	g.drawString("Fuel", 12, 75);
	g.setFont(regular);
	g.drawString("1st stage: " + mf.format(rocketStatus.getFirstStageFuel()), 90, 75);
	g.drawString("2nd stage: " + mf.format(rocketStatus.getSecondStageFuel()), 90, 90);
	g.drawString("3rd stage: " + mf.format(rocketStatus.getThirdStageFuel()), 90, 105);

    }

    private void renderKineticStateSurfaceRelative(Graphics g) {
	g.setColor(Color.black);
	g.setFont(bold);
	g.drawString("Current display: Rocket kinetic state (relative to surface)", 12, 30);
	g.drawString("Position", 12, 45);
	g.setFont(regular);
	g.drawString("x: " + df.format(rocketStatus.getL()), 90, 45);
	g.drawString("y: " + df.format(rocketStatus.getH()), 160, 45);

	g.setFont(bold);
	g.drawString("Velocity", 12, 60);
	g.setFont(regular);
	g.drawString("x: " + vf.format(rocketStatus.getVh()), 90, 60);
	g.drawString("y: " + vf.format(rocketStatus.getVv()), 160, 60);
	g.drawString("total: " + vf.format(total(rocketStatus.getVx(), rocketStatus.getVy())), 240, 60);

	g.setFont(bold);
	g.drawString("Acceleration", 12, 75);
	g.setFont(regular);
	g.drawString("x: " + af.formatSimple(rocketStatus.getAh()), 90, 75);
	g.drawString("y: " + af.formatSimple(rocketStatus.getAv()), 160, 75);
	g.drawString("total: " + af.format(total(rocketStatus.getAx(), rocketStatus.getAy()), false), 240, 75);

	g.setFont(bold);
	g.drawString("Attitude", 12, 90);
	g.setFont(regular);
	g.drawString("\u03b1: " + alphaF.format(rocketStatus.getRelativeAlpha()), 90, 90);
	g.drawString("\u03a9: " + omegaF.format(rocketStatus.getOmega()), 160, 90);
    }

    private void renderKineticStateSpaceRelative(Graphics g) {
	g.setColor(Color.black);
	g.setFont(bold);
	g.drawString("Current display: Rocket kinetic state (relative to space)", 12, 30);
	g.drawString("Position", 12, 45);
	g.setFont(regular);
	g.drawString("x: " + df.format(rocketStatus.getX()), 90, 45);
	g.drawString("y: " + df.format(rocketStatus.getY()), 160, 45);

	g.setFont(bold);
	g.drawString("Velocity", 12, 60);
	g.setFont(regular);
	g.drawString("x: " + vf.format(rocketStatus.getVx()), 90, 60);
	g.drawString("y: " + vf.format(rocketStatus.getVy()), 160, 60);
	g.drawString("total: " + vf.format(total(rocketStatus.getVx(), rocketStatus.getVy())), 240, 60);

	g.setFont(bold);
	g.drawString("Acceleration", 12, 75);
	g.setFont(regular);
	g.drawString("x: " + af.formatSimple(rocketStatus.getAx()), 90, 75);
	g.drawString("y: " + af.formatSimple(rocketStatus.getAy()), 160, 75);
	g.drawString("total: " + af.format(total(rocketStatus.getAx(), rocketStatus.getAy()), false), 240, 75);

	g.setFont(bold);
	g.drawString("Attitude", 12, 90);
	g.setFont(regular);
	g.drawString("\u03b1: " + alphaF.format(rocketStatus.getAlpha()), 90, 90);
	g.drawString("\u03a9: " + omegaF.format(rocketStatus.getOmega()), 160, 90);
    }

    private void renderEnvironmentConditions(Graphics g) {
	g.setColor(Color.black);
	g.setFont(bold);
	g.drawString("Current display: Environment conditions", 12, 30);
	g.drawString("Air density", 12, 45);
	g.setFont(regular);
	g.drawString(densF.format(rocketStatus.getStaticPressure()), 160, 45);

	g.setFont(bold);
	g.drawString("Aerodynamic forces", 12, 60);
	g.setFont(regular);
	g.drawString("x: " + ff.format(rocketStatus.getDynamicPressureX()), 160, 60);
	g.drawString("y: " + ff.format(rocketStatus.getDynamicPressureY()), 160, 75);
    }

    private void renderHelp(Graphics g) {
	Rectangle clipBounds = g.getClipBounds();

	int x = clipBounds.width - 205;

	g.setColor(new Color(255, 255, 255, 100));
	g.fillRect(x, 18, 200, 110);

	g.setColor(Color.BLACK);
	g.setFont(bold);
	g.drawString("Controls", x + 5, 30);

	g.setFont(regular);
	if (rocketID == 0) {
	    g.drawString("Engines: A, Z, X", x + 5, 45);
	    g.drawString("Staging/parachutes: B", x + 5, 60);
	    g.drawString("Kill rotation: S", x + 5, 75);
	    g.drawString("Reset: F5", x + 5, 90);
	    g.drawString("Control mode: F6", x + 5, 105);
	    g.drawString("Display mode: F1, F2, F3", x + 5, 120);
	}
	if (rocketID == 1) {
	    g.drawString("Engines: cursor up, left, right", x + 5, 45);
	    g.drawString("Staging/parachutes: cursor down", x + 5, 60);
	    g.drawString("Kill rotation: numeric 5", x + 5, 75);
	    g.drawString("Reset: F8", x + 5, 90);
	    g.drawString("Control mode: F7", x + 5, 105);
	    g.drawString("Display mode: F9, F10, F11", x + 5, 120);
	}
    }
}