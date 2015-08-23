using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using Tetris.components.box;

namespace Tetris.components.stage {
	/// <summary>
	/// 整个舞台
	/// </summary>
	public class BigStage : AbstractComponents {

		/********************************/
		/*			舞台属性声明			*/
		/********************************/
		public Texture2D Background { set; get; }		// 背景图片
		public Texture2D NewBackground { set; get; }	// 新背景图片
		public int BackgroundIndex = -1;
		public int NewBackgroundIndex = -1;
		public float Alpha = 0f;
		public static float AlphaChange = 0.01f;

		/// <summary>
		/// 绘制舞台元素
		/// </summary>
		/// <param name="spriteBatch"></param>
		public override void Draw() {
			/* 判断是否已更改背景图片 */
			if (BackgroundIndex != NewBackgroundIndex) {
				/* 如果已经结束变换背景 */
				if (Alpha == 1) {
					BackgroundIndex = NewBackgroundIndex;
					Background = NewBackground;
					Alpha = 0f;
					return ;
				}

				/* 绘制两张渐变过渡背景图片 */
				game.spriteBatch.Draw(Background, Vector2.Zero, new Color(Color.White, 1-Alpha));
				game.spriteBatch.Draw(NewBackground, Vector2.Zero, new Color(Color.White, Alpha));
				/* 背景透明度过渡值 */
				Alpha += AlphaChange;
			} else {
				game.spriteBatch.Draw(Background, Vector2.Zero, Color.White);
			}
			
			/* 绘制游戏说明 */
			string[] strArr = {
								  "Game Details: ",
								  "Press direction keys to move", 
								  "Z key turn left", 
								  "X key turn right", 
								  "C key break down", 
								  "F10 key copy screen"
							  };
			for (int i = 0; i < strArr.Length; i++) {
				game.spriteBatch.DrawString((SpriteFont)game.ctrl.lib["gameFont"], strArr[i], new Vector2(10, 460 + i * 22), Color.White);
			}
		}

		/// <summary>
		/// 立刻结束变换背景
		/// </summary>
		public void Stop() {
			if (NewBackground != null)
				Background = NewBackground;
			BackgroundIndex = NewBackgroundIndex;
			Alpha = 0f;
		}

		public BigStage(Tetris game) : base(game) {}

        /// <summary>
        /// 加载
        /// </summary>
        public override void init() {
            
        }

	}
}
