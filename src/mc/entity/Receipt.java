package mc.entity;

import mc.Viewable;

/**
 * 处理过程中的回执信息
 * 
 * @author VicTan@qq.com
 *
 */
public class Receipt implements Viewable {
	private String key;
	private StringBuilder sb = new StringBuilder();

	public Receipt append(String v) {
		sb.append("\r\n");
		sb.append(v);

		return this;
	}

	public Receipt append(String v, Throwable e) {

		return append(v).append(e.getMessage());
	}

	public Receipt append(Receipt v) {
		return append(v.preview());
	}

	public String identify() {
		return key;
	}

	public Receipt identify(String key) {
		this.key = key;
		return this;
	}

	@Override
	public String preview() {
		return sb.toString();
	}
}