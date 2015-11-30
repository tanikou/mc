package mc.view;

import java.awt.Component;
import java.awt.Graphics;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;

/**
 * 
 * @author VicTan@qq.com
 *
 */
public class CheckBox extends JCheckBox {
	private static final long serialVersionUID = 1L;

	public CheckBox() {
		this.setIcon(new CheckBoxIcon());
	}

	public CheckBox(String text, boolean selected) {
		this();
		this.setText(text);
		this.setSelected(selected);
	}

	private static class CheckBoxIcon implements Icon {
		private static ImageIcon iImageChecked;
		private static ImageIcon iImageNormal;

		static {
			try {
				iImageChecked = new ImageIcon(
						ImageIO.read(CheckBoxIcon.class
								.getResourceAsStream("/mc/view/image/Check.Checked.png")));
				iImageNormal = new ImageIcon(
						ImageIO.read(CheckBoxIcon.class
								.getResourceAsStream("/mc/view/image/Check.Normal.png")));
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		public void paintIcon(Component component, Graphics g, int x, int y) {
			AbstractButton btn = (AbstractButton) component;
			ButtonModel model = btn.getModel();
			g.translate(x, y);
			ImageIcon icon = model.isSelected() ? iImageChecked : iImageNormal;
			g.drawImage(icon.getImage(), 0, 0, component);
			g.translate(-x, -y);
		}

		public int getIconWidth() {
			return iImageChecked.getIconWidth();
		}

		public int getIconHeight() {
			return iImageChecked.getIconHeight();
		}
	}
}
