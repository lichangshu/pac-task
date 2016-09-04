/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.listener;

import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.exception.TaskCancelException;

/**
 *
 * @author changshu.li
 */
public interface EventListener {

	public void on(Event event, TaskContext context) throws TaskCancelException;
}
