using Tetris.components.box;

namespace Tetris.components.handlers {

	/// <summary>
	/// 向下移动处理类
	/// </summary>
	class MoveDownHandler : IInputHandler {
		public override void HandlerInput(InputState input) {
			if (input.CurrentState.IsKeyDown(key) && !input.LastKeyState.IsKeyDown(key)) {
				ctrl.dropBox.MoveDown();
			}
		}
	}
}
