package pl.sebcel.rc.gui.graphics;

import pl.sebcel.rc.events.RocketSettingsChangedEvent;
import pl.sebcel.rc.gui.RocketControlSettings;
import pl.sebcel.rc.infrastructure.events.EventBus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;

public class ControlsInterface {

    private int rocketID;
    private EventBus eventBus;

    private double mainThrust;
    private double leftThrust;
    private double rightThrust;

    private boolean lifecycleAction;
    private boolean killRotation;
    private boolean reset;
    private boolean simplifiedModel;

    public ControlsInterface(int rocketID) {
	this.rocketID = rocketID;
	this.eventBus = EventBusFactory.getInstance();
    }

    public void setMainThrust(double mainThrust) {
	this.mainThrust = mainThrust;
	fireEvent();
    }

    public void setLeftThrust(double leftThrust) {
	this.leftThrust = leftThrust;
	fireEvent();
    }

    public void setRightThrust(double rightThrust) {
	this.rightThrust = rightThrust;
	fireEvent();
    }

    public void toggleSpecialAction() {
	lifecycleAction = true;
	fireEvent();
    }

    public void killRotation() {
	killRotation = !killRotation;
	fireEvent();
    }

    public void reset() {
	reset = true;
	mainThrust = 0;
	leftThrust = 0;
	rightThrust = 0;
	killRotation = false;
	lifecycleAction = false;
	fireEvent();
    }

    private void fireEvent() {
	RocketControlSettings settings = new RocketControlSettings();
	settings.setMainThrust(mainThrust);
	settings.setLeftThrust(leftThrust);
	settings.setRightThrust(rightThrust);
	settings.setLifeCycleAction(lifecycleAction);
	settings.setReset(reset);
	settings.setKillRotation(killRotation);
	settings.setSimplifiedModel(simplifiedModel);
	eventBus.fireEvent(new RocketSettingsChangedEvent(rocketID, settings));
	lifecycleAction = false;
	reset = false;
    }

    public void setToggleSimplifiedModel() {
	this.simplifiedModel = !simplifiedModel;
	fireEvent();
    }
}