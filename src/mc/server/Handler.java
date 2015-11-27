package mc.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import mc.Responder;
import mc.Runner;
import mc.entity.Response;
import mc.entity.Trace;

/**
 * 报文处理基类（子类应该run方法中实现对报文的读取，处理，应答）。<br>
 * 基类实现了{@link Runner} 和 {@link Responder}。<br>
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
public abstract class Handler implements Runner, Responder {

	protected Socket socket;
	protected Trace trace = new Trace();
	protected DataInputStream in;
	protected DataOutputStream out;

	@Override
	public Runner setup(Socket socket) {
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
		}
		return this;
	}

	@Override
	public void send(Response res) throws IOException {
		send(res.source());
	}

	@Override
	public void send(byte res) throws IOException {
		send(new byte[] { res });
	}

	@Override
	public void send(byte[] res) throws IOException {
		this.out.write(this.trace.res = res);
	}

	/**
	 * 关闭所有的输入输出流
	 * 
	 * @throws IOException
	 */
	protected void close() throws IOException {
		this.out.close();
		this.in.close();
		this.socket.close();
	}

	/**
	 * 完成 <strong>报文的接受，处理，应答</strong>
	 */
	@Override
	public abstract void run();
}
