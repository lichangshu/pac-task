/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.impl;

import wang.lcs.pac.task.core.Agent;
import wang.lcs.pac.task.core.EventDispatcher;
import wang.lcs.pac.task.core.Scheduler;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.exception.ShutdownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class AgentImpl implements Agent {

	private static final Logger logger = LoggerFactory.getLogger(Agent.class);
	protected TaskContext context;
	protected EventDispatcher dispatcher;
	protected Scheduler scheduler;
	protected transient boolean shutdown = false;

	public AgentImpl(TaskContext context) {
		this.context = context;
		this.dispatcher = context.getEventDispatcher();
		this.scheduler = new SchedulerImpl();
	}

	@Override
	public void start() {
	}

	@Override
	public boolean putEvent(Event e, long timeout) throws ShutdownException, InterruptedException {
		check();
		return scheduler.schedule(e, timeout);
	}

	private void check() throws ShutdownException {
		if (isShutdown()) {
			logger.info("Agent is shutdown!");
			throw new ShutdownException();
		}
	}

	@Override
	public boolean putPriorityEvent(Event e, long timeout) throws ShutdownException, InterruptedException {
		check();
		return scheduler.schedule(e, timeout, 1);
	}

	@Override
	public Event fetchEvent(long timeout) throws ShutdownException, InterruptedException {
		check();
		return scheduler.fetchEvent(timeout);
	}

	@Override
	public void flagDone(Event event) {
		scheduler.flagDone(event);
	}

	@Override
	public synchronized void shutdown() {
		this.shutdown = true;
	}

	@Override
	public synchronized boolean isShutdown() {
		return shutdown;
	}

	@Override
	public void putEvent(Event e) throws ShutdownException {
		while (true) {
			try {
				boolean ok = this.putEvent(e, 100);
				if (ok) {
					return;
				}
			} catch (InterruptedException ex) {
				logger.error("Thread is Interrupted!", ex);
				Thread.interrupted();
			}
		}
	}

	@Override
	public void putPriorityEvent(Event e) throws ShutdownException {
		while (true) {
			try {
				boolean ok = this.putPriorityEvent(e, 100);
				if (ok) {
					return;
				}
			} catch (InterruptedException ex) {
				logger.error("Thread is Interrupted!", ex);
				Thread.interrupted();
			}
		}
	}

	@Override
	public Event fetchEvent() throws ShutdownException {
		while (true) {
			try {
				Event ok = this.fetchEvent(100);
				if (ok != null) {
					return ok;
				}
			} catch (InterruptedException ex) {
				logger.error("Thread is Interrupted!", ex);
				Thread.interrupted();
			}
		}
	}
}
