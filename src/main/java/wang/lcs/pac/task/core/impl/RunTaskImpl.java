/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.impl;

import wang.lcs.pac.task.core.RunTask;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.event.Event;
import wang.lcs.pac.task.core.exception.NoSuitableItemFoundException;
import wang.lcs.pac.task.core.exception.ShutdownException;
import wang.lcs.pac.task.core.exception.TaskDoneException;
import wang.lcs.pac.task.util.SysConfig;
import java.util.concurrent.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class RunTaskImpl implements RunTask {

	private static final Logger logger = LoggerFactory.getLogger(RunTaskImpl.class);
	private final ExecutorService pool;
	private Thread taskThread;

	public RunTaskImpl() {
		int max = Integer.valueOf(SysConfig.getConfig("queue.thread", "8"));
		int fix = 2;
		logger.info("RunTaskImpl ThreadPoolExecutor size core {}, max [tasek.thread] {}", fix, max);
		this.pool = new ThreadPoolExecutor(fix, max, 60, TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(max + fix),
				new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				if (!executor.isShutdown()) {
					try {
						executor.getQueue().put(r);
					} catch (InterruptedException ex) {
						logger.error("Error !", ex);
						Thread.interrupted();
					}
				}
			}
		});
	}

	/**
	 * 至少两个线程
	 *
	 * @param context
	 */
	@Override
	public void execute(final TaskContext context) {
		logger.info("======================================================");
		logger.info("|| Task start!");
		logger.info("======================================================");
		context.getAgent().start();
		this.taskThread = new Thread() {

			@Override
			public void run() {
				while (true) {
					try {
						final Event event = context.getAgent().fetchEvent(100);
						if (event != null) {
							submitTask(context, event);
						} else {
							if (context.isNoEventShutdown()) {
								exit(context);//任务完成
								return;
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								Thread.interrupted();
								exit(context);//任务完成
								return;
							}
						}
					} catch (ShutdownException ex) {
						logger.error("Task will shut down !");
						exit(context);//任务完成
						return;
					} catch (Exception ex) {
						context.getAgent().shutdown();
						logger.error("Errorrrrrrrrrrrrrrrrrr!", ex);
						System.exit(1); //不使用
					}
				}
			}

		};
		this.taskThread.setName("DispatchRuning");
		taskThread.start();
	}

	/**
	 * 该任务不应该抛出其他错误，否则将导致程序退出！
	 *
	 * @param context
	 * @param event
	 * @throws InterruptedException
	 */
	protected void submitTask(final TaskContext context, final Event event) throws InterruptedException {
		if (pool.isShutdown()) {
			logger.error("Thread pool is Shut down !");
			Thread.currentThread().interrupt();
			throw new InterruptedException();
		}
		pool.submit(new Runnable() {

			@Override
			public void run() {
				Thread.currentThread().setName("DispatchTask");
				try {
					logger.debug("Run.... Task start!" + event);
					context.getEventDispatcher().dispatch(event);
					logger.debug("Run.... Task stop !" + event);
				} catch (Exception ex) {
					logger.error("Task error!!", ex);
				} finally {
					context.getAgent().flagDone(event);
				}
			}

		});
	}

	private void exit(TaskContext context) {
		this.pool.shutdown();
		context.getAgent().shutdown();
		logger.info("======================================================");
		logger.info("|| Task Over !");
		logger.info("======================================================");
	}

}
