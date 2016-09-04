/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task;

import wang.lcs.pac.task.util.SysConfig;
import wang.lcs.pac.task.watch.CommandListener;
import wang.lcs.pac.task.watch.WatchCommand;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author changshu.li
 */
public class Stop {

	public static void main(String[] args) throws IOException {
		String port = SysConfig.getConfig("watch.port", "54896");
		System.out.println(String.format("Send [%s] command at port [%s]", CommandListener.STOP_STRING, port));
		try (OutputStream out = new Socket("127.0.0.1",
				Integer.valueOf(port)).getOutputStream()) {
			out.write(CommandListener.STOP_STRING.getBytes());
			out.write(WatchCommand.COMMAND_SPIDE);
		}
	}
}
