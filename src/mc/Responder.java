package mc;

import java.io.IOException;

import mc.entity.Response;

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
