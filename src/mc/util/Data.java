package mc.util;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据转换工具
 * 
 * @author VicTan@qq.com
 *
 */
public class Data {

	/**
	 * byte数组转BCD字符串
	 * 
	 * @param ary
	 * @return
	 */
	public static String bcd(byte[] ary) {
		return bcd2str(ary);
	}

	/**
	 * byte数组转BCD字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static String bcd2str(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.charAt(0) == 0 ? temp.substring(1) : temp.toString();
	}

	/**
	 * byte数组转int
	 * 
	 * @param ary
	 * @return
	 */
	public static int bcd2int(byte[] ary) {
		int v = 0;
		for (int i = 0; i < ary.length; i++) {
			v = (v * 100) + (((ary[i] & 0xFF) >> 4) * 10) + (ary[i] & 0x0F);
		}
		return v;
	}

	/**
	 * 将 byte数据 转换为对应的 hex字符号<br>
	 * 如：[0x00, 0x01, x02] -> "00 01 02"
	 * 
	 * @param ary
	 * @return
	 */
	public static String hex(byte[] ary) {
		return null == ary ? null : hex(ary, ' ');
	}

	public static String hex(byte[] ary, char ch) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ary.length; i++) {
			sb.append(ch);

			String hex = Integer.toHexString(ary[i] & 0xFF);
			if (hex.length() == 1) {
				sb.append('0').append(hex);
			} else {
				sb.append(hex);
			}
		}
		return sb.length() > 0 ? sb.substring(1) : sb.toString();
	}

	public static String hex(byte b) {
		return Integer.toHexString(b & 0xFF);
	}

	/**
	 * 将所有1位的hex位全部转成正常两位的hex <br>
	 * 如：12 2 -> 12 02
	 * 
	 * @param v
	 * @return
	 */
	public static String hex(String hex) {
		StringBuilder sb = new StringBuilder();
		String[] ary = hex.replaceAll("^ +| +$", "").split(" ");
		for (String v : ary) {
			sb.append(' ');
			if (1 == v.length()) {
				sb.append('0');
			}
			sb.append(v);
		}

		return sb.substring(1);
	}

	/**
	 * 将 int 转换为 byte数组
	 * 
	 * @param i
	 *            数字
	 * @param l
	 *            默认数据长度
	 * @return
	 */
	public static byte[] bytes(int i, int len) {
		byte[] ary = new byte[len];
		for (int j = 0; j < len; j++) {
			ary[len - j - 1] = (byte) (i >> (j * 8));
		}
		return ary;
	}

	/**
	 * 字符串转BCD码
	 * 
	 * @param 需要转换的字符串
	 * @return 转换后的BCD码
	 */
	public static byte[] bcd(String v) {
		if (v.length() % 2 != 0) {
			v = "0" + v;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = v.toCharArray();
		for (int i = 0; i < cs.length; i += 2) {
			int high = cs[i] - 48;
			int low = cs[i + 1] - 48;
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	/**
	 * 数字转BCD码
	 * 
	 * @param 需要转换的数字
	 * @param 转换后BCD码数组的长度
	 * @return 转换后的BCD码
	 */
	public static byte[] bcd(int v, int len) {
		byte[] ary = new byte[len];
		int temp;
		for (int j = len - 1; j >= 0; j--) {
			temp = v % 100;
			ary[j] = (byte) (((temp / 10) << 4) + ((temp % 10) & 0x0F));
			v /= 100;
		}
		return ary;
	}

	/**
	 * 将 char 转换为 byte
	 * 
	 * @param c
	 * @return
	 */
	public static byte asHex(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 将 16进制的<strong>hex</strong>字符串 还原为 <strong>byte</strong>数组<br>
	 * 如：<strong>"00 01 02"</strong> -> <strong> [0x00, 0x01, 0x02]</strong><br>
	 * <strong>hex</strong>字符串中如果有空格将被去掉，并连续两位认为是一个byte
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] asHex(String hex) {
		if (hex == null || false == hex.matches("[0-9ABCDEFabcdef ]{2,}")) {
			return null;
		}
		hex = hex.replaceAll(" ", "").toUpperCase();
		int length = hex.length() / 2;
		char[] chs = hex.toCharArray();
		byte[] ary = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			ary[i] = (byte) (asHex(chs[pos]) << 4 | asHex(chs[pos + 1]));
		}
		return ary;
	}

	/**
	 * 将 <strong>byte</strong> 还原为 <strong>int</strong>
	 * 
	 * @param b
	 * @return
	 */
	public static int toInt(byte[] b) {
		return toInt(b, 0, b.length);
	}

	public static int toInt(byte[] bytes, int off, int len) {
		int v = 0;
		int pos = off + len - 1;
		if (pos >= 0) {
			v |= (bytes[pos] & 0xFF);
		}
		pos--;
		if (pos >= 0) {
			v |= ((bytes[pos] << 8) & 0xFF00);
		}
		pos--;
		if (pos >= 0) {
			v |= ((bytes[pos] << 16) & 0xFF0000);
		}
		pos--;
		if (pos >= 0) {
			v |= ((bytes[pos] << 24) & 0xFF000000);
		}
		return v;
	}

	public static int toInt(String v) {
		return null == v ? 0 : Integer.parseInt(v);
	}

	/**
	 * 判断 <strong>big</strong> 中是否包含有 <strong> small </strong>
	 * 
	 * @param big
	 * @param small
	 * @return
	 */
	public static boolean has(byte[] big, byte[] small) {
		boolean has = false;
		for (int i = 0; i < big.length; i++) {
			if (big[i] == small[0]) {
				for (int k = 0; k < small.length; k++) {
					if (big[i + k] != small[k]) {
						break;
					}
					if (k == small.length - 1) {
						return true;
					}
				}
			}
		}
		return has;
	}

	public static String lpad(int v, char c, int l) {
		return lpad(v + "", c, l);
	}

	/**
	 * 用字符c将字符串v补位到长度为l
	 * 
	 * @param v
	 * @param c
	 * @param l
	 * @return
	 */
	public static String lpad(String v, char c, int l) {
		StringBuilder sb = new StringBuilder(l);
		v = null == v ? "" : v;
		for (int i = v.length(); i < l; i++) {
			sb.append(c);
		}
		sb.append(v);
		return sb.toString();
	}

	public static String toView(Date time) {
		return toView(time, "yyyy.MM.dd HH:mm:ss");
	}

	public static String toView(Date time, String format) {
		return new SimpleDateFormat(format).format(time);
	}

	/**
	 * 判断两个byte数组内容是否完全一致
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equal(byte[] a, byte[] b) {
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param v
	 * @return
	 */
	public static boolean isBlank(String v) {
		return null == v || "".equals(v);
	}
}
