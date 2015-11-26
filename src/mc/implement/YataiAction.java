package mc.implement;

import mc.Action;
import mc.Packet;
import mc.Responder;
import mc.entity.Receipt;

public class YataiAction extends Action {
	private Receipt receipt = new Receipt();

	public YataiAction(Responder responder) {
		super(responder);
	}

	@Override
	public Receipt handle(Packet packet) throws Exception {
		receipt.append("YataiAction 开始处理报文");
		// responder.send(null);
		receipt.append("YataiAction 处理报文结束");
		
		return receipt;
	}

}
