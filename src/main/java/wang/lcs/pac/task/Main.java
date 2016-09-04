/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task;

import wang.lcs.pac.task.util.SysConfig;
import wang.lcs.pac.task.core.RunTask;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.impl.RunTaskImpl;
import wang.lcs.pac.task.core.support.TaskContextFactory;
import wang.lcs.pac.task.util.LogUncaughtExceptionHandler;
import wang.lcs.pac.task.watch.WatchCommand;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) throws IOException {

		Thread.setDefaultUncaughtExceptionHandler(new LogUncaughtExceptionHandler());

		TaskContext context = TaskContextFactory.createTaskContext(false, new Object());

		int port = Integer.valueOf(SysConfig.getConfig("watch.port", "54896"));
		WatchCommand cmd = new WatchCommand(context, port);
		RunTask run = new RunTaskImpl();

		run.execute(context);//开启新线程 前台任务线程！
		cmd.start();//后台監聽线程
	}
}
