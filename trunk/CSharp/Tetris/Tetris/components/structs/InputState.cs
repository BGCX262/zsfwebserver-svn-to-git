using Microsoft.Xna.Framework.Input;

namespace Tetris.components.box {
	public class InputState {
		public KeyboardState LastKeyState = new KeyboardState();		// 上次按键，如果没按则为Keys.None（实时更新）
		public KeyboardState CurrentState = new KeyboardState();		// 当前按键，如果没按则为Keys.None（实时更新）

		/// <summary>
		/// 更新上次按键和当前按键
		/// </summary>
		public void Update() {
			LastKeyState = CurrentState;
			CurrentState = Keyboard.GetState();
		}

	}
}
