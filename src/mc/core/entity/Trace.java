package mc.core.entity;

import java.util.Date;

import mc.core.Viewable;
import mc.core.util.Data;

/**
 * 请求，应答记录
 * 
 * @author VicTan@qq.com
 *
 */
public class Trace implements Viewable {
	/** 客户端IP */
	public String client;
	/** 通讯端口 */
	public int port;
	/** 请求报文 */
	public byte[] req = new byte[] {};
	/** 应答报文 */
	public byte[] res = new byte[] {};
	/** 客户端链接发启时间 */
	public Date time = new Date();

	@Override
	public String stringify() {
		StringBuilder sb = new StringBuilder();
		sb.append("请求时间：");
		sb.append(Data.toView(time));
		sb.append("\r\n客户端IP：");
		sb.append(client);
		sb.append("\r\n请求端口：");
		sb.append(port);
		sb.append("\r\n请求报文：");
		sb.append(Data.hex(req));
		sb.append("\r\n应答报文：");
		sb.append(Data.hex(res));

		return sb.toString();
	}
}
