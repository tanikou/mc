package mc.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 按顺序数据截取
 * 
 * @author VicTan@qq.com
 *
 */
public class Cutter {
	List<Byte> source = new LinkedList<Byte>();

	public Cutter(byte[] data) {
		for (byte b : data) {
			source.add(b);
		}
	}

	/**
	 * 截取下一段数据
	 * 
	 * @param len
	 *            下一段数据长度
	 * @return
	 */
	public byte[] next(int len) {
		byte[] ary = new byte[len];
		for (int i = 0; i < len; i++) {
			ary[i] = source.remove(0);
		}
		return ary;
	}
}
