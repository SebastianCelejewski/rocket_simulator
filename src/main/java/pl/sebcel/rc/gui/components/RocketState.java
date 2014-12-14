package pl.sebcel.rc.gui.components;

public enum RocketState {

    INITIAL ("Full set-up"), 
    FIRST_STAGE_SEPARATED ("First stage separated"),
    SECOND_STAGE_SEPARATED ("Second stage separated"), 
    THIRD_STAGE_SEPARATED ("Third stage separated");

    private String description;

    RocketState(String description) {
	this.description = description;
    }

    public String getDescription() {
	return description;
    }

}
