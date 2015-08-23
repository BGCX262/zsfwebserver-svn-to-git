using Tetris.components.box;

namespace Tetris.components.handlers {
	/// <summary>
	/// 退出游戏处理类
	/// </summary>
	class QuiteHandler : IInputHandler {
		public override void HandlerInput(InputState input) {
			if (!input.LastKeyState.IsKeyDown(key) && input.CurrentState.IsKeyDown(key)) {
				ctrl.tetris.Exit();
			}
		}
	}
}
