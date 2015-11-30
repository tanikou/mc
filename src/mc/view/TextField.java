package mc.view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JTextField;

/**
 * 
 * @author VicTan@qq.com
 *
 */
public class TextField extends JTextField {
	private static final long serialVersionUID = 1L;
	private Color color = new Color(183, 207, 238);

	private int height = 27;

	public TextField() {
		this.setBorder(new Border(color, 1, true));
		this.setPreferredSize(new Dimension(this.getWidth(), height));
	}

	public TextField wight(int w) {
		this.setPreferredSize(new Dimension(w, height));
		return this;
	}

	public TextField height(int h) {
		this.setPreferredSize(new Dimension(this.getWidth(), height = h));
		return this;
	}

	/**
	 * 设置是否圆角
	 * 
	 * @param is
	 * @return
	 */
	public TextField round(boolean is) {
		this.setBorder(new Border(color, 1, is));
		return this;
	}

	public TextField text(String v) {
		super.setText(v);
		return this;
	}
}
