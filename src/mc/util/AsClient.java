package mc.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * 客户端模拟器
 * 
 * @author VicTan@qq.com
 *
 */
public class AsClient implements Callable<byte[]> {
	private byte[] packet;
	private Socket socket;
	private DataInputStream inn;
	private DataOutputStream out;
	private boolean closeable = false;

	public AsClient(String ip, int port) throws Exception {
		this(new Socket(ip, port));
		closeable = true;
	}

	public AsClient(Socket socket) {
		try {
			this.socket = socket;
			this.socket.setSoTimeout(30000);
			this.inn = new DataInputStream(new BufferedInputStream(
					this.socket.getInputStream()));
			this.out = new DataOutputStream(new BufferedOutputStream(
					this.socket.getOutputStream()));
		} catch (Exception e) {
			try {
				this.close();
			} catch (IOException ex) {
				e.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public AsClient timeout(int time) {
		try {
			this.socket.setSoTimeout(time);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * 发送已经包装好的bytes给服务器
	 * 
	 * @param bytes
	 * @return
	 */
	public AsClient send(byte[] bytes) {
		this.packet = bytes;
		return this;
	}

	@Override
	public byte[] call() throws Exception {
		try {
			return this.doRun();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				this.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private byte[] doRun() throws IOException {

		// 发送数据
		out.write(packet);
		out.flush();
		// 接受返回数据
		byte[] res = new byte[1024];
		int len = inn.read(res);

		return Arrays.copyOfRange(res, 0, len);
	}

	/**
	 * 关闭由此类所创建的所有流或链接
	 * 
	 * @throws IOException
	 */
	private void close() throws IOException {
		this.out.close();
		this.inn.close();
		if (closeable) {
			this.socket.close();
		}
	}
}
