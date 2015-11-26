package mc;

import java.net.Socket;

/**
 * 报文接受处理器
 * 
 * @author VicTan@qq.com
 *
 */
public interface Handler extends Runnable {
	/**
	 * 设定报文请求Socket，具体处理应该在多线程的run方法中实现
	 * 
	 * @param client
	 *            {@link Socket}报文请求
	 * @return 处理器自身
	 */
	public Handler setup(Socket client);
}
