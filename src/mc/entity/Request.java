package mc.entity;

import java.io.Serializable;

import mc.Viewable;
import mc.util.Data;

/**
 * 请求报文对象基类
 * 
 * @author VicTan
 *
 */
public class Request implements Serializable, Viewable {
	protected static final long serialVersionUID = 1L;
	protected byte[] source = new byte[] {};

	public Request(byte[] origin) {
		this.source = origin.clone();
	}

	/**
	 * 通信报文中的原始数据
	 * 
	 * @return
	 */
	public byte[] origin() {
		return this.source.clone();
	}

	@Override
	public String preview() {
		return Data.hex(this.source);
	}
}
