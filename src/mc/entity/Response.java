package mc.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

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
	protected List<byte[]> list = new LinkedList<byte[]>();

	public Response() {
	}

	public Response(byte[] origin) {
		this.source = origin;
	}

	public Response add(byte data) {
		return add(new byte[] { data });
	}

	public Response add(byte[] data) {
		list.add(data);
		return this;
	}

	@Override
	public String preview() {
		return Data.hex(source);
	}
}
