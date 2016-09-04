package wang.lcs.pac.task.core;

import wang.lcs.pac.task.core.event.Event;

public interface Scheduler {

	//public boolean schedule(Event event);

	//public boolean schedule(Event event, int priority);

	//public Event fetchEvent();

	public boolean schedule(Event event, long timeout, int priority) throws InterruptedException;

	public boolean schedule(Event event, long timeout) throws InterruptedException;

	public Event fetchEvent(long timeout) throws InterruptedException;

	public void flagDone(Event event);

	public boolean allEvensDone();

	public int getAssignedCount();

}
