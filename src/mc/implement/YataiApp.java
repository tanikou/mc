package mc.implement;

import mc.server.App;
import mc.util.Const;

public class YataiApp {

	public static void main(String[] args) {
		App app = new App(new YataiHandler());
		app.setServerPort(Const.port);
		new Thread(app).start();
	}
}
