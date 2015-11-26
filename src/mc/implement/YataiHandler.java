package mc.implement;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import mc.Handler;
import mc.Packet;
import mc.Responder;
import mc.dao.DB;
import mc.entity.Receipt;
import mc.entity.Response;
import mc.entity.Trace;
import mc.util.Data;

import org.apache.log4j.Logger;

public class YataiHandler implements Handler, Responder {
	private Logger logger = Logger.getLogger(YataiHandler.class);

	private Socket socket;
	private Trace trace = new Trace();
	private DataInputStream in;
	private DataOutputStream out;
	private Receipt receipt = new Receipt();

	@Override
	public Handler setup(Socket socket) {
		try {
			this.socket = socket;
			this.trace.client = socket.getInetAddress().getHostAddress();
			this.in = new DataInputStream(new BufferedInputStream(
					this.socket.getInputStream()));
			this.out = new DataOutputStream(new BufferedOutputStream(
					this.socket.getOutputStream()));
		} catch (Exception e) {
			try {
				this.close();
			} catch (IOException ex) {
			}
			logger.warn("处理报文失败", e);
		}
		return this;
	}

	@Override
	public void run() {
		try {
			this.run();
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

	public void doRun() throws Exception {
		// 读取报文
		this.read();
		// 解析 报文 中的 通讯数据包(第二层报文)
		Packet packet = new YataiPacket().source(trace.req);

		this.receipt.append(new YataiAction(this).handle(packet));

		logger.debug("YataiHandler处理结果回执：" + this.receipt.preview());
	}

	/**
	 * 关闭由此类所创建的所有流或链接
	 * 
	 * @throws IOException
	 */
	private void close() throws IOException {
		this.out.close();
		this.in.close();
		this.socket.close();
	}

	@Override
	public void send(Response res) throws IOException {
		// 返回应答
		this.out.write(res.source());
		this.out.flush();
	}
}
