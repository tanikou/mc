package mc.entity;

import java.util.Date;

/**
 * 请求，应答记录
 * 
 * @author VicTan@qq.com
 *
 */
public class Trace {

	public String client;
	public int port;

	/** 请求报文 */
	public byte[] req = new byte[] {};
	/** 应答报文 */
	public byte[] res = new byte[] {};
	/**
	 * 客户端链接发启时间
	 */
	public Date time = new Date();
}
