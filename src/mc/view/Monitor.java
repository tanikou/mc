package mc.view;

import javax.swing.JOptionPane;

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
	public static void fatal(String message) {
		try {
			JOptionPane.showMessageDialog(null, message);
		} catch (Throwable e) {
		}
	}
}
