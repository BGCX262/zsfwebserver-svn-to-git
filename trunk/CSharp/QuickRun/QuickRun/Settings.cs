using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections;
using System.IO;
using System.Drawing;
using System.Windows.Forms;

namespace QuickRun {
	/// <summary>
	/// 配置映射
	/// </summary>
	public class Settings {

		/// <summary>
		/// 内容关系映射
		/// </summary>
		public static Dictionary<DirectoryInfo, List<FileInfo>> invocations = new Dictionary<DirectoryInfo, List<FileInfo>>();

		/// <summary>
		/// 列表排序规则
		/// </summary>
		public static List<DirectoryInfo> sorter = new List<DirectoryInfo>();

		/// <summary>
		/// 标题高度
		/// </summary>
		public static int titleHeight = 25;

		/// <summary>
		/// 标题初始内容
		/// </summary>
		public static string title = "Quick Run";

		/// <summary>
		/// 标题字体
		/// </summary>
		public static Font titleFont = new Font(new FontFamily("宋体"), 9, FontStyle.Bold);

		/// <summary>
		/// 背景图片
		/// </summary>
		public static Image background = null;

		/// <summary>
		/// 标签页字体
		/// </summary>
		public static Font tabFont = new Font(new FontFamily("宋体"), 9);

		/// <summary>
		/// 标签默认配色
		/// </summary>
		public static Brush[] tabDefault = new Brush[] { Brushes.White, Brushes.Gray, Brushes.Black };

		/// <summary>
		/// 标签激活配色
		/// </summary>
		public static Brush[] tabActive = new Brush[] { Brushes.White, Brushes.Red, Brushes.Black };

		/// <summary>
		/// 是否钉在桌面
		/// </summary>
		public static bool IsLock = true;

		/// <summary>
		/// 当前激活标签
		/// </summary>
		public static int NowTabIndex = 0;

		/// <summary>
		/// 加载配置文件
		/// </summary>
		public static void LoadSettings() {
			if (!Directory.Exists("lnks")) {
				Directory.CreateDirectory("lnks");
				Directory.CreateDirectory(@"lnks\新建");
			}
			if (!File.Exists("config.inf")) {
				File.Create("config.inf");
			}

			DirectoryInfo dir = new DirectoryInfo("lnks");
			DirectoryInfo[] dirs = dir.GetDirectories();
			foreach (DirectoryInfo d in dirs) {
				FileInfo[] files = d.GetFiles();
				List<FileInfo> filesList = files.ToList();
				sorter.Add(d);
				invocations.Add(d, filesList);
				ImageList iList = new ImageList();
				iList.ColorDepth = ColorDepth.Depth32Bit;
				iList.ImageSize = new Size(32, 32);
				Form1.imageListInvocations.Add(d, iList);
			}
		}

		public static void SaveSettings() {
			
		}

	}
}
