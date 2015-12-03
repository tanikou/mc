package mc.entity;

import java.util.concurrent.atomic.AtomicInteger;

import mc.util.Data;

/**
 * 下发命令通知，默认读取三次则示客户端已经取得当前通知<br>
 * （当确认已经客户端收到通知后应该从命令队列进移除此通知）
 * 
 * @author VicTan@qq.com
 *
 */
public class Notice {
	private AtomicInteger times = new AtomicInteger(0);
	private byte[] source;

	/**
	 * 下载命令内容
	 * 
	 * @param o
	 */
	public Notice(byte o) {
		this(new byte[] { o });
	}

	/**
	 * 下载命令内容
	 * 
	 * @param o
	 */
	public Notice(byte[] o) {
		this.source = o;
	}

	/**
	 * 阅读一次
	 * 
	 * @return
	 */
	public byte[] read() {
		times.incrementAndGet();
		return source.clone();
	}

	/**
	 * 取得对应的下发命令（不增加阅读次数）
	 * 
	 * @return
	 */
	public byte[] source() {
		return source.clone();
	}

	/**
	 * 是否已经阅读
	 * 
	 * @return
	 */
	public boolean isRead() {
		return times.get() >= 3;
	}

	/**
	 * 判断下发命令是否和另另一个下发命令相同
	 * 
	 * @param notice
	 *            {@link Notice}
	 * @return
	 */
	public boolean equal(Notice notice) {
		return Data.equal(this.source(), notice.source());
	}

	/**
	 * 判断下发命令是否和另另一个下发命令相同<br>
	 * 只判断第一个byte（单一命令字）是否相等
	 * 
	 * @param b
	 * @return
	 */
	public boolean equal(byte b) {
		return b == source[0];
	}
}
