using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using System;
using System.Collections;
using Tetris.components.box;
using System.Threading;

namespace Tetris.components.stage {
	/// <summary>
	/// 游戏舞台
	/// </summary>
	public class GameStage : AbstractComponents {

		/********************************/
		/*			舞台数据声明			*/
		/********************************/
		public DropedBox[,] stageBoxs;					// 已落下的方块数组

		/********************************/
		/*			舞台属性声明			*/
		/********************************/
		public Rectangle Bounds { set; get; }			// 位置和大小
		public Texture2D Background { set; get; }		// 背景图片
		public Texture2D BorderColor { set; get; }		// 边框图片

		/// <summary>
		/// 将方块添加进游戏舞台
		/// </summary>
		/// <param name="dropBox"></param>
		public void AddDropBox(DropBox dropBox) {
			int[,] boxData = dropBox.boxData;
			for (int i = 0; i < boxData.GetLength(0); i++) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[i, j] == 1) {
						DropedBox db = new DropedBox(game);
						db.color = dropBox.color;
						db.Background = dropBox.Background;
						db.position = new Vector2(dropBox.position.X + j * DropBox.size, 
							dropBox.position.Y + i * DropBox.size);
						stageBoxs[(int)dropBox.position.Y / DropBox.size + i, 
							(int)dropBox.position.X / DropBox.size + j] = db;
					}
				}
			}
		}

		/// <summary>
		/// 获得满足消除的行数队列
		/// </summary>
		/// <returns></returns>
		public Queue GetFullRows() {
			Queue queue = new Queue();

			bool isFullRow = false;
			for (int i = 0; i < stageBoxs.GetLength(0); i++) {
				isFullRow = true;
				for (int j = 0; j < stageBoxs.GetLength(1); j++) {
					if (stageBoxs[i, j] == null || stageBoxs[i, j].isBreak)
						isFullRow = false;
				}
				if (isFullRow) {
					queue.Enqueue(i);
				}
			}

			return queue;
		}

		/// <summary>
		/// 根据队列消除队列中的行
		/// </summary>
		/// <param name="rows"></param>
		public void DeleteRowByRows(Queue rows) {
			/* 根据行数加分 */
			if (rows.Count == 4)
			    game.ctrl.pointStage.point += 8;
			else if (rows.Count == 3)
			    game.ctrl.pointStage.point += 4;
			else if (rows.Count == 2 || rows.Count == 1)
			    game.ctrl.pointStage.point += rows.Count;

			game.ctrl.pointStage.level = game.ctrl.pointStage.point / 20;

			/* 遍历消除的行 */
			while (rows.Count > 0) {
				int row = Int32.Parse(rows.Dequeue().ToString());

				/* 删除指定行 */
				for (int i = 0; i < stageBoxs.GetLength(1); i++) {
					/* 这里可以添加爆炸特效 */
					stageBoxs[row, i].BreakAndDistory();
				}
				/* 将此行上方的方块全部落下 */
				//DropOneRowByRow(row);
			}
		}

		/// <summary>
		/// 根据给定行号把该行上方的方块全部向下落一格
		/// </summary>
		/// <param name="row"></param>
		private void DropOneRowByRow(int row) {
			for (int i = row - 1; i >= 0; i--) {
				for (int j = 0; j < stageBoxs.GetLength(1); j++) {
					stageBoxs[i + 1, j] = stageBoxs[i, j];
					stageBoxs[i, j] = null;
					if (stageBoxs[i + 1, j] != null) {
						stageBoxs[i + 1, j].position += new Vector2(0, DropBox.size);
					}
				}
			}
		}

		/// <summary>
		/// 绘制游戏舞台元素
		/// </summary>
		/// <param name="spriteBatch"></param>
		public override void Draw() {
			/* 画边框 */
			int borderWidth = 2;
			game.spriteBatch.Draw(BorderColor, new Rectangle(Bounds.X - borderWidth, Bounds.Y - borderWidth, 
				Bounds.Width + borderWidth * 2, borderWidth), new Color(Color.LightGray, 0.75f));
			game.spriteBatch.Draw(BorderColor, new Rectangle(Bounds.X - borderWidth, Bounds.Y,
				borderWidth, Bounds.Height + borderWidth), new Color(Color.LightGray, 0.75f));
			game.spriteBatch.Draw(BorderColor, new Rectangle(Bounds.X + Bounds.Width, Bounds.Y, 
				borderWidth, Bounds.Height + borderWidth), new Color(Color.LightGray, 0.75f));
			game.spriteBatch.Draw(BorderColor, new Rectangle(Bounds.X, Bounds.Y + Bounds.Height, 
				Bounds.Width, borderWidth), new Color(Color.LightGray, 0.75f));

			/* 画中间游戏区域 */
			game.spriteBatch.Draw(Background, Bounds, new Color(Color.LightGray, 0.75f));

			/* 画所有的已落下方块 */
			for (int i = 0; i < stageBoxs.GetLength(0); i++) {
				for (int j = 0; j < stageBoxs.GetLength(1); j++) {
					if (stageBoxs[i, j] != null) {
						stageBoxs[i, j].Draw();
					}
				}
			}
		}

		/// <summary>
		/// 根据位置和大小初始化一个游戏舞台
		/// </summary>
		/// <param name="bounds"></param>
		public GameStage(Rectangle bounds, Tetris game) : base(game) {
			Bounds = bounds;
            stageBoxs = new DropedBox[Bounds.Height / DropBox.size, Bounds.Width / DropBox.size];
		}

        /// <summary>
        /// 加载
        /// </summary>
        public override void init() {
            stageBoxs = new DropedBox[Bounds.Height / DropBox.size, Bounds.Width / DropBox.size];
        }

	}
}
