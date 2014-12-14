package pl.sebcel.rc.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.sebcel.rc.events.RocketSettingsChangedEvent;
import pl.sebcel.rc.events.RocketStateChangedEvent;
import pl.sebcel.rc.gui.dto.RocketStatus;
import pl.sebcel.rc.gui.formatters.AccelerationFormat;
import pl.sebcel.rc.gui.formatters.AngleFormat;
import pl.sebcel.rc.gui.formatters.DistanceFormat;
import pl.sebcel.rc.gui.formatters.MassFormat;
import pl.sebcel.rc.gui.formatters.RadialVelocityFormat;
import pl.sebcel.rc.gui.formatters.VelocityFormat;
import pl.sebcel.rc.infrastructure.events.EventBus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;
import pl.sebcel.rc.infrastructure.events.EventListener;

@Deprecated
public class ControlsPanel extends JPanel implements ChangeListener, ActionListener, EventListener<RocketStateChangedEvent> {

    private static final long serialVersionUID = 1L;

    private int rocketId;

    private JSlider firstStageMainThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JSlider secondStageMainThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JSlider thirdStageMainThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JRadioButton killRotationButton = new JRadioButton("Kill rotation");
    private JSlider retroThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JSlider leftThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JSlider rightThrustSlider = new JSlider(JSlider.HORIZONTAL);
    private JButton resetButton = new JButton("Reset");

    private JTextField xField = new JTextField();
    private JTextField yField = new JTextField();
    private JTextField alphaField = new JTextField();

    private JTextField vxField = new JTextField();
    private JTextField vyField = new JTextField();
    private JTextField omegaField = new JTextField();

    private JTextField axField = new JTextField();
    private JTextField ayField = new JTextField();
    private JTextField epsilonField = new JTextField();

    private JTextField spField = new JTextField();
    private JTextField dpxField = new JTextField();
    private JTextField dpyField = new JTextField();

    private JTextField fuel1Field = new JTextField();
    private JTextField fuel2Field = new JTextField();
    private JTextField fuel3Field = new JTextField();

    private JRadioButton firstStageSeparationButton = new JRadioButton("1st stage");
    private JRadioButton secondStageSeparationButton = new JRadioButton("2nd stage");
    private JRadioButton thirdStageSeparationButton = new JRadioButton("3rd stage");

    private JRadioButton drougeParachuteDeploymentButton = new JRadioButton("Drouge");
    private JRadioButton dragParachuteDeploymentButton = new JRadioButton("Drag");
    private boolean drougeParachuteHasBeenAlreadyDeployed = false;

    private EventBus eventBus;

    private boolean reset = false;

    public ControlsPanel(int rocketId) {

	eventBus = EventBusFactory.getInstance();
	eventBus.subscribeToEvent(RocketStateChangedEvent.class, this);

	this.rocketId = rocketId;

	this.firstStageMainThrustSlider.setValue(0);
	this.secondStageMainThrustSlider.setValue(0);
	this.thirdStageMainThrustSlider.setValue(0);

	this.leftThrustSlider.setValue(100);
	this.rightThrustSlider.setValue(0);
	this.firstStageMainThrustSlider.addChangeListener(this);
	this.secondStageMainThrustSlider.addChangeListener(this);
	this.thirdStageMainThrustSlider.addChangeListener(this);
	this.killRotationButton.addChangeListener(this);
	this.leftThrustSlider.addChangeListener(this);
	this.rightThrustSlider.addChangeListener(this);
	this.resetButton.addActionListener(this);
	this.firstStageSeparationButton.addChangeListener(this);
	this.secondStageSeparationButton.addChangeListener(this);
	this.thirdStageSeparationButton.addChangeListener(this);
	this.drougeParachuteDeploymentButton.addChangeListener(this);
	this.dragParachuteDeploymentButton.addChangeListener(this);

	this.firstStageMainThrustSlider.setPreferredSize(new Dimension(140, 20));
	this.secondStageMainThrustSlider.setPreferredSize(new Dimension(140, 20));
	this.thirdStageMainThrustSlider.setPreferredSize(new Dimension(140, 20));
	this.leftThrustSlider.setPreferredSize(new Dimension(140, 20));
	this.rightThrustSlider.setPreferredSize(new Dimension(140, 20));

	this.vxField.setPreferredSize(new Dimension(110, 21));
	this.vyField.setPreferredSize(new Dimension(110, 21));

	int y = 0;

	this.setLayout(new GridBagLayout());
	this.add(new JLabel("Main engines"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("1st stage"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(firstStageMainThrustSlider, new GridBagConstraints(1, y++, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("2nd stage"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(secondStageMainThrustSlider, new GridBagConstraints(1, y++, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("3rd stage"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(thirdStageMainThrustSlider, new GridBagConstraints(1, y++, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Left"), new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("Right"), new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(leftThrustSlider, new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(rightThrustSlider, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(killRotationButton, new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(resetButton, new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Position"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("x"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(xField, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("y"), new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(yField, new GridBagConstraints(3, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Orientation"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("\u03b1"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(alphaField, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Speed"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("vx"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(vxField, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("vy"), new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(vyField, new GridBagConstraints(3, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("\u03a9"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(omegaField, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Acceleration"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("ax"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(axField, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("ay"), new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(ayField, new GridBagConstraints(3, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("\u03f5"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(epsilonField, new GridBagConstraints(1, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Pressure"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("Stat"), new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(spField, new GridBagConstraints(3, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("Dyn x"), new GridBagConstraints(0, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(dpxField, new GridBagConstraints(1, y, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("Dyn y"), new GridBagConstraints(2, y, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(dpyField, new GridBagConstraints(3, y++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Fuel"), new GridBagConstraints(0, y++, 4, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("1st stage"), new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(fuel1Field, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("2nd stage"), new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(fuel2Field, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("3rd stage"), new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(fuel3Field, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 1, 1), 1, 1));

	this.add(new JLabel("Separation"), new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(new JLabel("Parachutes"), new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(firstStageSeparationButton, new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(drougeParachuteDeploymentButton, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));

	this.add(secondStageSeparationButton, new GridBagConstraints(0, y, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(dragParachuteDeploymentButton, new GridBagConstraints(2, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));
	this.add(thirdStageSeparationButton, new GridBagConstraints(0, y++, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(1, 1, 1, 1), 1, 1));

	leftThrustSlider.setMinimumSize(new Dimension(150, 21));
	rightThrustSlider.setMinimumSize(new Dimension(150, 21));

	this.setBorder(BorderFactory.createRaisedBevelBorder());
	this.setMinimumSize(new Dimension(400, 100));
    }

    @Override
    public void stateChanged(ChangeEvent e) {
	fireEvent();
    }

    private void fireEvent() {
	RocketControlSettings settings = new RocketControlSettings();
//	settings.setFirstStageMainThrust((double) firstStageMainThrustSlider.getValue() / 100);
//	settings.setSecondStageMainThrust((double) secondStageMainThrustSlider.getValue() / 100);
//	settings.setThirdStageMainThrust((double) thirdStageMainThrustSlider.getValue() / 100);
//	settings.setRetroThrust((double) retroThrustSlider.getValue() / 100);
	settings.setLeftThrust(1 - (double) leftThrustSlider.getValue() / 100);
	settings.setRightThrust((double) rightThrustSlider.getValue() / 100);
//	settings.setFirstStageSeparation(firstStageSeparationButton.isSelected());
//	settings.setSecondStageSeparation(secondStageSeparationButton.isSelected());
//	settings.setThirdStageSeparation(thirdStageSeparationButton.isSelected());
//	settings.setDrougeParachuteDeployment(drougeParachuteDeploymentButton.isSelected());
//	settings.setDragParachuteDeployment(dragParachuteDeploymentButton.isSelected());
	settings.setReset(reset);
	settings.setKillRotation(killRotationButton.isSelected());
	eventBus.fireEvent(new RocketSettingsChangedEvent(rocketId, settings));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource().equals(resetButton)) {
	    reset();
	}
    }

    public void setMainThrust(double thrust) {
	if (!firstStageSeparationButton.isSelected()) {
	    firstStageMainThrustSlider.setValue((int) (100 * thrust));
	    return;
	}
	if (!secondStageSeparationButton.isSelected()) {
	    secondStageMainThrustSlider.setValue((int) (100 * thrust));
	    return;
	}
	thirdStageMainThrustSlider.setValue((int) (100 * thrust));
    }

    public void setRetroThrust(double thrust) {
	retroThrustSlider.setValue((int) (100 * thrust));
    }

    public void setLeftThrust(double thrust) {
	leftThrustSlider.setValue((int) (100 * (1 - thrust)));
    }

    public void setRightThrust(double thrust) {
	rightThrustSlider.setValue((int) (100 * thrust));
    }

    public void specialAction() {
	if (!firstStageSeparationButton.isSelected()) {
	    firstStageSeparationButton.setSelected(true);
	    return;
	}

	if (!secondStageSeparationButton.isSelected()) {
	    secondStageSeparationButton.setSelected(true);
	    return;
	}

	if (!thirdStageSeparationButton.isSelected()) {
	    thirdStageSeparationButton.setSelected(true);
	    return;
	}

	if (!drougeParachuteDeploymentButton.isSelected() && !drougeParachuteHasBeenAlreadyDeployed) {
	    drougeParachuteDeploymentButton.setSelected(true);
	    return;
	}

	if (drougeParachuteDeploymentButton.isSelected()) {
	    drougeParachuteDeploymentButton.setSelected(false);
	    drougeParachuteHasBeenAlreadyDeployed = true;
	    return;
	}

	if (!dragParachuteDeploymentButton.isSelected()) {
	    dragParachuteDeploymentButton.setSelected(true);
	    return;
	}

	if (dragParachuteDeploymentButton.isSelected()) {
	    dragParachuteDeploymentButton.setSelected(false);
	    return;
	}
    }

    private DistanceFormat df = new DistanceFormat();
    private VelocityFormat vf = new VelocityFormat();
    private AccelerationFormat af = new AccelerationFormat();
    private MassFormat mf = new MassFormat();
    private DecimalFormat f = new DecimalFormat("##0");
    private AngleFormat alphaF = new AngleFormat();
    private RadialVelocityFormat omegaF = new RadialVelocityFormat();

    @Override
    public void eventFired(RocketStateChangedEvent event) {

	if (event.getRocketId() == this.rocketId) {
	    RocketStatus s = event.getRocketState();
	    this.setEnabled(false);

	    xField.setText(df.format(s.getX()));
	    yField.setText(df.format(s.getY()));
	    alphaField.setText(alphaF.format(s.getAlpha()));

	    vxField.setText(vf.format(s.getVx()));
	    vyField.setText(vf.format(s.getVy()));
	    omegaField.setText(omegaF.format(s.getOmega()));

	    axField.setText(af.format(s.getAx(), false));
	    ayField.setText(af.format(s.getAy(), true));
	    epsilonField.setText(f.format(s.getEpsilon()));

	    spField.setText(f.format(s.getStaticPressure()));
	    dpxField.setText(f.format(s.getDynamicPressureX()));
	    dpyField.setText(f.format(s.getDynamicPressureY()));

	    fuel1Field.setText(mf.format(s.getFirstStageFuel()));
	    fuel2Field.setText(mf.format(s.getSecondStageFuel()));
	    fuel3Field.setText(mf.format(s.getThirdStageFuel()));

	    if (s.getOmega() == 0) {
		killRotationButton.setSelected(false);
	    }

	    this.setEnabled(true);
	}
    }

    public void reset() {
	this.reset = true;
	fireEvent();
	firstStageMainThrustSlider.setValue(0);
	secondStageMainThrustSlider.setValue(0);
	thirdStageMainThrustSlider.setValue(0);
	retroThrustSlider.setValue(0);
	firstStageSeparationButton.setSelected(false);
	secondStageSeparationButton.setSelected(false);
	thirdStageSeparationButton.setSelected(false);
	dragParachuteDeploymentButton.setSelected(false);
	drougeParachuteDeploymentButton.setSelected(false);
	killRotationButton.setSelected(false);
	drougeParachuteHasBeenAlreadyDeployed = false;
	this.reset = false;
    }

    public void killRotation() {
	killRotationButton.setSelected(!killRotationButton.isSelected());
    }
}