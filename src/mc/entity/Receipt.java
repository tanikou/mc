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
		sb.append(v);
		sb.append("\r\n");
		
		return this;
	}

	public Receipt append(String v, Throwable e) {

		return append(v).append(e.getMessage());
	}

	public Receipt append(Viewable v) {
		return append(v.stringify());
	}

	public String identify() {
		return key;
	}

	public String identify(String key) {
		return this.key = key;
	}

	@Override
	public String stringify() {
		return sb.toString();
	}
}
