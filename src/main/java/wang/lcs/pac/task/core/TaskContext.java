/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core;

/**
 *
 * @author changshu.li
 */
public interface TaskContext<T> {

	public Agent getAgent();

	public EventDispatcher getEventDispatcher();

	public T getEnv();

	public boolean isNoEventShutdown();

}
