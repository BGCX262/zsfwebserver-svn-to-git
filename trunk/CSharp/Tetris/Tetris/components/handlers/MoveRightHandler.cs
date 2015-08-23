using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 向右移动处理类
	/// </summary>
	class MoveRightHandler : IInputHandler {

		public override void HandlerInput(InputState input) {
			if (input.CurrentState.IsKeyDown(key) && !input.LastKeyState.IsKeyDown(key)) {
				ctrl.dropBox.MoveRight();
			}
		}

	}
}
