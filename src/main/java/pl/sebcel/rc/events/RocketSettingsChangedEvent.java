package pl.sebcel.rc.events;

import pl.sebcel.rc.gui.RocketControlSettings;

public class RocketSettingsChangedEvent {
    private int rocketId;
    private RocketControlSettings settings;

    public RocketSettingsChangedEvent(int rocketId, RocketControlSettings settings) {
	this.rocketId = rocketId;
	this.settings = settings;
    }

    public int getRocketId() {
	return rocketId;
    }

    public RocketControlSettings getSettings() {
	return settings;
    }
}