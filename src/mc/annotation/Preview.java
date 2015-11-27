package mc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 自动预览报文用的注解
 * 
 * @author VicTan@qq.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Preview {

	public String name();

	/** 显示的排序 */
	public int order();
}
