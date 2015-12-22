package mc.impl.debug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import mc.Handler;
import mc.Response;
import mc.dao.Shared;
import mc.exception.AppException;
import mc.util.Data;

/**
 * 接受并分终端记录调试报文
 * 
 * @author VicTan@qq.com
 *
 */
public class DebugHandler extends Handler {
	public static final byte Head = (byte) 0xDB;

	@Override
	public void run() {
		try {
			this.socket.setSoTimeout(DebugApp.timeout);
			this.doRun();
			log(trace.stringify());
		} catch (Exception e) {
			e.printStackTrace();

			log(e.getMessage() + "\r\n" + trace.stringify());
		}
		this.close();
	}

	private void doRun() throws Exception {
		List<Byte> list = new LinkedList<Byte>();
		try {
			byte[] ary = new byte[2];
			list.add(this.in.readByte());
			for (int i = 0; i < ary.length; i++) {
				list.add(ary[i] = this.in.readByte());
			}
			int len = Data.bcd2int(ary);
			for (int i = 0; i < len; i++) {
				list.add(this.in.readByte());
			}
		} catch (Exception ex) {
			trace.time = Shared.getSystemTime();
			trace.req = Data.bytes(list);
			throw new AppException("读取报文数据发生异常，已读数据长度：" + list.size());
		}

		trace.time = Shared.getSystemTime();
		trace.req = Data.bytes(list);

		this.send(new byte[] { trace.req[0], Response.OK });
	}

	private void log(String text) {
		try {
			write(new File(DebugApp.folder + trace.client + ".log"), text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write(File file, String text) throws IOException {
		System.out.println(text);
		if (false == file.exists()) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file, true);
		writer.append(text);
		writer.append("\r\n\r\n");
		writer.close();
	}
}
