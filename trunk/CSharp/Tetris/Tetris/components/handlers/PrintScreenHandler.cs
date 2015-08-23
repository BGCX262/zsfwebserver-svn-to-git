using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Tetris.components.box;
using System.Drawing;
using System.Windows.Forms;

namespace Tetris.components.handlers {
	/// <summary>
	/// 截屏处理类
	/// </summary>
	class PrintScreenHandler : IInputHandler {
		public override void HandlerInput(InputState input) {
			if (input.CurrentState.IsKeyDown(key) && !input.LastKeyState.IsKeyDown(key)) {
				Microsoft.Xna.Framework.Rectangle rec = ctrl.tetris.Window.ClientBounds;
				Image img = new Bitmap(rec.Width, rec.Height);
				Graphics graphics = Graphics.FromImage(img);
				graphics.CopyFromScreen(new Point(rec.X, rec.Y), new Point(0, 0), new Size(rec.Width, rec.Height));
				/* 保存剪贴板 */
				Clipboard.SetImage(img);
				//DateTime dateTime = System.DateTime.Now;
				/* 保存至文件 */
				//img.Save(dateTime.Year + "-" + dateTime.Month + "-" + dateTime.Day + " " + 
				//    dateTime.Hour + ";" + dateTime.Minute + ";" + dateTime.Second + ".png");
				graphics.Dispose();
				img.Dispose();
			}
		}
	}
}
