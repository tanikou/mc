package mc.dao;

import java.util.Date;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import mc.entity.Notice;
import mc.util.Const;

import org.apache.log4j.Logger;

/**
 * 数据操作
 * 
 * @author VicTan@qq.com
 *
 */
public class DB {
	private static Logger logger = Logger.getLogger(DB.class);

	/** 整个环境只允许有一个DB，所有的缓存数据访问可db来处理 */
	private static DB db;
	/** 此次班别中所有连接过的终端机编号 如果不使用{@link ConcurrentHashMap}在定时多线程定时任务中会出错 */
	private Map<String, Date> actived = new ConcurrentHashMap<String, Date>();
	/** 下发命令队列 */
	private Map<String, Queue<Notice>> notice = new ConcurrentHashMap<String, Queue<Notice>>();

	// 及时初始化，可以直接使用而不需要像单例一样先get再使用
	static {
		db = new DB();

		// 定时任务，开始
		TimerTask task = new TimerTask() {
			public void run() {
				for (String key : db.actived.keySet()) {
					logger.trace("计算" + key + "是否离线");
				}
			}
		};
		new Timer().schedule(task, Const.losttime, Const.losttime);
		// 定时任务，结束
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
	 * @param physical
	 *            终端编号
	 * @param o
	 *            命令字。
	 */
	public static void broadcast(String physical, Notice o) {
		Queue<Notice> queue = db.notice.get(physical);
		if (null == queue) {
			queue = new ConcurrentLinkedQueue<Notice>();
			db.notice.put(physical, queue);
		}
		queue.add(o);
	}

	/**
	 * 取消下发指令
	 * 
	 * @param physical
	 *            终端编号
	 * @param o
	 *            需要取消的下发命令
	 * @return 如果第一个下发指令是参数所指定的下发命令则<strong>移除</strong>并返回true，否则false
	 */
	public static boolean unbroadcast(String physical, Notice o) {
		Queue<Notice> queue = db.notice.get(physical);
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
	 * 判断终端是否有下发命令
	 * 
	 * @param physical
	 *            终端编号
	 * @return
	 */
	public static boolean isNoticed(String physical) {
		Queue<Notice> queue = db.notice.get(physical);
		if (null == queue) {
			return false;
		}
		return queue.size() > 0;
	}

	/**
	 * 取得队列中最前面的下发命令
	 * 
	 * @param physical
	 *            终端编号
	 * @return 下发命令，如果没有下发命令则为<strong>null</strong>
	 */
	public static Notice notice(String physical) {
		Queue<Notice> queue = db.notice.get(physical);
		return null == queue ? null : queue.peek();
	}
}
