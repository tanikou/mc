package mc;

import java.net.Socket;
import java.util.concurrent.Callable;

import mc.entity.Trace;

/**
 * 报文接受处理器
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Processor implements Callable<Trace> {
	/**
	 * 处理报文请求
	 * 
	 * @param client
	 *            {@link Socket}报文请求
	 * @return 处理器自身
	 */
	public abstract Processor handle(Socket client);
}
