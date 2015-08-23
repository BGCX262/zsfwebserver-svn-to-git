namespace QuickRun {
	partial class Form1 {
		/// <summary>
		/// 必需的设计器变量。
		/// </summary>
		private System.ComponentModel.IContainer components = null;

		/// <summary>
		/// 清理所有正在使用的资源。
		/// </summary>
		/// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
		protected override void Dispose(bool disposing) {
			if (disposing && (components != null)) {
				components.Dispose();
			}
			base.Dispose(disposing);
		}

		#region Windows 窗体设计器生成的代码

		/// <summary>
		/// 设计器支持所需的方法 - 不要
		/// 使用代码编辑器修改此方法的内容。
		/// </summary>
		private void InitializeComponent() {
			this.components = new System.ComponentModel.Container();
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
			this.notifyIcon1 = new System.Windows.Forms.NotifyIcon(this.components);
			this.notifyMenu = new System.Windows.Forms.ContextMenuStrip(this.components);
			this.notifyExit = new System.Windows.Forms.ToolStripMenuItem();
			this.spc = new System.Windows.Forms.SplitContainer();
			this.listView1 = new System.Windows.Forms.ListView();
			this.notifyMenu.SuspendLayout();
			this.spc.Panel2.SuspendLayout();
			this.spc.SuspendLayout();
			this.SuspendLayout();
			// 
			// notifyIcon1
			// 
			this.notifyIcon1.ContextMenuStrip = this.notifyMenu;
			this.notifyIcon1.Icon = ((System.Drawing.Icon)(resources.GetObject("notifyIcon1.Icon")));
			this.notifyIcon1.Visible = true;
			this.notifyIcon1.Click += new System.EventHandler(this.notifyIcon1_Click);
			// 
			// notifyMenu
			// 
			this.notifyMenu.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.notifyExit});
			this.notifyMenu.Name = "notifyMenu";
			this.notifyMenu.Size = new System.Drawing.Size(101, 26);
			this.notifyMenu.ItemClicked += new System.Windows.Forms.ToolStripItemClickedEventHandler(this.notifyMenu_ItemClicked);
			// 
			// notifyExit
			// 
			this.notifyExit.BackColor = System.Drawing.SystemColors.Control;
			this.notifyExit.Name = "notifyExit";
			this.notifyExit.Size = new System.Drawing.Size(100, 22);
			this.notifyExit.Tag = "exit";
			this.notifyExit.Text = "退出";
			// 
			// spc
			// 
			this.spc.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
						| System.Windows.Forms.AnchorStyles.Left)
						| System.Windows.Forms.AnchorStyles.Right)));
			this.spc.BackColor = System.Drawing.Color.Transparent;
			this.spc.FixedPanel = System.Windows.Forms.FixedPanel.Panel1;
			this.spc.Location = new System.Drawing.Point(2, 35);
			this.spc.Margin = new System.Windows.Forms.Padding(0);
			this.spc.Name = "spc";
			// 
			// spc.Panel1
			// 
			this.spc.Panel1.AutoScroll = true;
			this.spc.Panel1MinSize = 85;
			// 
			// spc.Panel2
			// 
			this.spc.Panel2.Controls.Add(this.listView1);
			this.spc.Panel2MinSize = 100;
			this.spc.Size = new System.Drawing.Size(399, 264);
			this.spc.SplitterDistance = 87;
			this.spc.SplitterWidth = 1;
			this.spc.TabIndex = 0;
			this.spc.TabStop = false;
			// 
			// listView1
			// 
			this.listView1.AllowDrop = true;
			this.listView1.BorderStyle = System.Windows.Forms.BorderStyle.None;
			this.listView1.Dock = System.Windows.Forms.DockStyle.Fill;
			this.listView1.LabelEdit = true;
			this.listView1.Location = new System.Drawing.Point(0, 0);
			this.listView1.MultiSelect = false;
			this.listView1.Name = "listView1";
			this.listView1.Size = new System.Drawing.Size(311, 264);
			this.listView1.Sorting = System.Windows.Forms.SortOrder.Ascending;
			this.listView1.TabIndex = 0;
			this.listView1.TabStop = false;
			this.listView1.TileSize = new System.Drawing.Size(144, 66);
			this.listView1.UseCompatibleStateImageBehavior = false;
			this.listView1.MouseClick += new System.Windows.Forms.MouseEventHandler(this.listView1_MouseClick);
			this.listView1.DragDrop += new System.Windows.Forms.DragEventHandler(this.listView1_DragDrop);
			this.listView1.DragEnter += new System.Windows.Forms.DragEventHandler(this.listView1_DragEnter);
			// 
			// Form1
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
			this.ClientSize = new System.Drawing.Size(400, 300);
			this.Controls.Add(this.spc);
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
			this.MinimumSize = new System.Drawing.Size(400, 300);
			this.Name = "Form1";
			this.ShowInTaskbar = false;
			this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
			this.Text = "Form1";
			this.Deactivate += new System.EventHandler(this.Form1_Deactivate);
			this.Load += new System.EventHandler(this.Form1_Load);
			this.MouseUp += new System.Windows.Forms.MouseEventHandler(this.Form1_MouseUp);
			this.Paint += new System.Windows.Forms.PaintEventHandler(this.Form1_Paint);
			this.MouseDown += new System.Windows.Forms.MouseEventHandler(this.Form1_Down);
			this.MouseMove += new System.Windows.Forms.MouseEventHandler(this.Form1_MouseMove);
			this.notifyMenu.ResumeLayout(false);
			this.spc.Panel2.ResumeLayout(false);
			this.spc.ResumeLayout(false);
			this.ResumeLayout(false);

		}

		#endregion

		private System.Windows.Forms.NotifyIcon notifyIcon1;
		private System.Windows.Forms.SplitContainer spc;
		private System.Windows.Forms.ListView listView1;
		private System.Windows.Forms.ContextMenuStrip notifyMenu;
		private System.Windows.Forms.ToolStripMenuItem notifyExit;

	}
}

