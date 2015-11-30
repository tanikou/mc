package mc.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * 
 * @author VicTan@qq.com
 *
 */
public class Button extends JButton {
	private static final long serialVersionUID = 1L;

	public static final Color BUTTON_COLOR1 = new Color(205, 255, 205);
	public static final Color BUTTON_COLOR2 = new Color(51, 154, 47);
	public static final Color BUTTON_FOREGROUND_COLOR = Color.WHITE;

	private boolean hover;

	public Button() {
		setFont(new Font("system", Font.PLAIN, 12));
		setBorderPainted(false);
		setForeground(BUTTON_COLOR2);
		setFocusPainted(false);
		setContentAreaFilled(false);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setForeground(BUTTON_FOREGROUND_COLOR);
				hover = true;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setForeground(BUTTON_COLOR2);
				hover = false;
				repaint();
			}
		});
	}

	public Button(String text) {
		this();
		this.setText(text);
	}

	public Button(ImageIcon icon) {
		this();
		this.setIcon(icon);
		this.setUI(new BasicButtonUI());// 恢复基本视觉效果
		this.setContentAreaFilled(false);// 设置按钮透明
		// this.setMargin(new Insets(0, 0, 0, 0));// 按钮内容与边框距离
		// this.setBorder(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		int h = getHeight();
		int w = getWidth();
		float tran = 0.4F;
		if (!hover) {
			tran = 0.3F;
		}

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint p1;
		GradientPaint p2;
		if (getModel().isPressed()) {
			p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
					new Color(100, 100, 100));
			p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,
					new Color(255, 255, 255, 100));
		} else {
			p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,
					new Color(0, 0, 0));
			p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
					h - 3, new Color(0, 0, 0, 50));
		}
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				tran));
		RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
				h - 1, 10, 10);
		Shape clip = g2d.getClip();
		g2d.clip(r2d);
		GradientPaint gp = new GradientPaint(0.0F, 0.0F, BUTTON_COLOR1, 0.0F,
				h, BUTTON_COLOR2, true);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, w, h);
		g2d.setClip(clip);
		g2d.setPaint(p1);
		g2d.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);
		g2d.setPaint(p2);
		g2d.drawRoundRect(1, 1, w - 3, h - 3, 8, 8);
		g2d.dispose();
		super.paintComponent(g);
	}
}
