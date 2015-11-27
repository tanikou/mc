package mc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import mc.annotation.Preview;
import mc.test.Tester;

/**
 * 报文应答对象基类（子类用于组装正常数据为报文）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Response implements Serializable, Viewable {
	private static final long serialVersionUID = 1L;
	protected byte[] source;

	public Response(byte[] origin) {
		this.source = origin.clone();
	}

	@Override
	public String preview() {
		// return Data.hex(source);
		StringBuilder sb = new StringBuilder();
		sb.append("以下数据通过注释自动取得\r\n");
		for (Field field : Tester.class.getDeclaredFields()) {
			Preview bind = field.getAnnotation(Preview.class);
			if (null == bind)
				continue;
			Object o;
			try {
				o = field.get(this);
			} catch (Exception e) {
				o = "通过注解自动取得数据失败";
			}
			sb.append("\r\n");
			sb.append(bind.name());
			sb.append("：");
			sb.append(o);
		}

		for (Method method : Tester.class.getDeclaredMethods()) {
			Preview bind = method.getAnnotation(Preview.class);
			if (null == bind)
				continue;
			Object o;
			try {
				o = method.invoke(this);
			} catch (Exception e) {
				o = "通过注解自动取得数据失败";
			}
			sb.append("\r\n");
			sb.append(bind.name());
			sb.append("：");
			sb.append(o);
		}

		return sb.toString();
	}

	public abstract Packet packet();
}
