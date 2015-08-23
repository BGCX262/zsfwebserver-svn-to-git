
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;
using Tetris.components.stage;
using System.IO;
using System.Threading;
using System;

namespace Tetris.components.box {
	/// <summary>
	/// 已掉下的方块
	/// </summary>
	public class DropedBox : AbstractComponents {

		GameStage gameStage;						// 游戏舞台

		/********************************/
		/*			方块属性声明			*/
		/********************************/
		public Texture2D Background { set; get; }	// 背景图片(无色)
		public Color color { set; get; }			// 方块的背景图片颜色
		public Vector2 position { set; get; }		// 方块的位置
		public Texture2D[] Breaks;					// 方块破碎效果
		public bool isBreak;						// 是否进行破碎效果

		/// <summary>
		/// 方块破碎并销毁
		/// </summary>
		public void BreakAndDistory() {
			InitBreak();
			//Thread thread = new Thread(new ThreadStart(Break));
			//thread.IsBackground = true;
			//thread.Start();
			//Break();
		}

		public void Break() {
			for (int i = 0; i < Breaks.Length; i++) {
				game.spriteBatch.Draw(Breaks[i], new Vector2(position.X - 37.5f, position.Y - 37.5f), Color.White);
				Thread.Sleep(50);
			}
			//GC.SuppressFinalize(this);
		}

		/// <summary>
		/// 加载破碎效果所需要的素材
		/// </summary>
		private void InitBreak() {
			Breaks = new Texture2D[12];
			for (int i = 0; i < Breaks.Length; i++) {
				Breaks[i] = (Texture2D)game.ctrl.lib["break" + (i + 1)];
			}
			isBreak = true;
		}

		/// <summary>
		/// 绘制
		/// </summary>
		int index = 0;		// 绘制的效果索引
		int wait = 5;		// n倍减速绘制
		public override void Draw() {
			int x = (int)position.X + gameStage.Bounds.X;
			int y = (int)position.Y + gameStage.Bounds.Y;
			game.spriteBatch.Draw(Background, new Rectangle(x, y, DropBox.size, DropBox.size), color);

			/* 爆炸效果 */
			if (isBreak) {
				/* 绘制爆炸效果 */
				game.spriteBatch.Draw(Breaks[index], new Vector2(x - 37.5f, y - 37.5f), Color.White);

				/* 5次刷新才更新一次爆炸效果 */
				if (wait-- > 1) {
					return ;
				} else {
					wait = 5;
				}
				index++;

				/* 爆炸效果结束 落下上方的方块 */
				if (index > Breaks.Length - 1) {

					int j = (int)position.X / DropBox.size;
					for (int i = (int)position.Y / DropBox.size - 1; i >= 0; i--) {
						game.ctrl.gameStage.stageBoxs[i + 1, j] = game.ctrl.gameStage.stageBoxs[i, j];
						game.ctrl.gameStage.stageBoxs[i, j] = null;
						if (game.ctrl.gameStage.stageBoxs[i + 1, j] != null) {
							game.ctrl.gameStage.stageBoxs[i + 1, j].position += new Vector2(0, DropBox.size);
						}
					}
				}
			}
		}

		/// <summary>
		/// 加载
		/// </summary>
        public override void init() {
            gameStage = game.ctrl.gameStage;
        }

		public DropedBox(Tetris game) : base(game) {}

	}
}
