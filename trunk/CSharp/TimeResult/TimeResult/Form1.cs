using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace TimeResult {
	public partial class Form1 : Form {
		public Form1() {
			InitializeComponent();
		}

		private void button1_Click(object sender, EventArgs e) {
			DateTime date = dateTimePicker1.Value;
			DateTime time = dateTimePicker2.Value;

			DateTime result = new DateTime(date.Year, date.Month,
				date.Day, time.Hour, time.Minute, time.Second);
			DateTime aa = new DateTime(1970, 1, 1);
			long a = (result.Ticks - aa.Ticks) / 10000 - 8 * 60 * 60 * 1000;
			Clipboard.SetText(a + "");
			MessageBox.Show("时间戳：" + a + "\n已复制到剪切板");
		}

		private void Form1_Load(object sender, EventArgs e) {
			dateTimePicker2.Format = DateTimePickerFormat.Time;
			dateTimePicker2.ShowUpDown = true;
		}
	}
}
