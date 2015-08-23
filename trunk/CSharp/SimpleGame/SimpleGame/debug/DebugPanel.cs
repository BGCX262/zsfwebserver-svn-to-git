using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using SimpleGame.util;

namespace SimpleGame.debug {
	/// <summary>
	/// 调试面板
	/// </summary>
	public class DebugPanel : DrawableGameComponent {

		public enum Side {
			TopLeft,
			TopRight,
			BottomLeft,
			BottomRight
		}

		#region Fields

        private SpriteBatch spriteBatch;
        private SpriteFont font;
		private Texture2D background;
		private Rectangle bounds;

		private Side _side;
		private int _fps;
		public bool enableDebug = true;

		/* fps */
		private int frameCount = 0;
		private int calSecond = -1;

		public int collisions = 0;
		public int keyDowns = 0;

		/* bounds */
		private Rectangle[] rectangles;

		#endregion

		public DebugPanel(Game game) : base(game) {
			_side = Side.TopLeft;
		}

		public DebugPanel(Game game, Side side) : base(game) {
			_side = side;
		}

		#region Load and Unload

		protected override void LoadContent() {
			base.LoadContent();

			spriteBatch = new SpriteBatch(GraphicsDevice);
			font = Game.Content.Load<SpriteFont>("debugfont");
			background = Game.Content.Load<Texture2D>("blank");

			int width = 110;
			int height = 90;
			Rectangle ClientBounds = Game.Window.ClientBounds;
			rectangles = new Rectangle[] {
				new Rectangle(0, 0, width, height),
				new Rectangle(ClientBounds.Width - width, 0, width, height),
				new Rectangle(0, ClientBounds.Height - height, width, height),
				new Rectangle(ClientBounds.Width - width, ClientBounds.Height - height, width, height)
			};
			bounds = rectangles[(int)_side];
		}

		protected override void UnloadContent() {
			base.UnloadContent();
		}

		#endregion

		#region Update and Draw

		public override void Update(GameTime gameTime) {
			base.Update(gameTime);

			if (enableDebug) {
				MouseState mState = Mouse.GetState();
				Vector2 mousePos = new Vector2(mState.X, mState.Y);
				
				if (MathUtil.IsPointInRectangle(mousePos, bounds)) {
					_side = (++_side) > Side.BottomRight ? Side.TopLeft : _side;
					bounds = rectangles[(int)_side];
				}

				KeyboardState kState = Keyboard.GetState();
				keyDowns = kState.GetPressedKeys().Length;
			}

		}

		public override void Draw(GameTime gameTime) {
			base.Draw(gameTime);

			if (enableDebug) {
				/* fps */
				if (calSecond != gameTime.TotalGameTime.Seconds) {
					calSecond = gameTime.TotalGameTime.Seconds;
					_fps = frameCount;
					frameCount = 0;
				}

				spriteBatch.Begin(SpriteBlendMode.AlphaBlend);

				spriteBatch.Draw(background, bounds, new Color(Color.Black, 50));
				spriteBatch.DrawString(font, "Debug Panel", new Vector2(bounds.X + 5, bounds.Y + 5), Color.White);
				spriteBatch.DrawString(font, "FPS: " + _fps, new Vector2(bounds.X + 5, bounds.Y + 18), Color.White);
				spriteBatch.DrawString(font, "Collisions: " + collisions, new Vector2(bounds.X + 5, bounds.Y + 31), Color.White);
				spriteBatch.DrawString(font, "KeyDowns: " + keyDowns, new Vector2(bounds.X + 5, bounds.Y + 44), Color.White);
				int seconds = gameTime.TotalGameTime.Seconds + gameTime.TotalGameTime.Minutes * 60 + gameTime.TotalGameTime.Hours * 60 * 60;
				spriteBatch.DrawString(font, "GameTime: " + seconds, new Vector2(bounds.X + 5, bounds.Y + 57), Color.White);

				spriteBatch.End();
				frameCount++;
			}
		}

		#endregion

	}
}
