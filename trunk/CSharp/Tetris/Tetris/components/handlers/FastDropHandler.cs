using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 快速下落处理类
	/// </summary>
	public class FastDropHandler : IInputHandler {

		public override void HandlerInput(InputState input) {
			if (input.CurrentState.IsKeyDown(key) && !input.LastKeyState.IsKeyDown(key)) {
				do {
					ctrl.dropBox.MoveDown();
				} while (ctrl.dropBox.position.Y > -BoxUtil.getTop(ctrl.dropBox.boxData) * DropBox.size);
			}
		}
	}
}
