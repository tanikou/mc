package mc.implement;

import mc.server.App;

/**
 * 文件更新程序监听服务
 * 
 * @author VicTan@qq.com
 *
 */
public class FileApp {

	public static void main(String[] args) {
		new Thread(new App(FileHandler.class, 1202)).start();
	}
}
