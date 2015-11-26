package mc;

/**
 * 监控器
 * 
 * @author VicTan@qq.com
 *
 */
public interface Monitor {

	/**
	 * 致命错误提醒
	 * 
	 * @param message
	 */
	public void fatal(String message);
}
