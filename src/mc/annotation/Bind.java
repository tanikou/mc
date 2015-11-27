package mc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自动解析报文用的注解
 * 
 * @author VicTan@qq.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
	/** 在报文原始数据中的下标起点 */
	public int begin();

	/** 在报文原始数据中的长度 */
	public int length();

	/** 数据类型 */
	public Type type();
}
