package mc;

import java.io.IOException;

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
	 * @param res
	 *            {@link Response}
	 * @throws IOException
	 */
	public void send(Response res) throws IOException;

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
}
