import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * 这个类名叫What the fuck
 * 
 * @author zsf
 */
public class WhatTheFuck {

	/**
	 * 分析结果实体
	 * 
	 * @author zsf
	 */
	class Entry {

		/** 类名 **/
		public String typeName;

		/** 内容长度 **/
		public int contentCount;

		/** 内容数组 **/
		public List<String> content;

		/**
		 * DEBUG用
		 */
		public String toString() {
			return "typeName: " + typeName + "\tcontentCount: " + contentCount + "\tcontent: " + content + "\r\n";

		}

	}
	
	/**
	 * 分隔标识符
	 */
	public static final String[] SPLIT_TAG = new String[] { "8E", "8F", "90" };

	/**
	 * 行结束标识
	 */
	public static final String WARP = "00";

	/**
	 * 处理核心方法
	 * 
	 * @param str
	 *            接收字符串（数据）
	 * @return 每个类别的数有几条， 然后各自的具体内容是
	 */
	@SuppressWarnings({ "serial" })
	public Entry[] processData(String str) {
		/* 去空格去TAB */
		str = str.replaceAll(" ", "").replaceAll(((char) 9) + "", "").replaceAll("\r\n", "");

		/* ------------------可行性判断------------------- */
		if (!checkIsTrue(str))
			return null;

		/* ------------------逻辑实现------------------- */

		/* 首先，以00分割出一段数组 */
		String[] strArr = str.split(WARP);

		/* 建立TAG和实体的映射表 */
		HashMap<String, Entry> map = new HashMap<String, Entry>();

		/* 遍历每条信息 */
		for (String content : strArr) {
			/* 取出本条内容的TAG */
			String tag = getTag(content);

			/* 根据实体是否存在做不同的处理 */
			Entry entry = map.get(tag);
			if (entry == null) {
				entry = new Entry();
				entry.typeName = tag;
				
				/* 新生成的ArrayList重写toString方法 */
				entry.content = new ArrayList<String>() {
					
					public String toString() {
						Iterator<String> i = iterator();
						if (!i.hasNext())
							return "";

						StringBuilder sb = new StringBuilder();
						sb.append("1.");
						for (int j = 2;; j++) {
							String e = i.next();
							sb.append(e);
							if (!i.hasNext())
								return sb.toString();
							sb.append("\r\n" + j + ".");
						}
					}
				};
			}
			entry.contentCount++;
			entry.content.add(content.replaceFirst(tag, ""));

			map.put(tag, entry);

		}

		/* 整理返回值 */
		Entry[] entrys = new Entry[map.size()];
		Set<java.util.Map.Entry<String, Entry>> set = map.entrySet();
		int mark = 0;
		for (Iterator<java.util.Map.Entry<String, Entry>> iter = set.iterator(); iter.hasNext(); mark++) {
			java.util.Map.Entry<String, Entry> entry = iter.next();
			entrys[mark] = entry.getValue();

		}

		return entrys;
	}

	/**
	 * 获取字符串中的TAG
	 * 
	 * @param str
	 *            需要取得TAG的字符串
	 * @return TAG
	 */
	private String getTag(String str) {
		/* 遍历 */
		for (String tag : SPLIT_TAG)
			if (str.startsWith(tag))
				return tag;
		
		return null;
	}

	/**
	 * 可行性判断
	 * 
	 * @param str
	 * @return
	 */
	private boolean checkIsTrue(String str) {
		/* 长度 */
		if (str.length() <= 0)
			return false;
		/* 是否有结束符 */
		if (str.indexOf(WARP) == -1)
			return false;
		/* 是否有标签 */
		boolean flag = false;
		String[] strArr = str.split(WARP);
		for (String s : strArr) {
			for (String tag : SPLIT_TAG)
				if (s.startsWith(tag)) {
					flag = false;
					break;
				} else
					flag = true;
			if (flag)
				return false;
		}

		return true;
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();

		String str = "8E31313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313131\r\n" + 
		"3131313131313132\r\n" + 
		"00\r\n" + 
		"8E313200\r\n" + 
		"8E31313100\r\n" + 
		"8E41426162616200\r\n" + 
		"8F313200\r\n" + 
		"8F31313100\r\n" + 
		"90313200\r\n" + 
		"903131313100\r\n";
		//String str = "123";

		WhatTheFuck wtf = new WhatTheFuck();
		final Entry[] entrys = wtf.processData(str);

		System.out.println(Arrays.asList(entrys));
		
		long end = System.currentTimeMillis();
		System.out.println("程序用时：" + (end - begin) + "毫秒");
		
		
		/* 测试tablemodel */
		final JFrame jf = new JFrame();
		final MyPopMenu mpm = wtf.new MyPopMenu();
		
		jf.setBounds(100, 100, 1024, 768);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final TestTableModel model = wtf.new TestTableModel();
		model.entry = entrys[0];
		final JTable table = new JTable(model);
		
		jf.add(new JScrollPane(table));
		
		jf.setVisible(true);
		
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent e) {
		    	if (e.getButton() == MouseEvent.BUTTON3) {
		    		try {
						Robot robot = new Robot();
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
		    	}
		        maybeShowPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		        maybeShowPopup(e);
		    }

		    private void maybeShowPopup(MouseEvent e) {
		        if (e.isPopupTrigger()) {
		        	mpm.selectedRow = table.getSelectedRow();
		        	mpm.selectedColumn = table.getSelectedColumn();
		            mpm.show(e.getComponent(), e.getX(), e.getY());
		        }
		    }

		});
		
		new Thread() {
			public void run() {
				for (int i = 1; ; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					model.entry = entrys[i % 3];
					table.setModel(model);
					table.updateUI();
				}
			}
		}.start();
		
	}

	/**
	 * 这不是一个tablemodel
	 * @author zsf
	 */
	@SuppressWarnings("serial")
	class TestTableModel extends AbstractTableModel {
		
		public Entry entry;

		@Override
		public int getRowCount() {
			if (entry == null)
				return 0;
			
			return entry.contentCount;
		}

		@Override
		public int getColumnCount() {
			if (entry == null)
				return 0;
			
			return 2;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (entry == null)
				return null;
			
			if (columnIndex == 0)
				return rowIndex + 1;
			else
				return entry.content.get(rowIndex);
		}

		@Override
		public String getColumnName(int column) {
			if (column == 0)
				return "Index";
			else
				return "Content";
		}
		
	}
	
	@SuppressWarnings("serial")
	class MyPopMenu extends JPopupMenu {
		
		public JMenuItem[] items;
		
		/**********至关重要的代码************/
		public int selectedRow;
		public int selectedColumn;
		
		public MyPopMenu() {
			items = new JMenuItem[5];
			items[0] = new JMenuItem("1");
			items[1] = new JMenuItem("2");
			items[2] = new JMenuItem("3");
			items[3] = new JMenuItem("4");
			items[4] = new JMenuItem("5");
			
			for (JMenuItem jmi : items) {
				jmi.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						JOptionPane.showMessageDialog(null, "当前选择的是表格中的" + selectedRow + "行，" + selectedColumn + "列，自个取值呗~");
					}
				});
				add(jmi);
			}
		}
		
	}

}
