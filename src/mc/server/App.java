package mc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mc.Handler;
import mc.Runner;
import mc.dao.Shared;
import mc.entity.Notice;
import mc.util.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 卡机联动管控程序主服务（各协议的实现可以使用<strong>多线程监听不同的端口</strong>）
 * 
 * @author VicTan@qq.com
 */
public class App implements Runnable {
	private static Log logger = LogFactory.getLog(App.class);

	/** 用于 多端口服务 中停止本端口服务 */
	private boolean isSingleRunning = true;
	/** 用于 多端口服务 中记录各端口号 */
	private int iPort = 8008;
	private int iMaxThread = 50;
	/** 用于客户端报文请求的<strong>读取，解析，处理，应答</strong> */
	private Class<? extends Runner> iRunner;

	/**
	 * 使用默认端口8008开启服务
	 * 
	 * @param runner
	 *            用来读取，转换，处理报文的类。{@link Runner} 或者 {@link Handler}的子类
	 */
	public App(Class<? extends Runner> runner) {
		this.iRunner = runner;
	}

	/**
	 * 
	 * @param runner
	 *            用来读取，转换，处理报文的类。{@link Runner} 或者 {@link Handler}的子类
	 * @param port
	 *            监听端口
	 */
	public App(Class<? extends Runner> runner, int port) {
		this.iRunner = runner;
		this.iPort = port;
	}

	/**
	 * 取得端口号
	 * 
	 * @param port
	 * @return
	 */
	public int getServerPort(int port) {
		return this.iPort;
	}

	/**
	 * 设置本服务线程池中的最大处理线程数
	 * 
	 * @param max
	 * @return
	 */
	public App setMaxThread(int max) {
		this.iMaxThread = max;
		return this;
	}

	/**
	 * 停止当前服务
	 */
	public void stop() {
		this.isSingleRunning = false;
	}

	@Override
	public void run() {
		try {
			doRun();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("使用端口" + iPort + "开启服务失败", e);
		}
	}

	private void doRun() throws IOException {
		ExecutorService exer = Executors.newFixedThreadPool(iMaxThread);
		ServerSocket server = new ServerSocket(iPort);
		logger.info(Data.toView(Shared.getSystemTime()) + "启动服务，端口：" + iPort);

		while (isSingleRunning) {
			try {
				exer.submit(iRunner.newInstance().setup(server.accept()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		server.close();
		exer.shutdown();
		logger.info(Data.toView(Shared.getSystemTime()) + "停止服务，端口：" + iPort);
	}

	/************************************* 以下代码全部调用Shared对象 *************************************/
	/**
	 * 取对指定终端机的下发命令
	 * 
	 * @param physical
	 *            终端编号
	 * @param o
	 *            命令字。
	 */
	public static boolean broadcast(String physical, Notice o) {
		return Shared.broadcast(physical, o);
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
		return Shared.unbroadcast(physical, o);
	}

	/**
	 * 取得队列中最前面的下发命令
	 * 
	 * @param physical
	 *            终端编号
	 * @return 下发命令，如果没有下发命令则为<strong>null</strong>
	 */
	public static Notice notification(String physical) {
		return Shared.notification(physical);
	}
}