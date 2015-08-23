using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 向左移动处理类
	/// </summary>
	public class MoveLeftHandler : IInputHandler {

		public override void HandlerInput(InputState input) {
			if (input.CurrentState.IsKeyDown(key) && !input.LastKeyState.IsKeyDown(key)) {
				ctrl.dropBox.MoveLeft();
			}
		}
	}
}
