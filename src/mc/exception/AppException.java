package mc.exception;

/**
 * 程序处理异常
 * 
 * @author VicTan@qq.com
 *
 */
public class AppException extends Exception {
	private static final long serialVersionUID = 1L;

	public AppException(String message) {
		super(message);
	}

	public AppException(String message, Exception ex) {
		super(message, ex);
	}
}
