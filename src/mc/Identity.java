package mc;

/**
 * 请求报文标识
 * 
 * @author VicTan@qq.com
 *
 */
public class Identity {
	private String key = null;

	public String identify() {
		return key;
	}

	public Identity identify(String key) {
		this.key = key;
		return this;
	}
}
