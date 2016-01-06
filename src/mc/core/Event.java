package mc.core;

import java.util.EventListener;

/**
 * 事件
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Event implements EventListener {
	public abstract void todo(Object o);
}
