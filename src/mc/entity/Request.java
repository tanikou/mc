package mc.entity;

import java.io.Serializable;

import mc.Viewable;
import mc.util.Data;

/**
 * 请求报文对象基类
 * 
 * @author VicTan@qq.com
 *
 */
public class Request implements Serializable, Viewable {
	protected static final long serialVersionUID = 1L;
	protected byte[] source = new byte[] {};

	public Request(byte[] origin) {
		this.source = origin.clone();
	}

	public byte[] source() {
		return this.source.clone();
	}

	@Override
	public String preview() {
		return Data.hex(this.source);
	}
}
