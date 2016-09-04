/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.watch;

import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.util.SysConfig;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wang.lcs.pac.task.core.exception.ShutdownException;

/**
 *
 * @author changshu.li
 */
public class WatchCommand extends Thread {

	public static final char COMMAND_SPIDE = '\n';
	private static final Logger logger = LoggerFactory.getLogger(WatchCommand.class);
	private final int fix = Integer.valueOf(SysConfig.getConfig("thread.command", "1"));
	private final ServerSocket server;
	private final TaskContext context;
	private final ExecutorService pool;

	public WatchCommand(TaskContext context, int port) throws IOException {
		this.server = new ServerSocket(port);
		this.context = context;
		this.setDaemon(true);//后台进程
		this.setName("WatchCommand");
		pool = Executors.newFixedThreadPool( //后台运行
				fix, new ThreadFactory() {

			@Override
			public Thread newThread(Runnable r) {
				Thread th = new Thread(r);
				th.setDaemon(true); //后台运行
				th.setName("WatchRequest");
				return th;
			}

		});
	}

	@Override
	public void run() {
		logger.info("start watch command with [{}], Thread pool size (thread.command) [{}]", server.getLocalPort(), fix);
		try {
			while (true) {
				Socket sk = server.accept();
				if (!pool.isShutdown()) {
					pool.submit(new Reqeust(sk, context));
				}
			}
		} catch (IOException ex) {
			logger.error("Monitor stop java process error !", ex);
		}
		logger.info("Exit");
	}

	private static class Reqeust extends Thread {

		private final Socket sk;
		private final TaskContext context;

		public Reqeust(Socket sk, TaskContext context) {
			this.sk = sk;
			this.context = context;
		}

		@Override
		public void run() {
			try {
				InputStream in = new BufferedInputStream(sk.getInputStream());
				byte[] buff = new byte[1024 * 2];
				StringBuffer str = new StringBuffer();
				for (int i = 0; i < buff.length; i++) {
					int ln = in.read();//Block
					if (COMMAND_SPIDE == ln) {
						break;
					}
					str.append((char) ln);
				}
				if (str.length() < buff.length) {
					//优先事件
					context.getAgent().putPriorityEvent(
							new SocketCommandEvent(str.toString()));
				} else {
					throw new IOException("Not find command !\r\n" + str);
				}
			} catch (ShutdownException | IOException ex) {
				logger.error("Watch command error!", ex);
				throw new RuntimeException(ex);
			}
		}

	}

//	public synchronized boolean isStop() {
//		return stop;
//	}
//
//	public synchronized void quit() {
//		this.stop = true;
//		for (Task t : tasks) {
//			t.stopTask();
//		}
//		pool.shutdown();
//	}
//
//	public synchronized <T extends Task> Task add(T thread) {
//		tasks.add(thread);
//		return thread;
//	}
}
