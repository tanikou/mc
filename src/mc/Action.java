package mc;

import mc.entity.Trace;

/**
 * 业务处理基类（处理并返回应该在子类中实现）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Action {
	protected Responder responder;

	/**
	 * 
	 * @param responder
	 *            {@link Responder}报文应答器
	 */
	public Action(Responder responder) {
		this.responder = responder;
	}

	/**
	 * 根据报文处理具体业务
	 * 
	 * @param packet
	 *            {@link Packet}接受到的报文数据
	 * @return {@link Trace}
	 */
	public abstract Trace handle(Packet packet);
}
