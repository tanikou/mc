package mc;

import java.io.Serializable;

import mc.util.Data;

/**
 * 请求报文对象基类（子类用于拆解报文为正常数据）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Request implements Serializable, Viewable {
	protected static final long serialVersionUID = 1L;
	protected byte[] source = new byte[] {};

	public Request(Packet packet) {
		this.source = packet.data();
	}

	@Override
	public String preview() {
		return Data.hex(this.source);
	}
}
