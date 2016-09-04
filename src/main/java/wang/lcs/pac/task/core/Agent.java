/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core;

import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.exception.ShutdownException;

/**
 *
 * @author changshu.li
 */
public interface Agent {

	public void start();

	public boolean putEvent(Event e, long timeout) throws InterruptedException, ShutdownException;

	public void putEvent(Event e) throws ShutdownException;

	public boolean putPriorityEvent(Event e, long timeout) throws InterruptedException, ShutdownException;

	public void putPriorityEvent(Event e) throws ShutdownException;

	public Event fetchEvent(long timeout) throws InterruptedException, ShutdownException;

	public Event fetchEvent() throws ShutdownException;

	public void flagDone(Event event);

	public void shutdown();

	public boolean isShutdown();
}
