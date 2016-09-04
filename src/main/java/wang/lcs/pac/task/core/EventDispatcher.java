package wang.lcs.pac.task.core;

import wang.lcs.pac.task.core.event.Event;

/**
 *
 * @author changshu.li
 */
public interface EventDispatcher {

	public void initialize();

	public void shutdown();

	public void dispatch(Event event);
}
