using Tetris.components.box;
using Microsoft.Xna.Framework.Graphics;
using System.IO;

namespace Tetris.components.handlers {
	/// <summary>
	/// 更改背景图片处理类
	/// </summary>
	class ChangeBackgroundHandler : IInputHandler {

		public override void HandlerInput(InputState input) {
			if (!input.LastKeyState.IsKeyDown(key) && input.CurrentState.IsKeyDown(key)) {
				int size = Directory.GetFiles(@"Content\imgs", "bg?.xnb", SearchOption.TopDirectoryOnly).Length;
				if (ctrl.currentBG == size)
					ctrl.currentBG = 0;
				ctrl.bigStage.Stop();
				Texture2D bg = (Texture2D)ctrl.lib["bg" + ++ctrl.currentBG];
				ctrl.bigStage.NewBackground = bg;
				ctrl.bigStage.NewBackgroundIndex = ctrl.currentBG;
			}
		}

	}
}
