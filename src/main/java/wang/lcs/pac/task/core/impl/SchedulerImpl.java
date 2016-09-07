/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.impl;

import wang.lcs.pac.task.core.Scheduler;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.event.StartEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.lcs.pac.task.util.SysConfig;

/**
 *
 * @author changshu.li
 */
public class SchedulerImpl implements Scheduler {

	private static final Logger logger = LoggerFactory.getLogger(SchedulerImpl.class);
	private static final int size = Integer.valueOf(SysConfig.
			getConfig("queue.fix", Integer.MAX_VALUE + ""));

	LinkedBlockingDeque<Event> eventList = new LinkedBlockingDeque<>(size);
	Set<Event> flagSet = new HashSet();
	int done = 0;

	public SchedulerImpl() {
		eventList.add(new StartEvent());
	}

	@Override
	public synchronized boolean schedule(Event event, long timeout) throws InterruptedException {
		return eventList.offer(event, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public synchronized boolean schedule(Event event, long timeout, int priority) throws InterruptedException {
		return eventList.offerFirst(event, timeout, TimeUnit.MILLISECONDS);
	}

	@Override
	public synchronized void flagDone(Event event) {
		boolean ok = flagSet.remove(event);
		if (ok) {
			done++;
		}
	}

	@Override
	public synchronized boolean allEvensDone() {
		return this.getAssignedCount() == 0
				&& this.getEventCount() == 0;
	}

	@Override
	public synchronized int getAssignedCount() {
		return flagSet.size();
	}

	public synchronized int getEventCount() {
		return eventList.size();
	}

	@Override
	public synchronized Event fetchEvent(long timeout) throws InterruptedException {
		Event ev = eventList.poll(timeout, TimeUnit.MILLISECONDS);
		if (ev != null) {
			flagSet.add(ev);
		}
		return ev;
	}
	/*
	public boolean schedule(Event event) {
		return eventList.add(event);
	}

	public boolean schedule(Event event, int priority) {
		return eventList.offerFirst(event);
	}

	public Event fetchEvent() {
		Event ev = eventList.poll();
		if (ev != null) {
			flagSet.add(ev);
		}
		return ev;
	}
	 */
}
