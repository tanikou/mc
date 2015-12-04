package mc.impl.file;

import mc.Action;
import mc.Packet;
import mc.Responder;
import mc.entity.Receipt;
import mc.util.Adder;

public class FileAction extends Action {
	private Receipt receipt = new Receipt();

	public FileAction(Responder responder) {
		super(responder);
	}

	/**
	 * 分发业务
	 * 
	 * @param packet
	 *            {@link Packet}
	 * @return
	 * @throws Exception
	 */
	public Receipt dispatch(Packet packet) throws Exception {
		receipt.append("FileAction 开始处理报文");
		Adder adder = new Adder().add(new byte[] { 0x09, 0x08 });
		responder.send(new FilePacket().data(adder.source()));
		receipt.append("FileAction 处理报文结束");

		return receipt;
	}

}
