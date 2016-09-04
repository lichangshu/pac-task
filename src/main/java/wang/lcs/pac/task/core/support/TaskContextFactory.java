/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.support;

import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.impl.TaskContextImpl;
import wang.lcs.pac.task.core.listener.EventListener;
import wang.lcs.pac.task.util.SysConfig;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author changshu.li
 */
public class TaskContextFactory {

	private static final Logger logger = LoggerFactory.getLogger(TaskContextFactory.class);
	public static String prefix = "task.event.listener.";

	public static <T> TaskContext createTaskContext(boolean done, T env) {
		return createTaskContext(done, env, "def");
	}

	public static <T> TaskContext createTaskContext(boolean done, T env, String suffix) {
		String fix = prefix + suffix + ".";
		List<EventListener> list = new ArrayList();
		Properties conf = SysConfig.getConfig();
		Set<String> names = new TreeSet(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
		});
		names.addAll(conf.stringPropertyNames());
		for (String key : names) {
			if (!key.startsWith(fix)) {
				continue;
			}
			String cls = conf.getProperty(key, "");
			try {
				int ix = cls.indexOf(":");
				if (ix > 0) {
					list.add((EventListener) Class.forName(cls.substring(0, ix)).getConstructor(String.class).newInstance(cls.substring(ix + 1)));
				} else {
					list.add((EventListener) Class.forName(cls).newInstance());
				}
				logger.debug("Create Listener Object Ok, [{}]!", cls);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
				logger.error("Create Listener Object error !", ex);
			}
		}
		logger.info("Create Listener Object Ok, size {}, {}!", list.size(), list);
		return new TaskContextImpl(done, env, list.toArray(new EventListener[list.size()]));
	}
}
