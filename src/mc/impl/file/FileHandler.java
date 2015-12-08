package mc.impl.file;

import mc.Handler;
import mc.Packet;
import mc.dao.Shared;
import mc.entity.Receipt;
import mc.entity.Trace;
import mc.util.Data;

import org.apache.log4j.Logger;

public class FileHandler extends Handler {
	private Logger logger = Logger.getLogger(FileHandler.class);

	private Receipt receipt = new Receipt();

	@Override
	public void run() {
		try {
			this.doRun();
		} catch (Throwable e) {
			e.printStackTrace();
			receipt.append("处理报文出错，报文内容--" + Data.hex(trace.req));
			receipt.append("处理报文出错，错误信息：", e);
		} finally {
			this.close();
		}
		receipt.append(trace.preview());

		logger.debug("处理报文结束，处理过程信息：" + receipt.preview());
	}

	private Trace read() throws Exception {
		receipt.append("开始读取报文内容");

		this.trace.time = Shared.getSystemTime();
		this.trace.req = new byte[] { this.in.readByte() };

		return this.trace;
	}

	private void doRun() throws Exception {
		// 读取报文
		this.read();
		// 解析 报文 中的 通讯数据包(第二层报文)
		Packet packet = new FilePacket().source(trace.req);

		this.receipt.append(new FileAction(this).dispatch(packet));
	}
}
