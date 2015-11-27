package mc.implement;

import mc.server.App;
import mc.util.Const;

/**
 * 根据 亚太协议 实现的卡机联动管控程序监听服务
 * 
 * @author VicTan@qq.com
 *
 */
public class YataiApp {

	public static void main(String[] args) {
		App app = new App(new YataiHandler());
		app.setServerPort(Const.port);
		new Thread(app).start();
	}
}
