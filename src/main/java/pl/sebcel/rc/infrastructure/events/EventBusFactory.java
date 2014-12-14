package pl.sebcel.rc.infrastructure.events;

public class EventBusFactory {

    private static EventBus instance;

    public static EventBus getInstance() {
	if (instance == null) {
	    instance = createInstance();
	}
	return instance;
    }

    private static EventBus createInstance() {
	return new EventBus();
    }

}
