package mc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * 常量
 * 
 * @author VicTan@qq.com
 */
public class Const {
	private static Log logger = LogFactory.getLog(Const.class);
	private static Properties config = load();

	/**
	 * 取得配置文件中的常量信息
	 * 
	 * @param v
	 * @return
	 */
	public static String prop(String v) {
		Object o = config.get(v);
		return null == o ? null : ((String) o).trim();
	}

	/**
	 * 读取配置文件
	 * 
	 * @return
	 * @throws IOException
	 */
	private static Properties load() {
		// 如果存在外部的log4j配置则使用外部自定义的配置，否则使用内置的配置
		if (new File("log4j.xml").exists()) {
			DOMConfigurator.configure("log4j.xml");
		}

		Properties props = new Properties();

		try {
			File file = new File("config.ini");
			if (false == file.exists()) {
				props.load(Const.class.getResourceAsStream("/config.ini"));
				logger.error("加载配置：" + file.getAbsolutePath() + "失败，使用内置配置");
			} else {
				props.load(new FileInputStream(file));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return props;
	}
}
