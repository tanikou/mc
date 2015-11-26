package mc.entity;

import java.io.Serializable;

import mc.Viewable;
import mc.util.Data;

/**
 * 报文应答对象基类
 * 
 * @author VicTan@qq.com
 *
 */
public class Response implements Serializable, Viewable {
	private static final long serialVersionUID = 1L;
	protected byte[] source;

	public Response(byte[] origin) {
		this.source = origin.clone();
	}

	public byte[] source() {
		return this.source.clone();
	}

	@Override
	public String preview() {
		return Data.hex(source);
	}
}
