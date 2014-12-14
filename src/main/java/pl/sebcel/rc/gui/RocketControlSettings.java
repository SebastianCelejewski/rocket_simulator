package pl.sebcel.rc.gui;

public class RocketControlSettings {

    private double mainThrust;
    private double leftThrust;
    private double rightThrust;
    private boolean lifeCycleAction;
    private boolean reset;
    private boolean killRotation;
    private boolean simplifiedModel;

    public double getMainThrust() {
	return mainThrust;
    }

    public void setMainThrust(double mainThrust) {
	this.mainThrust = mainThrust;
    }

    public double getLeftThrust() {
	return leftThrust;
    }

    public void setLeftThrust(double leftThrust) {
	this.leftThrust = leftThrust;
    }

    public double getRightThrust() {
	return rightThrust;
    }

    public void setRightThrust(double rightThrust) {
	this.rightThrust = rightThrust;
    }

    public boolean isReset() {
	return reset;
    }

    public void setReset(boolean reset) {
	this.reset = reset;
    }

    public boolean isKillRotation() {
	return killRotation;
    }

    public void setKillRotation(boolean killRotation) {
	this.killRotation = killRotation;
    }

    public boolean isLifeCycleAction() {
	return lifeCycleAction;
    }

    public void setLifeCycleAction(boolean lifeCycleAction) {
	this.lifeCycleAction = lifeCycleAction;
    }

    public boolean isSimplifiedModel() {
	return simplifiedModel;
    }

    public void setSimplifiedModel(boolean simplifiedModel) {
	this.simplifiedModel = simplifiedModel;
    }

}