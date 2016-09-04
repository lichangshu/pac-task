/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.impl;

import wang.lcs.pac.task.core.Agent;
import wang.lcs.pac.task.core.EventDispatcher;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.listener.EventListener;
import wang.lcs.pac.task.core.support.AgentFactory;

/**
 *
 * @author changshu.li
 */
public class TaskContextImpl<T> implements TaskContext {

	private final EventDispatcher dispatcher;
	private final T evn;
	private final boolean noEventShutdown;

	public TaskContextImpl(T env, EventListener... listeners) {
		this(true, env, listeners);
	}

	public TaskContextImpl(boolean noEventShutdown, T env, EventListener... listeners) {
		this.noEventShutdown = noEventShutdown;
		this.evn = env;
		dispatcher = new EventDispatcherImpl(this, listeners);
		dispatcher.initialize();
	}

	@Override
	public EventDispatcher getEventDispatcher() {
		return dispatcher;
	}

	@Override
	public Agent getAgent() {
		return AgentFactory.getAgent(this);
	}

	@Override
	public T getEnv() {
		return evn;
	}

	@Override
	public boolean isNoEventShutdown() {
		return noEventShutdown;
	}

}
