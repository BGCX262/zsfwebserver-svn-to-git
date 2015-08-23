using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.IO;

namespace QuickRun.component {
	/// <summary>
	/// 自定义Tab
	/// </summary>
	public class SimpleTab : Panel {

		public DirectoryInfo _dir;
		private string _title;
		private Point[] border;
		StringFormat sf = new StringFormat();
		private bool IsActive = false;
		private Brush[] defaultTheme = Settings.tabDefault;

		public void SetActive(bool active) {
			IsActive = active;
			defaultTheme = IsActive ? Settings.tabActive : Settings.tabDefault;
			this.Refresh();
		}

		public SimpleTab(string title, DirectoryInfo dir) {
			sf.Alignment = StringAlignment.Center;
			sf.LineAlignment = StringAlignment.Center;
			_dir = dir;
			_title = title;
			Height = 22;
			this.Dock = DockStyle.Top;
			this.Paint += new PaintEventHandler(SimpleTab_Paint);
		}

		void InitBorder(out GraphicsPath path) {
			List<Point> list = new List<Point>();
			list.Add(new Point(3, 0));
			list.Add(new Point(Width - 1, 0));
			list.Add(new Point(Width - 1, Height - 1));
			list.Add(new Point(3, Height - 1));
			list.Add(new Point(0, Height - 5));
			list.Add(new Point(0, 4));
			list.Add(new Point(2, 1));
			list.Add(new Point(3, 0));
			border = list.ToArray();

			path = new GraphicsPath();
			path.AddLines(border);
		}

		void InitLeft(out GraphicsPath path) {
			List<Point> list = new List<Point>();
			list.Add(new Point(3, 0));
			list.Add(new Point(6, 0));
			list.Add(new Point(6, Height - 1));
			list.Add(new Point(3, Height - 1));
			list.Add(new Point(0, Height - 5));
			list.Add(new Point(0, 4));
			list.Add(new Point(2, 1));
			list.Add(new Point(3, 0));

			path = new GraphicsPath();
			path.AddLines(list.ToArray());
		}

		void SimpleTab_Paint(object sender, PaintEventArgs e) {
			GraphicsPath path;
			InitBorder(out path);
			GraphicsPath lpath;
			InitLeft(out lpath);
			e.Graphics.FillPath(defaultTheme[0], path);
			e.Graphics.FillPath(defaultTheme[1], lpath);
			e.Graphics.DrawPath(new Pen(defaultTheme[2], 1), path);
			e.Graphics.DrawString(_title, Settings.tabFont, defaultTheme[2], new RectangleF(0, 0, Width, Height), sf);
		}

	}
}
