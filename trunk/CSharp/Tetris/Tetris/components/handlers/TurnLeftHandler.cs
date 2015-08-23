using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 方块组左翻转处理类
	/// </summary>
	class TurnLeftHandler : IInputHandler {
		public override void HandlerInput(InputState input) {
			if (!input.LastKeyState.IsKeyDown(key) && input.CurrentState.IsKeyDown(key)) {
				ctrl.dropBox.TurnLeft();
			}
		}
	}
}
