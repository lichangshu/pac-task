/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.watch;

import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.listener.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class CommandListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(CommandListener.class);
	public static final String STOP_STRING = "STOP";

	@Override
	public void on(Event event, TaskContext context) {
		if (event instanceof SocketCommandEvent) {
			SocketCommandEvent cmd = (SocketCommandEvent) event;
			logger.info("Do socket event {}, {}", cmd.getCommand(), cmd);
			if (STOP_STRING.equalsIgnoreCase(cmd.getCommand())) {
				context.getAgent().shutdown();
			}
		}
	}

}
