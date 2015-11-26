package mc;

import java.io.IOException;

import mc.entity.Response;

/**
 * 报文应答接口（应该由{@link Handler}实现一同实现，并由{@link Action}处理完成时调用把应答报文返回出去）
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
}
