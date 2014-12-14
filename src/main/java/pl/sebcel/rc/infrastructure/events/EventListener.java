package pl.sebcel.rc.infrastructure.events;

public interface EventListener<T> {

    public void eventFired(T event);

}
