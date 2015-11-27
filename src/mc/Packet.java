package mc;

import mc.util.Data;

/**
 * 报文基类（不同报文的加密解密需要在实现中自己处理）<br>
 * 用于第一次处理接受到的报文（可能是多重报文，若多重报文提取有效数据需要在子类中实现）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Packet implements Viewable {
	protected byte[] source;

	/**
	 * 取得报文原始数据
	 * 
	 * @return
	 */
	public byte[] source() {
		return this.source;
	}

	/**
	 * 
	 * @param ary
	 *            报文原始数据
	 * @return
	 */
	public Packet source(byte[] ary) {
		this.source = ary.clone();
		return this;
	}

	@Override
	public String preview() {
		return Data.hex(this.source);
	}

	/**
	 * 取得报文包中的有效数据（子类做不同的解密处理）
	 * 
	 * @return 有效数据
	 */
	public abstract byte[] data();

	/**
	 * 设定报文中的有效数据（子类做不同的加密处理）
	 * 
	 * @param ary
	 */
	public abstract Packet data(byte[] ary);
}
