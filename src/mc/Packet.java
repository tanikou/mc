package mc;

/**
 * 报文接口
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
}
