package mc.entity;

import java.util.Date;

import mc.Identity;
import mc.Viewable;
import mc.util.Data;

/**
 * 请求，应答记录
 * 
 * @author VicTan@qq.com
 *
 */
public class Trace implements Viewable {

	public String client;
	public int port;

	public Identity identity = new Identity();
	/** 请求报文 */
	public byte[] req = new byte[] {};
	/** 应答报文 */
	public byte[] res = new byte[] {};
	/**
	 * 客户端链接发启时间
	 */
	public Date time = new Date();

	@Override
	public String preview() {
		StringBuilder sb = new StringBuilder();
		sb.append("请求时间：");
		sb.append(Data.toView(time));
		sb.append("\r\n请求IP：");
		sb.append(client);
		sb.append("\r\n端口号：");
		sb.append(port);
		sb.append("\r\n报文标识：");
		sb.append(identity.identify());
		sb.append("\r\n请求报文：");
		sb.append(Data.hex(req));
		sb.append("\r\n应答报文：");
		sb.append(Data.hex(res));

		return sb.toString();
	}
}
