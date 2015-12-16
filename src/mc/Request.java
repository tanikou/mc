package mc;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import mc.annotation.Bind;
import mc.annotation.Preview;
import mc.annotation.Type;
import mc.util.Data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 请求报文对象基类（子类用于拆解报文为正常数据）
 * 
 * @author VicTan@qq.com
 *
 */
public abstract class Request implements Serializable, Viewable {
	protected static final long serialVersionUID = 1L;
	private static Log logger = LogFactory.getLog(Request.class);

	protected byte[] source = new byte[] {};

	public Request(Packet packet) {
		this.source = packet.data();
	}

	/**
	 * 反射会影响效率，如果要求高，请在子类实现中自己做解析转换
	 */
	public void autobind() {
		for (Field field : this.getClass().getDeclaredFields()) {
			Bind bind = field.getAnnotation(Bind.class);
			if (null == bind)
				continue;
			try {
				byte[] ary = Arrays.copyOfRange(source, bind.begin(),
						bind.begin() + bind.length());
				if (Type.Int == bind.type()) {
					field.set(this, Data.toInt(ary));

				} else if (Type.Byte == bind.type()) {
					field.set(this, ary[0]);

				} else if (Type.Bytes == bind.type()) {
					field.set(this, ary);

				} else if (Type.String == bind.type()) {
					field.set(this, new String(ary));

				} else if (Type.HexString == bind.type()) {
					field.set(this, Data.hex(ary));

				} else if (Type.BcdString == bind.type()) {
					field.set(this, Data.bcd2str(ary));

				} else if (Type.BcdInt == bind.type()) {
					field.set(this, Data.bcd2int(ary));
				}
			} catch (Exception e) {
				logger.trace(field.getName() + " 自动解析报文失败", e);
			}
		}
	}

	@Override
	public String stringify() {
		// return Data.hex(source);
		StringBuilder sb = new StringBuilder();
		sb.append("以下数据通过注释自动取得");
		for (Field field : this.getClass().getDeclaredFields()) {
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

		for (Method method : this.getClass().getDeclaredMethods()) {
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
}
