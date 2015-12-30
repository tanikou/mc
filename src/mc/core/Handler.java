package mc.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

import mc.core.entity.Trace;
import mc.core.util.Adder;

/**
 * 报文处理基类（子类应该run方法中实现对报文的读取，处理，应答）。<br>
 * 基类继承于{@link Runner} 并实现了 {@link Responder}。<br>
 * 基类对象有<strong>以下属性 供子类使用</strong>
 * <ul>
 * <li>socket 客户端请求{@link Socket}对象</li>
 * <li>trace 处理{@link Trace}对象</li>
 * <li>in 请求的{@link DataInputStream}</li>
 * <li>out 请求的{@link DataOutputStream}</li>
 * </ul>
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Handler extends Runner implements Responder {

	protected Socket socket;
	protected Trace trace = new Trace();
	protected DataInputStream in;
	protected DataOutputStream out;

	@Override
	public Runner setup(Socket socket) {
		try {
			this.socket = socket;
			this.trace.port = socket.getLocalPort();
			this.trace.client = socket.getInetAddress().getHostAddress();
			this.in = new DataInputStream(this.socket.getInputStream());
			this.out = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			this.close();
		}
		return this;
	}

	@Override
	public void send(Packet res) throws IOException {
		send(res.source());
	}

	@Override
	public void send(Adder adder) throws IOException {
		send(adder.source());
	}

	@Override
	public void send(byte res) throws IOException {
		send(new byte[] { res });
	}

	@Override
	public void send(byte[] res) throws IOException {
		this.out.write(this.trace.res = res);
		this.out.flush();
	}

	@Override
	public void doSendAnObjectAndDoNotTrace(Serializable data)
			throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(this.out);
		// 返回应答
		oout.writeObject(data);
		oout.flush();
	}

	/**
	 * 关闭所有的输入输出流
	 * 
	 * @throws IOException
	 */
	protected void close() {
		try {
			this.out.close();
		} catch (Exception e) {
		}
		try {
			this.in.close();
		} catch (Exception e) {
		}
		try {
			this.socket.close();
		} catch (Exception e) {
		}
	}

	/**
	 * 完成 <strong>报文的接受，处理，应答</strong>
	 */
	@Override
	public abstract void run();
}
