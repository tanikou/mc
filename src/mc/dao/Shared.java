package mc.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

import mc.entity.Instant;
import mc.entity.Notice;
import mc.util.Const;

import org.apache.log4j.Logger;

/**
 * 共享数据操作
 * 
 * @author VicTan@qq.com
 *
 */
public class Shared {
	private static Logger logger = Logger.getLogger(Shared.class);

	/** 整个环境只允许有一个DB，所有的缓存数据访问可db来处理 */
	private static Shared db;
	/** 在线的终端列表 如果不使用{@link ConcurrentHashMap}在定时多线程定时任务中会出错 */
	private Map<String, Date> activated = new ConcurrentHashMap<String, Date>();
	/** 下发命令队列 */
	private Map<String, Queue<Notice>> notice = new ConcurrentHashMap<String, Queue<Notice>>();
	/** 所有的终端列表 */
	private Set<String> clients = new ConcurrentSkipListSet<String>();
	/** 临时信息列表 */
	private Map<String, Instant> instant = new ConcurrentHashMap<String, Instant>();
	private String losttime = Const.prop("lost.time");

	// 及时初始化，可以直接使用而不需要像单例一样先get再使用
	static {
		db = new Shared();

		// 定时任务，开始
		TimerTask task = new TimerTask() {
			public void run() {
				StringBuilder sb = new StringBuilder();
				sb.append("\r\n开始 检测终端在线状态");
				int max = Integer.parseInt(db.losttime) * 1000;
				long time = getSystemTime().getTime();
				try {
					Set<String> actived = db.activated.keySet();
					Set<String> alive = new HashSet<String>();
					// 检测上一次连接中失联的终端
					for (String term : actived) {
						if (time - db.activated.get(term).getTime() > max) {
							sb.append("\r\n终端：").append(term).append("失去链接");
							db.activated.remove(term);

							// 更新即时信息
							db.instant.put(term, Instant.Offline);
						} else {
							alive.add(term);
						}
					}
					// 输入一直失去链接的终端
					for (String term : db.clients) {
						if (false == alive.contains(term)) {
							sb.append("\r\n终端：").append(term).append("失去链接");
						}
					}
					// 输出在线的终端
					for (String term : alive) {
						sb.append("\r\n终端：").append(term).append("链接正常");
					}
				} catch (Exception e) {
					logger.warn("检测在线状态异常", e);
				}
				sb.append("\r\n结束 检测终端在线状态");
				logger.trace(sb);
			}
		};
		if (null != db.losttime && db.losttime.matches("[0-9]+")) {
			int time = Integer.parseInt(db.losttime) * 1000;
			new Timer().schedule(task, time, time);
		} else {
			logger.debug("超时时间配置不正确，跳过定时任务设定");
		}
		// 定时任务，结束
	}

	/**
	 * 不允许外部实例化
	 */
	private Shared() {
	}

	/**
	 * 取得系统时间
	 * 
	 * @return {@link Date}
	 */
	public static Date getSystemTime() {
		return new Date();
	}

	/**
	 * 取对指定终端机的下发命令
	 * 
	 * @param client
	 *            终端编号
	 * @param o
	 *            命令字。
	 */
	public static boolean broadcast(String client, byte o) {
		Queue<Notice> queue = db.notice.get(client);
		if (null == queue) {
			queue = new ConcurrentLinkedQueue<Notice>();
			db.notice.put(client, queue);
		}
		queue.add(new Notice(o));
		return true;
	}

	/**
	 * 取对指定终端机的下发命令
	 * 
	 * @param client
	 *            终端编号
	 * @param o
	 *            命令字。
	 */
	public static boolean broadcast(String client, Notice o) {
		Queue<Notice> queue = db.notice.get(client);
		if (null == queue) {
			queue = new ConcurrentLinkedQueue<Notice>();
			db.notice.put(client, queue);
		}
		queue.add(o);
		return true;
	}

	/**
	 * 取消下发指令
	 * 
	 * @param client
	 *            终端编号
	 * @param o
	 *            需要取消的下发命令
	 * @return 如果第一个下发指令是参数所指定的下发命令则<strong>移除</strong>并返回true，否则false
	 */
	public static boolean unbroadcast(String client, Notice o) {
		Queue<Notice> queue = db.notice.get(client);
		if (null == queue || 0 == queue.size()) {
			return false;
		}
		Notice notice = queue.peek();
		if (null == notice) {
			return false;
		} else if (notice.equal(o)) {
			queue.poll();
			return true;
		}
		return false;
	}

	/**
	 * 取消下发指令
	 * 
	 * @param client
	 *            终端编号
	 * @param o
	 *            需要取消的下发命令
	 * @return 如果第一个下发指令是参数所指定的下发命令则<strong>移除</strong>并返回true，否则false
	 */
	public static boolean unbroadcast(String client, byte o) {
		Queue<Notice> queue = db.notice.get(client);
		if (null == queue || 0 == queue.size()) {
			return false;
		}
		Notice notice = queue.peek();
		if (null == notice) {
			return false;
		} else if (notice.equal(o)) {
			queue.poll();
			return true;
		}
		return false;
	}

	/**
	 * 取得队列中最前面的下发命令
	 * 
	 * @param client
	 *            终端编号
	 * @return 下发命令，如果没有下发命令则为<strong>null</strong>
	 */
	public static Notice notification(String client) {
		Queue<Notice> queue = db.notice.get(client);
		if (null == queue) {
			return null;
		}
		Notice notice = queue.peek();
		if (notice.isRead()) {
			queue.poll();// 删除已经下发完成的命令
			return queue.peek();// 重新取下一条下发命令
		}
		return notice;
	}

	/**
	 * 注册客户端
	 * 
	 * @param client
	 *            客户端编号（商户号+终端号）
	 * @return
	 */
	public static boolean register(String client) {
		db.notice.put(client, new ConcurrentLinkedQueue<Notice>());
		db.instant.put(client, Instant.Offline);

		return db.clients.add(client);
	}

	/**
	 * 设定某终端的时实状态
	 * 
	 * @param client
	 *            终端编号
	 * @param instant
	 *            状态
	 */
	public static void instant(String client, Instant instant) {
		db.instant.put(client, instant);
	}

	/**
	 * 取得所有即时状态
	 */
	public static Map<String, Instant> instant() {
		return db.instant;
	}

	/**
	 * 取得当前在线终端列表
	 * 
	 * @return
	 */
	public static Map<String, Date> activated() {
		return db.activated;
	}

	/**
	 * 刷新终端的最新链接时间
	 * 
	 * @param client
	 *            终端编号
	 */
	public static void heartbeat(String client) {
		db.activated.put(client, getSystemTime());
	}
}
