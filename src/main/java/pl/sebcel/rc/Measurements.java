package pl.sebcel.rc;

import java.io.FileWriter;
import java.util.Calendar;

import pl.sebcel.rc.events.RocketStateChangedEvent;
import pl.sebcel.rc.gui.dto.RocketStatus;
import pl.sebcel.rc.infrastructure.events.EventBusFactory;
import pl.sebcel.rc.infrastructure.events.EventListener;

public class Measurements implements EventListener<RocketStateChangedEvent> {

    private long initialTime = Calendar.getInstance().getTimeInMillis();

    public Measurements() {
	EventBusFactory.getInstance().subscribeToEvent(RocketStateChangedEvent.class, this);
    }

    @Override
    public void eventFired(RocketStateChangedEvent event) {
	int rocketID = event.getRocketId();
	RocketStatus rocketState = event.getRocketState();

	long relativeTime = Calendar.getInstance().getTimeInMillis() - initialTime;

	String row = relativeTime + ";";
	row += rocketState.getX() + ";";
	row += rocketState.getY() + ";";
	row += rocketState.getVx() + ";";
	row += rocketState.getVy() + ";";
	row += rocketState.getAx() + ";";
	row += rocketState.getAy() + ";";
	row += rocketState.getStaticPressure() + ";";
	row += rocketState.getDynamicPressureX() + ";";
	row += rocketState.getDynamicPressureY() + ";";
	row += rocketState.getFirstStageFuel() + ";";
	row += rocketState.getSecondStageFuel() + ";";
	row += rocketState.getThirdStageFuel() + "\n";

	try {
	    FileWriter fw = new FileWriter("rocket-" + rocketID + ".txt", true);
	    fw.write(row);
	    fw.flush();
	    fw.close();
	} catch (Exception ex) {
	    throw new RuntimeException("Failed to save rocket measurements: " + ex.getMessage(), ex);
	}
    }
}