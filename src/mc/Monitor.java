package mc;

/**
 * 监控器
 * 
 * @author VicTan@qq.com
 *
 */
public class Monitor {

	/**
	 * 致命错误提醒
	 * 
	 * @param message
	 */
	public void fatal(String message) {
		System.out.println("发生致命错误：" + message);
		System.exit(-1);
	}
}
