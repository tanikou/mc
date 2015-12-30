package mc.view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import mc.util.AsClient;
import mc.util.Data;

/**
 * 丑陋代码写出的swing辅助工具
 * 
 * @author VicTan@qq.com
 *
 */
public class Viewer {
	private final JFrame frame = new JFrame("Log Tracer");
	private JTextArea iTxtArea = new JTextArea();
	private JScrollPane iScroll = new JScrollPane(iTxtArea);
	private final TextField iTxtPath = new TextField();
	private final TextField iTxtRate = new TextField();
	private JButton iBtnNext = new Button(">");
	private JCheckBox iChkAppend = new CheckBox("+", false);
	private File iFileLog;
	private int iIntLastPos = 0;
	/** 读取文件的开始位置 */
	private long iPageStart = 0;
	private long iPageEnd = 0;
	private JButton iBtnFind = new Button("查找");
	private String iKeyPrefix = "--";
	private long iLongTotal = 0;
	private final TextField iTxtKey = new TextField();
	private final Queue<Thread> queue = new LinkedList<Thread>();
	private Viewer viewer = this;
	private volatile List<String> hex = new LinkedList<String>();
	private int timerid = 0;
	private String server = "127.0.0.1";
	private int port = 8008;

	public Viewer() {
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		iTxtPath.wight(150).setEditable(false);

		ImageIcon icon = new ImageIcon();
		try {
			InputStream in = this.getClass().getResourceAsStream(
					"/mc/view/image/Open.File.png");
			icon.setImage(ImageIO.read(in));
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		JButton iBtnLog = new Button(icon);
		iBtnLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				chooser.showDialog(new JLabel(), "Log File");
				File file = chooser.getSelectedFile();
				if (null != file && file.isFile()) {
					viewer.init(file);
				}
			}
		});

		DropTargetListener listener = new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
			}

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
			}

			@Override
			public void dragExit(DropTargetEvent dte) {
			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				try {
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))// 如果拖入的文件格式受支持
					{
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);// 接收拖拽来的数据
						@SuppressWarnings("unchecked")
						List<File> list = (List<File>) (dtde.getTransferable()
								.getTransferData(DataFlavor.javaFileListFlavor));
						dtde.dropComplete(true);// 指示拖拽操作已完成

						if (list.size() == 0) {
							return;
						}
						File file = list.get(0);
						if (file.getName().equals("0")) {
							viewer.doViewList(file);

						} else if (file.getName().equals("1")) {
							viewer.doViewList(file);

						} else if (file.getName().equals("2")) {
							viewer.doViewList(file);

						} else if (file.getName().equals("3")) {
							viewer.doViewList(file);

						} else if (file.getName().equals("4")) {
							viewer.doViewPrice(file);

						} else if (file.getName().equals("5")) {
							viewer.doViewHex(file);

						} else if (file.getName().matches("5\\d{16,16}")) {
							viewer.doViewTerm(file);

						} else {
							viewer.init(file);
						}
					} else {
						dtde.rejectDrop();// 否则拒绝拖拽来的数据
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		new DropTarget(iTxtPath, listener);
		new DropTarget(iTxtArea, listener);

		iTxtRate.wight(40).text("0");
		iTxtRate.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					double rate = Double.parseDouble(iTxtRate.getText());
					iPageEnd = (long) (rate / 100 * iLongTotal);
					iTxtArea.setText("");
					viewer.next();
				}
			}
		});

		TextField iTxtPer = new TextField();
		iTxtPer.wight(20).text("%").round(false).setEditable(false);

		JButton iBtnPrev = new Button("<");
		iBtnPrev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		iBtnPrev.setVisible(false);
		iBtnNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				viewer.next();
			}
		});

		JButton iBtnRePacket = new Button("重发报文");
		iBtnRePacket.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String hex = JOptionPane
						.showInputDialog(frame, "请输入Hex形式的完整报文");
				if (null == hex || "".equals(hex)) {
					return;
				}
				viewer.packet(Data.asHex(hex));
			}
		});

		iTxtArea.setAutoscrolls(false);
		iTxtArea.setLineWrap(true);

		iTxtKey.wight(70);
		iTxtKey.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					viewer.stop();
					Thread thread = new Thread(new FullFind());
					thread.start();
					// 重新设置线程做查询
					queue.add(thread);
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					viewer.stop();
				}
			}
		});

		iBtnFind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = iTxtKey.getText();
				if (key.length() == 0) {
					viewer.message("请先输入关键词");
					return;
				}
				iIntLastPos = iTxtArea.getText().indexOf(key, iIntLastPos);
				if (0 > iIntLastPos) {
					viewer.message("未搜索到匹配关键词");
					return;
				}
				iTxtArea.requestFocus();
				iTxtArea.setSelectionStart(iIntLastPos);
				iTxtArea.setSelectionEnd(iIntLastPos + key.length());
				iIntLastPos = iIntLastPos + key.length();
			}
		});

		iTxtArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_F && e.isAltDown()) {
					String pre = JOptionPane.showInputDialog(frame,
							"请输入报文前置关键符");
					if (null == pre || "".equals(pre)) {
						return;
					}
					iKeyPrefix = pre;
					// viewer.extract(iKeyPrefix = pre);
					viewer.message("设置提取报文的前置关键符为：" + iKeyPrefix);

				} else if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()
						&& e.isShiftDown()) {
					viewer.extract(iKeyPrefix, 0, viewer.iLongTotal);

				} else if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
					viewer.doExtractLoaded(iKeyPrefix);

				} else if (e.getKeyCode() == KeyEvent.VK_P && e.isControlDown()) {
					viewer.packet();

				} else if (e.getKeyCode() == KeyEvent.VK_I && e.isControlDown()) {
					viewer.describe();

				} else if (e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()) {
					viewer.next(iPageStart);
				} else if (e.getKeyCode() == KeyEvent.VK_Q && e.isControlDown()) {
					viewer.message("当前服务器为" + server + ":" + port);
					String hex = JOptionPane.showInputDialog(frame,
							"请输入服务器IP与端口号，如127.0.0.1:8008");
					if (null == hex || "".equals(hex)) {
						viewer.message("你未输入");
						return;
					}
					String[] v = hex.split(":");
					if (v.length != 2) {
						viewer.message("请输入规定的格式如127.0.0.1:8008");
						return;
					}
					server = v[0];
					port = Integer.parseInt(v[1]);
					viewer.message("新服务器为" + server + ":" + port);
				}
			}
		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(iBtnLog);
		panel.add(iTxtPath);

		panel.add(new JLabel("  "));
		panel.add(iTxtRate);
		panel.add(iTxtPer);
		panel.add(iBtnPrev);
		panel.add(iBtnNext);
		panel.add(iChkAppend);

		panel.add(iTxtKey);
		panel.add(iBtnFind);
		panel.add(new JLabel("  "));
		panel.add(iBtnRePacket);
		// panel.add(iTxtMsg);

		frame.add(panel);
		Dimension size = frame.getSize();
		iScroll.setPreferredSize(new Dimension(size.width, size.height - 75));
		iScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		iScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		iScroll.setAutoscrolls(false);
		frame.add(iScroll);

		describe();

		frame.setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		new Viewer();
	}

	public void describe() {
		iTxtArea.setText("");
		iTxtArea.append("把日志写入数据库会不会是个好的办法呢？\r\n\r\n");
		iTxtArea.append("可解析下载文件和查看日志文件\r\n");
		iTxtArea.append("支持拖拽日志文件到显示框\r\n");
		iTxtArea.append("进度框可直接输入百分比到一定位置开始预览\r\n");
		iTxtArea.append("选中追加选项时换页不会清空上一页内容\r\n");
		iTxtArea.append("在关键词栏里面  回车  做从当前文件位置开始做全文搜索\r\n");
		iTxtArea.append("在关键词栏里面  ESC  可取消当前正在进行的全文搜索\r\n");
		iTxtArea.append("搜索是在当前页内循环检索\r\n");
		iTxtArea.append("重发报文是向本机发送完整报文，FA开头-CRC结束\r\n");
		iTxtArea.append("Ctrl+Q可设置服务器IP与端口\r\n");
		iTxtArea.append("ALT+F可设置提取报文的前置关键符\r\n");
		iTxtArea.append("Ctrl+F可提取当前页内能匹配前置关键符的报文\r\n");
		iTxtArea.append("Ctrl+Shift+F可提取整个日志中的能匹配前置关键符的报文\r\n");
	}

	private void init(File file) {
		iTxtPath.setText(file.getAbsolutePath());
		iFileLog = new File(iTxtPath.getText());
		iPageStart = 0;
		iPageEnd = 0;
		iLongTotal = iFileLog.length();
		iTxtArea.setText("");
		iTxtRate.setText("0");
		iBtnNext.doClick();
	}

	private class FullFind implements Runnable {
		@Override
		public void run() {
			try {
				viewer.search();
			} catch (Exception e) {
			}
		}
	};

	private void doExtractLoaded(String key) {
		hex.clear();
		for (int i = 0; i < iTxtArea.getLineCount(); i++) {
			try {
				int start = iTxtArea.getLineStartOffset(i);
				String line = iTxtArea.getText(start,
						iTxtArea.getLineEndOffset(i) - start);

				int idx = line.indexOf(key + "fa ");
				if (idx >= 0) {
					hex.add(line.substring(idx + 2));
				} else if (line.startsWith("fa ")) {
					hex.add(line);
				}
			} catch (Exception ex) {
				// ex.printStackTrace();
				viewer.message("提取报文失败，错误信息：" + ex.getMessage());
			}
		}
		viewer.message("使用 " + iKeyPrefix + " 提取报文完成，总共：" + hex.size() + " 条报文");
		iTxtArea.setText("");
		for (String line : hex) {
			iTxtArea.append(line);
		}
	}

	private void doExtract(String key, long iSeekStart, long iSeekEnd)
			throws IOException {
		if (null == iFileLog) {
			viewer.message("请先选择日志文件");
			return;
		}

		hex.clear();

		RandomAccessFile reader = new RandomAccessFile(iFileLog, "r");
		reader.seek(iSeekStart);
		String line = null;
		iTxtArea.setText("");
		while (null != (line = reader.readLine())) {
			int idx = line.indexOf(key + "fa ");
			if (idx >= 0) {
				String txt = line.substring(idx + 2);
				hex.add(txt);
				iTxtArea.append(txt);
				iTxtArea.append("\r\n");

			} else if (line.startsWith("fa ")) {
				hex.add(line);
				iTxtArea.append(line);
				iTxtArea.append("\r\n");
			}
			double pos = 1.0 * reader.getFilePointer() / iLongTotal * 100;
			viewer.message(String.format("报文提取进度：%.2f", pos));
			if (reader.getFilePointer() >= iSeekEnd) {
				break;
			}
		}
		reader.close();

		viewer.message("使用 " + iKeyPrefix + " 提取报文完成，总共：" + hex.size() + " 条报文");
	}

	private void extract(final String key, final long iSeekStart,
			final long iSeekEnd) {

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					doExtract(key, iSeekStart, iSeekEnd);
				} catch (Exception ex) {
					ex.printStackTrace();
					viewer.message("提取报文失败" + ex.getMessage());
				}
			}
		}).start();
	}

	public void extract(String key) {
		extract(key, viewer.iPageStart, viewer.iPageEnd);
	}

	private void packet() {
		if (hex.size() == 0) {
			viewer.message("队列中没有报文，请先格式化");
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				String line = hex.remove(0).replaceAll("\r|\n|\r\n", "");
				viewer.packet(Data.asHex(line));

				if (hex.size() == 0) {
					viewer.message("批量发送报文完毕");
				} else {
					viewer.packet();
					viewer.message("剩余报文数：" + hex.size());
				}
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void packet(byte[] ary) {
		if (null == ary) {
			viewer.message("发送报文为空，跳过此次操作");
			return;
		}

		try {
			viewer.message("发送：" + Data.hex(ary));

			byte[] res = new AsClient(server, port).send(ary).call();

			viewer.message("应答：" + Data.hex(res));
		} catch (Exception ex) {
			// ex.printStackTrace();
			viewer.message("异常：" + ex.getMessage());
		}
	}

	private void search() throws IOException {
		String line = null, key = iTxtKey.getText();

		if (null == iFileLog) {
			viewer.message("请先选择日志文件");
			return;
		} else if ("".equals(key)) {
			viewer.message("请先输入查询关键字");
			return;
		} else if (iTxtArea.getText().indexOf(key) >= 0) {
			iBtnFind.doClick();
			return;
		}
		viewer.message("已经从本页开始做全文搜索，请稍候。可按ESC按取消搜索");

		RandomAccessFile reader = new RandomAccessFile(iFileLog, "r");
		long pointer = 0, all = reader.length();
		boolean targeted = false;
		reader.seek(iPageEnd);
		// iTxtKey.setEditable(false);
		while (null != (line = reader.readLine())) {
			if (targeted = this.decode(line).contains(key)) {
				iIntLastPos = 0;// 重置开始查询的位置
				iPageEnd = pointer;// 上一行的位置

				viewer.next();
				iBtnFind.doClick();
				break;
			}
			pointer = reader.getFilePointer();

			iTxtRate.setText(String.format("%.2f", 1.0 * pointer / all * 100));
		}
		viewer.message(targeted ? "搜索完毕" : "已经搜索完毕，未的到关键词");
		// iTxtKey.setEditable(true);
		reader.close();
	}

	private String decode(String v) throws UnsupportedEncodingException {
		return new String(v.getBytes("ISO-8859-1"), "utf-8");
	}

	private void next() {
		next(iPageEnd);
	}

	private void next(long iLongSeek) {
		try {
			doNext(iLongSeek);
			viewer.message("重新加载本页完毕");
		} catch (IOException e) {
			viewer.message("加载失败。。。");
		}
	}

	private void doNext(long iLongSeek) throws IOException {
		if (null == iFileLog) {
			viewer.message("请先选择日志文件");
			return;
		} else if (iLongSeek >= iLongTotal) {
			viewer.message("文件已经查看完毕");
			return;
		}
		if (false == iChkAppend.isSelected()) {
			iTxtArea.setText("");
		}
		RandomAccessFile reader = new RandomAccessFile(iFileLog, "r");
		reader.seek(iPageStart = iLongSeek);

		String line = null;
		int start = iTxtArea.getSelectionEnd();
		for (int i = 0; i < 250; i++) {
			if (null == (line = reader.readLine())) {
				break;
			}
			iTxtArea.append(this.decode(line));
			iTxtArea.append("\r\n");
		}
		iPageEnd = reader.getFilePointer();
		iTxtRate.setText(String.format("%.2f", (double) (1.0 * iPageEnd
				/ iLongTotal * 100)));
		reader.close();

		iTxtArea.requestFocus();
		iTxtArea.setSelectionStart(start);
		iTxtArea.setSelectionEnd(start);
	}

	public void doViewHex(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);

			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			iTxtArea.setText("");
			iTxtArea.append(Data.hex(buffer));

			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doViewList(File file) {
		try {
			RandomAccessFile reader = new RandomAccessFile(file, "r");

			byte[] four = new byte[4], ten = new byte[10], two = new byte[2];
			iTxtArea.setText("");

			reader.read(two);
			iTxtArea.append("\r\n版本号：" + Data.hex(two));
			reader.read(four);
			iTxtArea.append("\r\n生效日期：" + Data.hex(four));
			reader.read(four);
			iTxtArea.append("\r\n截止日期：" + Data.hex(four));
			reader.read(two);
			iTxtArea.append("\r\n有效区域：" + Data.hex(two));

			reader.read(four);
			int count = Data.toInt(four);
			iTxtArea.append("\r\n白名单数量：" + count);

			for (int i = 0; i < count; i++) {
				reader.read(ten);
				iTxtArea.append("\r\n卡号[" + (i + 1) + "]：" + Data.bcd(ten));
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doViewPrice(File file) {
		try {
			RandomAccessFile reader = new RandomAccessFile(file, "r");

			byte[] four = new byte[4], two = new byte[2], time = new byte[6];
			iTxtArea.setText("");

			iTxtArea.append("\r\n版本号：" + reader.read());
			reader.read(time);
			iTxtArea.append("\r\n生效日期：" + Data.bcd(time));
			int count = reader.read();
			iTxtArea.append("\r\n油品数量：" + count);

			for (int i = 0; i < count; i++) {
				iTxtArea.append("\r\n\r\n--------------------旧");
				iTxtArea.append("\r\n枪号：" + reader.read());
				reader.read(two);
				iTxtArea.append("\r\n油品代码：" + Data.bcd(two));
				reader.read(four);
				iTxtArea.append("\r\n油品密度：" + Data.toInt(four));
				int sub = reader.read();
				iTxtArea.append("\r\n价格数量：" + sub);
				for (int k = 1; k < sub + 1; k++) {
					reader.read(two);
					iTxtArea.append("\r\n价格 [ " + k + " ]：" + Data.toInt(two));
				}
			}

			for (int i = 0; i < count; i++) {
				iTxtArea.append("\r\n\r\n--------------------新");
				iTxtArea.append("\r\n枪号：" + reader.read());
				reader.read(two);
				iTxtArea.append("\r\n油品代码：" + Data.bcd(two));
				reader.read(four);
				iTxtArea.append("\r\n油品密度：" + Data.toInt(four));
				int sub = reader.read();
				iTxtArea.append("\r\n价格数量：" + sub);
				for (int k = 1; k < sub + 1; k++) {
					reader.read(two);
					iTxtArea.append("\r\n价格 [ " + k + " ]：" + Data.toInt(two));
				}
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stop() {
		while (queue.size() > 0) {
			Thread old = queue.poll();
			if (null != old) {
				old.interrupt();
			}
		}
	}

	private void message(String v) {
		frame.setTitle("Log Tracer - " + v);
		new Thread(new Runnable() {
			@Override
			public void run() {
				int uid = ++timerid;
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				if (uid == timerid) {
					frame.setTitle("Log Tracer");
				}
			}
		}).start();
	}

	public void doViewTerm(File file) {
		try {
			RandomAccessFile reader = new RandomAccessFile(file, "r");
			byte[] buffer = new byte[4], merch = new byte[2];
			iTxtArea.setText("");

			iTxtArea.append("\r\n终端号：" + file.getName().substring(2));
			iTxtArea.append("\r\n版本号：" + reader.readByte());
			iTxtArea.append("\r\n省代码：" + Data.hex(reader.readByte()));
			iTxtArea.append("\r\n地市代码：" + Data.hex(reader.readByte()));
			reader.read(buffer);
			iTxtArea.append("\r\n上级单位：" + Data.bcd(buffer));
			reader.read(buffer);
			iTxtArea.append("\r\n油站代码：" + Data.bcd(buffer));
			reader.read(buffer);
			iTxtArea.append("\r\n网点编号：" + Data.bcd(buffer));
			reader.read(merch);
			iTxtArea.append("\r\n商户代码：" + Data.bcd(merch));
			int num = reader.read();
			iTxtArea.append("\r\n枪数：" + num);
			for (int i = 0; i < num; i++) {
				iTxtArea.append("\r\n");
				iTxtArea.append("统一编号：" + reader.readByte());
				iTxtArea.append("  物理编号：" + reader.readByte());
			}

			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
