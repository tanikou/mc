package mc;

import java.io.Serializable;

import mc.util.Data;

/**
 * 报文应答对象基类（子类用于组装正常数据为报文）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Response implements Serializable, Viewable {
	private static final long serialVersionUID = 1L;
	protected byte[] source;

	public Response(byte[] origin) {
		this.source = origin.clone();
	}

	@Override
	public String preview() {
		return Data.hex(source);
	}

	public abstract Packet packet();
}
