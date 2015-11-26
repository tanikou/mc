package mc;

/**
 * 报文接口（不同报文的加密解密需要在实现中自己处理）
 * 
 * @author VicTan
 *
 */
public interface Packet extends Viewable {
	/**
	 * 取得报文原始数据
	 * 
	 * @return
	 */
	public byte[] source();

	public Packet source(byte[] ary);
}
