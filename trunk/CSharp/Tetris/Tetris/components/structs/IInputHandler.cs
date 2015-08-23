using Microsoft.Xna.Framework.Input;
using Tetris.components.controller;

namespace Tetris.components.box {
	public abstract class IInputHandler {

		protected Controller ctrl;
		protected Keys key;

		public void setController(Controller ctrl) {
			this.ctrl = ctrl;
		}

		public void setKey(Keys key) {
			this.key = key;
		}

		public abstract void HandlerInput(InputState input);

	}
}
