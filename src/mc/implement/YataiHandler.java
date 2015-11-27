package mc.implement;

import java.io.IOException;

import mc.Packet;
import mc.dao.DB;
import mc.entity.Receipt;
import mc.entity.Trace;
import mc.server.Handler;
import mc.util.Data;

import org.apache.log4j.Logger;

public class YataiHandler extends Handler {
	private Logger logger = Logger.getLogger(YataiHandler.class);

	private Receipt receipt = new Receipt();

	@Override
	public void run() {
		try {
			this.doRun();
		} catch (Throwable e) {
			// e.printStackTrace();
			receipt.append("处理报文出错，报文内容--" + Data.hex(trace.req));
			receipt.append("处理报文出错，错误信息：", e);
		} finally {
			try {
				this.close();
			} catch (IOException e) {
			}
		}

		logger.debug("处理报文结束，处理过程信息：" + receipt.preview());
	}

	private Trace read() throws Exception {
		receipt.append("开始读取报文内容");

		this.trace.time = DB.getSystemTime();
		this.trace.req = new byte[] { this.in.readByte() };

		return this.trace;
	}

	private void doRun() throws Exception {
		// 读取报文
		this.read();
		// 解析 报文 中的 通讯数据包(第二层报文)
		Packet packet = new YataiPacket().source(trace.req);

		this.receipt.append(new YataiAction(this).handle(packet));
	}
}
