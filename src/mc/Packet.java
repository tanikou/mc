package mc;

public abstract class Packet implements Viewable {

	/**
	 * 取得报文原始数据
	 * 
	 * @return
	 */
	public abstract byte[] source();
}
