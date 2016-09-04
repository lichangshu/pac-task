/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.impl;

import wang.lcs.pac.task.core.EventDispatcher;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.exception.TaskCancelException;
import wang.lcs.pac.task.core.listener.EventListener;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class EventDispatcherImpl implements EventDispatcher {

	private static final Logger logger = LoggerFactory.getLogger(EventDispatcherImpl.class);
	private final List<EventListener> list = new CopyOnWriteArrayList();
	private final TaskContext context;

	public EventDispatcherImpl(TaskContext context, EventListener... list) {
		this.list.addAll(Arrays.asList(list));
		this.context = context;
	}

	@Override
	public void initialize() {
	}

	@Override
	public void shutdown() {
	}

	@Override
	public void dispatch(Event event) {
		for (EventListener i : list) {
			try {
				i.on(event, context);
			} catch (TaskCancelException ex) {
				logger.info("Task cancel {}, {}!", event, ex.getMessage());
				return;
			}
		}
	}
}
