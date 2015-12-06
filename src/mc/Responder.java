package mc;

import java.io.IOException;
import java.io.Serializable;

import mc.util.Adder;

/**
 * 报文应答接口
 * 
 * @author VicTan@qq.com
 *
 */
public interface Responder {

	/**
	 * 输出应答报文
	 * 
	 * @param packet
	 * @throws IOException
	 */
	public void send(Packet packet) throws IOException;

	/**
	 * 输出应答报文
	 * 
	 * @param adder
	 * @throws IOException
	 */
	public void send(Adder adder) throws IOException;

	/**
	 * 输出应答报文
	 * 
	 * @param res
	 * @throws IOException
	 */
	public void send(byte[] res) throws IOException;

	/**
	 * 输出应答报文
	 * 
	 * @param res
	 * @throws IOException
	 */
	public void send(byte res) throws IOException;

	/**
	 * 输出一个可序列化的对象，并且不记录到Trace中
	 * 
	 * @param data
	 *            {@link Serializable}
	 * @throws IOException
	 */
	public void doSendAnObjectAndDoNotTrace(Serializable data)
			throws IOException;
}
