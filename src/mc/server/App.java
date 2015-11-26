package mc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mc.Handler;
import mc.dao.DB;
import mc.util.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 主服务
 * 
 * @author VicTan@qq.com
 */
public class App implements Runnable {
	private static Log logger = LogFactory.getLog(App.class);

	/** 用于 多端口服务 中停止本端口服务 */
	private boolean isSingleRunning = true;
	/** 用于 多端口服务 中记录各端口号 */
	private int iPort = 8008;
	private int iMaxThread = 100;
	private Handler iHanlder;

	public App(Handler hanlder) {
		this.iHanlder = hanlder;
	}

	public App setServerPort(int port) {
		this.iPort = port;
		return this;
	}

	public App setMaxThread(int max) {
		this.iMaxThread = max;
		return this;
	}

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
		logger.info(Data.toView(DB.getSystemTime()) + "启动服务，端口：" + iPort);

		while (isSingleRunning) {
			exer.submit(iHanlder.setup(server.accept()));
		}

		server.close();
		exer.shutdown();
		logger.info(Data.toView(DB.getSystemTime()) + "停止服务，端口：" + iPort);
	}
}