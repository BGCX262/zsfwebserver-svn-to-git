using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Tetris.components.box;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace Tetris.components.stage {
	/// <summary>
	/// 计分舞台
	/// </summary>
	public class PointStage : AbstractComponents {

		/********************************/
		/*			舞台内容声明			*/
		/********************************/
		public Box nextBox { set; get; }			// 下一个方块
		public Vector2 position { set; get; }		// 位置

		public int point;							// 当前分数
		public int level = 1;						// 当前等级

		/// <summary>
		/// 绘制
		/// </summary>
		public override void Draw() {
			/* 背景 */
			game.spriteBatch.Draw((Texture2D)game.ctrl.lib["white"],
				new Rectangle((int)position.X, (int)position.Y, 150, 500), new Color(Color.Gray, 0.5f));

			/* 绘制下一个方块 */
			for (int i = 0; i < nextBox.boxData.GetLength(0); i++) {
				for (int j = 0; j < nextBox.boxData.GetLength(1); j++) {
					if (nextBox.boxData[i, j] == 1) {
						game.spriteBatch.Draw((Texture2D)game.ctrl.lib["box"],
							new Rectangle((int)position.X + 25 + j * DropBox.size, (int)position.Y + 25 + i * DropBox.size,
								DropBox.size, DropBox.size), nextBox.color);
					}
				}
			}

			/* 绘制分数 */
			game.spriteBatch.Draw((Texture2D)game.ctrl.lib["textfield"], position + new Vector2(25, 147), new Color(Color.White, 0.5f));
			game.spriteBatch.DrawString((SpriteFont)game.ctrl.lib["gameFont"], "Point: " + point, position + new Vector2(30, 150), Color.White);

			/* 绘制等级 */
			game.spriteBatch.Draw((Texture2D)game.ctrl.lib["textfield"], position + new Vector2(25, 177), new Color(Color.White, 0.5f));
			game.spriteBatch.DrawString((SpriteFont)game.ctrl.lib["gameFont"], "Level: " + level, position + new Vector2(30, 180), Color.White);


		}

		/// <summary>
		/// 加载
		/// </summary>
		public override void init() {
			Rectangle gameBounds = game.ctrl.gameStage.Bounds;
			position = new Vector2(gameBounds.X + gameBounds.Width + 2, gameBounds.Y);
		}

		public PointStage(Tetris game) : base(game) { }
	}
}
