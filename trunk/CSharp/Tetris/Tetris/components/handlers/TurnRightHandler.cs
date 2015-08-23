using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 方块组右翻转处理类
	/// </summary>
	class TurnRightHandler : IInputHandler {
		public override void HandlerInput(InputState input) {
			if (!input.LastKeyState.IsKeyDown(key) && input.CurrentState.IsKeyDown(key)) {
				ctrl.dropBox.TurnRight();
			}
		}
	}
}
