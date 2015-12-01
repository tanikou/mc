package mc.entity;

import java.util.concurrent.atomic.AtomicInteger;

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
		return source;
	}

	/**
	 * 取得对应的下发命令（不增加阅读次数）
	 * 
	 * @return
	 */
	public byte[] source() {
		return source;
	}

	/**
	 * 是否已经阅读
	 * 
	 * @return
	 */
	public boolean isRead() {
		return times.get() >= 3;
	}
}
