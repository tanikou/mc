package mc.core.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 简单报文构造器
 * 
 * @author VicTan@qq.com
 *
 */
public class Adder {
	protected List<byte[]> list = new LinkedList<byte[]>();

	/**
	 * 添加到头部
	 * 
	 * @param data
	 * @return
	 */
	public Adder preadd(byte data) {
		list.add(0, new byte[] { data });
		return this;
	}

	/**
	 * 添加到头部
	 * 
	 * @param data
	 * @return
	 */
	public Adder preadd(byte[] data) {
		list.add(0, data);
		return this;
	}

	/**
	 * 添加到结尾
	 * 
	 * @param data
	 * @return
	 */
	public Adder add(byte data) {
		return add(new byte[] { data });
	}

	/**
	 * 添加到结尾
	 * 
	 * @param data
	 * @return
	 */
	public Adder add(byte[] data) {
		list.add(data);
		return this;
	}

	public byte[] source() {
		int i = 0;
		for (byte[] ary : list) {
			i += ary.length;
		}
		byte[] data = new byte[i];
		i = 0;
		for (byte[] ary : list) {
			for (byte b : ary) {
				data[i++] = b;
			}
		}
		return data;
	}
}
