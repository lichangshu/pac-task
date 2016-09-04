/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(LogUncaughtExceptionHandler.class);

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		logger.error("Thread {} UncaughtException {}", t.getName(), e.getMessage());
	}
}
