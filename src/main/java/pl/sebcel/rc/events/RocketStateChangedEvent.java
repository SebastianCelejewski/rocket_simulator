package pl.sebcel.rc.events;

import pl.sebcel.rc.gui.dto.RocketStatus;

public class RocketStateChangedEvent {

    private int rocketId;
    private RocketStatus rocketState;

    public RocketStateChangedEvent(int rocketId, RocketStatus rocketState) {
	this.rocketId = rocketId;
	this.rocketState = rocketState;
    }

    public int getRocketId() {
	return rocketId;
    }

    public RocketStatus getRocketState() {
	return rocketState;
    }
}
