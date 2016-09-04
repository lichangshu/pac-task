/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wang.lcs.pac.task.core.support;

import wang.lcs.pac.task.core.Agent;
import wang.lcs.pac.task.core.TaskContext;
import wang.lcs.pac.task.core.impl.AgentImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author changshu.li
 */
public class AgentFactory {

	private static Map<TaskContext, Agent> map = new ConcurrentHashMap();

	public static Agent getAgent(TaskContext context) {
		if (map.get(context) == null) {
			synchronized (AgentFactory.class) {
				if (map.get(context) == null) {
					map.put(context, new AgentImpl(context));
				}
			}
		}
		return map.get(context);
	}
}
