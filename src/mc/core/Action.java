package mc.core;

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
}
