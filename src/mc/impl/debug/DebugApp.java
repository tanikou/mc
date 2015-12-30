package mc.impl.debug;

import java.io.File;

import mc.Server;
import mc.util.Const;

/**
 * 处理终端发送的调试信息
 * 
 * @author VicTan@qq.com
 *
 */
public class DebugApp {
	public static String folder = "debug" + File.separator;
	public static int timeout = 10000;

	static {
		File folder = new File(DebugApp.folder);
		if (false == folder.exists()) {
			folder.mkdirs();
		}
	}

	public static void main(String[] args) {
		int port = Integer.parseInt(Const.prop("debug.port"));
		new Thread(new Server(DebugHandler.class, port)).start();
	}
}
