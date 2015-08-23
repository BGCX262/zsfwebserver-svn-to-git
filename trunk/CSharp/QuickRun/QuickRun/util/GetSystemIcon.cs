using System;
using System.IO;
using System.Drawing;
using Microsoft.Win32;


using System.Runtime.InteropServices;


namespace QuickRun.util {
	///
	/// 提供从操作系统读取图标的方法
	///
	public class GetSystemIcon {
		///
		/// 依据文件名读取图标，若指定文件不存在，则返回空值。
		///
		///
		///
		public static Icon GetIcon(string fileName) {
			if (fileName == null || fileName.Equals(string.Empty)) return null;
			if (!File.Exists(fileName)) return null;

			SHFILEINFO shinfo = new SHFILEINFO();
			//Use this to get the small Icon
			Win32.SHGetFileInfo(fileName, 256, ref shinfo, (uint)Marshal.SizeOf(shinfo), Win32.SHGFI_ICON | Win32.SHGFI_LARGEICON);
			//The icon is returned in the hIcon member of the shinfo struct
			System.Drawing.Icon myIcon = System.Drawing.Icon.FromHandle(shinfo.hIcon);
			return myIcon;
		}
	}



	[StructLayout(LayoutKind.Sequential)]
	public struct SHFILEINFO {
		public IntPtr hIcon;
		public IntPtr iIcon;
		public uint dwAttributes;
		[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 260)]
		public string szDisplayName;
		[MarshalAs(UnmanagedType.ByValTStr, SizeConst = 80)]
		public string szTypeName;
	};

	///
	/// 定义调用的API方法
	///
	class Win32 {
		public const uint SHGFI_ICON = 0x100;
		public const uint SHGFI_LARGEICON = 0x0; // 'Large icon
		public const uint SHGFI_SMALLICON = 0x1; // 'Small icon

		[DllImport("shell32.dll")]
		public static extern IntPtr SHGetFileInfo(string pszPath, uint dwFileAttributes, ref SHFILEINFO psfi, uint cbSizeFileInfo, uint uFlags);
		[DllImport("shell32.dll")]
		public static extern uint ExtractIconEx(string lpszFile, int nIconIndex, int[] phiconLarge, int[] phiconSmall, uint nIcons);

	}
}
