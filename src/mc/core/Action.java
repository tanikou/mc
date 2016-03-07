package mc.core;

import mc.core.entity.Receipt;

/**
 * 业务处理基类（处理并返回应该在子类中实现）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Action {
	protected Responder responder;
	protected Receipt receipt = new Receipt();

	/**
	 * 
	 * @param responder
	 *            {@link Responder}报文应答器
	 */
	public Action(Responder responder) {
		this.responder = responder;
	}
}
