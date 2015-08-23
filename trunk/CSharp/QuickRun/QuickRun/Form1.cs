using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Collections;
using QuickRun.component;
using QuickRun.util;
using System.Diagnostics;

namespace QuickRun {
	public partial class Form1 : Form {

		public static Dictionary<DirectoryInfo, ImageList> imageListInvocations = new Dictionary<DirectoryInfo, ImageList>();

		ShellContextMenu shellMenu = new ShellContextMenu();

		public Form1() {
			InitializeComponent();
		}

		private void Form1_Load(object sender, EventArgs e) {
			notifyIcon1.Text = Settings.title;

			spc.Top = Settings.titleHeight + 7;
			spc.Left = 7;
			spc.Width = this.Width - spc.Left - 7;
			spc.Height = this.Height - spc.Top - 7;

			listView1.DoubleClick += new EventHandler(SelectItem);

			InitInvocations();
			this.Activate();
		}

		#region 加载文件夹和文件夹内容
		/// <summary>
		/// 加载标签
		/// </summary>
		private void InitInvocations() {
			DirectoryInfo first = Settings.sorter[0];
			List<DirectoryInfo> temp = new List<DirectoryInfo>(Settings.sorter);
			temp.Reverse();
			foreach (DirectoryInfo dir in temp) {
				SimpleTab tab = new SimpleTab(dir.Name, dir);
				tab.MouseEnter += new EventHandler(MenuSelect);
				tab.SetActive(dir.Equals(first));
				spc.Panel1.Controls.Add(tab);
			}
			LoadDir(first);
		}

		/// <summary>
		/// 根据文件夹加载列表
		/// </summary>
		/// <param name="dir"></param>
		private void LoadDir(DirectoryInfo dir) {
			Settings.NowTabIndex = Settings.sorter.IndexOf(dir);
			listView1.Items.Clear();
			ImageList iList = imageListInvocations[dir];
			iList.Images.Clear();
			listView1.LargeImageList = iList;
			int i = 0;
			Settings.invocations[dir] = dir.GetFiles().ToList();
			foreach (FileInfo file in Settings.invocations[dir]) {
				string fileName = file.Name.EndsWith(".lnk") ? file.Name.Substring(0, file.Name.LastIndexOf(".lnk")) : file.Name;
				SimpleItem item = new SimpleItem(fileName, i++);
				item.TargetFile = file;
				iList.Images.Add(GetSystemIcon.GetIcon(file.FullName));
				listView1.Items.Add(item);
			}
		}
		#endregion

		#region 主逻辑事件-切换标签、双击图标等等
		/// <summary>
		/// 窗口列表快捷方式双击
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		public void SelectItem(object sender, EventArgs e) {
			ListView listView = (ListView)sender;
			SimpleItem item = (SimpleItem)listView.SelectedItems[0];
			using (Process p = new Process()) {
				p.StartInfo.FileName = item.TargetFile.FullName;
				p.Start();
				p.Close();
			}
		}

		/// <summary>
		/// 标签鼠标停留
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		public void MenuSelect(object sender, EventArgs e) {
			SimpleTab tab = (SimpleTab)sender;
			foreach (Control item in spc.Panel1.Controls) {
				SimpleTab t = (SimpleTab)item;
				t.SetActive(item.Equals(tab));
			}
			LoadDir(tab._dir);
		}
		#endregion

		#region 窗体事件-拖动、缩放功能

		/// <summary>
		/// 是否允许拖动
		/// </summary>
		bool moveFlag = false;

		/// <summary>
		/// 是否允许缩放
		/// </summary>
		bool resizeFlag = false;

		/// <summary>
		/// 正在缩放状态
		/// </summary>
		bool resizing = false;

		/// <summary>
		/// 拖动偏移记录值
		/// </summary>
		Point offset = new Point();

		/// <summary>
		/// 缩放偏移记录值
		/// </summary>
		Point resizeOffset = new Point();

		/// <summary>
		/// 鼠标左键按下检测
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void Form1_Down(object sender, MouseEventArgs e) {
			if (e.Button == MouseButtons.Left) {	// 左键按下
				moveFlag = e.Y < Settings.titleHeight;
				if (moveFlag) {
					offset.X = e.X;
					offset.Y = e.Y;
				} else {
					if (resizeFlag) {
						resizeOffset.X = this.Width - e.X;
						resizeOffset.Y = this.Height - e.Y;
						resizing = true;
					}
				}
			}
		}

		/// <summary>
		/// 鼠标移动
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void Form1_MouseMove(object sender, MouseEventArgs e) {
			if (moveFlag) {
				this.Top += e.Y - offset.Y;
				this.Left += e.X - offset.X;
			} else {
				resizeFlag = e.X > Width - 7 && e.Y > Height - 7;
				if (resizeFlag) {
					this.Cursor = Cursors.SizeNWSE;
				} else
					this.Cursor = Cursors.Default;
				if (resizing) {
					this.Width = e.X + resizeOffset.X;
					this.Height = e.Y + resizeOffset.Y;
				}
			}
		}

		/// <summary>
		/// 鼠标按键释放
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void Form1_MouseUp(object sender, MouseEventArgs e) {
			if (e.Button == MouseButtons.Left) {
				moveFlag = false;
				resizing = false;
			}
		}
		#endregion

		#region 主题相关
		/// <summary>
		/// 主窗体绘制事件
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void Form1_Paint(object sender, PaintEventArgs e) {
			/* 背景 */
			if (Settings.background != null)
				e.Graphics.DrawImage(Settings.background, new Rectangle(0, 0, Width, Height));

			/* 顶部 */
			e.Graphics.DrawLine(new Pen(Color.White), new Point(0, Settings.titleHeight), new Point(Width, Settings.titleHeight));
			e.Graphics.DrawString(Settings.title, Settings.titleFont, Brushes.Black, new PointF(5, 7));
		}
		#endregion

		#region 通知栏菜单、图标、窗体的显示与隐藏
		/// <summary>
		/// 通知栏图标点击事件
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void notifyIcon1_Click(object sender, EventArgs e) {
			MouseEventArgs even = (MouseEventArgs)e;
			if (even.Button == MouseButtons.Left) {
				if (this.Visible)
					this.Hide();
				else {
					int left = (Screen.PrimaryScreen.Bounds.Width - Width) / 2;
					int top = (Screen.PrimaryScreen.Bounds.Height - Height) / 2;
					this.Left = left;
					this.Top = top;
					this.Show();
					this.Activate();
				}
			}
		}

		/// <summary>
		/// 窗体失去焦点事件
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void Form1_Deactivate(object sender, EventArgs e) {
			if (!Settings.IsLock)
				this.Hide();
		}

		/// <summary>
		/// 通知栏图标菜单
		/// </summary>
		/// <param name="sender"></param>
		/// <param name="e"></param>
		private void notifyMenu_ItemClicked(object sender, ToolStripItemClickedEventArgs e) {
			if (e.ClickedItem.Tag.Equals("exit")) {
				this.Dispose();
			}
		}
		#endregion

		#region 文件拖拽到列表事件
		private void listView1_DragEnter(object sender, DragEventArgs e) {
			if (e.Data.GetDataPresent(DataFormats.FileDrop)) {
				e.Effect = DragDropEffects.Copy;
			} else {
				e.Effect = DragDropEffects.None;
			}
		}

		private void listView1_DragDrop(object sender, DragEventArgs e) {
			string[] s = (string[])e.Data.GetData(DataFormats.FileDrop, false);
			DirectoryInfo now = Settings.sorter[Settings.NowTabIndex];
			for (int i = 0; i < s.Length; i++) {
				FileInfo file = new FileInfo(s[i]);
				FileInfo newFile = new FileInfo(@"lnks\" + now.Name + "\\" + file.Name);
				File.Copy(s[i], newFile.FullName);
				Settings.invocations[now].Add(file);
				string fileName = file.Name.EndsWith(".lnk") ? file.Name.Substring(0, file.Name.LastIndexOf(".lnk")) : file.Name;
				SimpleItem item = new SimpleItem(fileName, listView1.Items.Count);
				item.TargetFile = file;
				imageListInvocations[now].Images.Add(GetSystemIcon.GetIcon(newFile.FullName));
				listView1.Items.Add(item);
			}
		}
		#endregion

		#region 文件右键菜单
		private void listView1_MouseClick(object sender, MouseEventArgs e) {
			SimpleItem item = (SimpleItem) listView1.SelectedItems[0];
			if (e.Button == MouseButtons.Right) {
				shellMenu.ShowContextMenu(new FileInfo[] { item.TargetFile }, Control.MousePosition);
				LoadDir(Settings.sorter[Settings.NowTabIndex]);
			}
		}
		#endregion

	}

	static class Program {
		/// <summary>
		/// 应用程序的主入口点。
		/// </summary>
		[STAThread]
		static void Main() {
			Settings.LoadSettings();
			Application.EnableVisualStyles();
			Application.SetCompatibleTextRenderingDefault(false);
			Application.Run(new Form1());
		}
	}
}
