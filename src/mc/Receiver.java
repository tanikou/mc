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
public abstract class Receiver implements Callable<Trace> {
	public abstract Receiver handle(Socket client);
}
