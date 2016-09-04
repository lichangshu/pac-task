/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.watch;

import wang.lcs.pac.task.core.event.Event;

/**
 *
 * @author changshu.li
 */
public class SocketCommandEvent implements Event {

	final private String command;

	public SocketCommandEvent(String cmd) {
		this.command = cmd;
	}

	public String getCommand() {
		return command;
	}
}
