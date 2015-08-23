using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace QuickRun.component {
	
	public class SimpleItem : ListViewItem {

		private FileInfo _targetFile;

		public FileInfo TargetFile {
			get { return _targetFile; }
			set { _targetFile = value; }
		}

		public SimpleItem(string text, int imageIndex) : base(text, imageIndex) {}

	}
}
