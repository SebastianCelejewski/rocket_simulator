package pl.sebcel.rc.infrastructure.events;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus {

    private Map<Class<?>, Set<EventListener<?>>> listenersMap = new HashMap<Class<?>, Set<EventListener<?>>>();

    public void fireEvent(Object event) {
	Set<EventListener<?>> listeners = listenersMap.get(event.getClass());
	for (EventListener listener : listeners) {
	    listener.eventFired(event);
	}
    }

    public <T> void subscribeToEvent(Class<T> eventType, EventListener<T> listener) {
	Set<EventListener<?>> listeners = listenersMap.get(eventType);
	if (listeners == null) {
	    listeners = new HashSet<EventListener<?>>();
	    listenersMap.put(eventType, listeners);
	}

	listeners.add(listener);
    }
}
