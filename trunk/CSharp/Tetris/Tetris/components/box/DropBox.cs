using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Tetris.components.stage;
using System.Collections;
using Tetris.components.controller;
using System.Threading;

namespace Tetris.components.box {
	public class DropBox : AbstractComponents {

		/********************************/
		/*			方块的属性			*/
		/********************************/
		private GameStage gameStage;				// 方块所在的游戏舞台
		public Texture2D Background { set; get; }	// 方块的背景图片（无色）
		public Color color { set; get; }			// 方块的背景图片颜色
		public int[,] boxData;						// 方块的形状数据
		public Vector2 position { set; get; }		// 方块的位置
		public static int size;					// 方块的大小
		public String nextBox;						// 下一个方块

		/********************************/
		/*			  所有形状			*/
		/********************************/
		public const string S_BOX = "S";
		public const string Z_BOX = "Z";
		public const string L_BOX = "L";
		public const string J_BOX = "J";
		public const string I_BOX = "I";
		public const string O_BOX = "O";
		public const string T_BOX = "T";

		/// <summary>
		/// 设置方块形状和颜色
		/// </summary>
		/// <param name="type"></param>
		public Box GetBoxData(string type) {
			Box box = new Box();
			switch (type) {
				case S_BOX:
					box.boxData = BoxUtil.S_BOX;
					box.color = BoxUtil.S_COLOR;
					break;
				case Z_BOX:
					box.boxData = BoxUtil.Z_BOX;
					box.color = BoxUtil.Z_COLOR;
					break;
				case L_BOX:
					box.boxData = BoxUtil.L_BOX;
					box.color = BoxUtil.L_COLOR;
					break;
				case J_BOX:
					box.boxData = BoxUtil.J_BOX;
					box.color = BoxUtil.J_COLOR;
					break;
				case I_BOX:
					box.boxData = BoxUtil.I_BOX;
					box.color = BoxUtil.I_COLOR;
					break;
				case O_BOX:
					box.boxData = BoxUtil.O_BOX;
					box.color = BoxUtil.O_COLOR;
					break;
				case T_BOX:
					box.boxData = BoxUtil.T_BOX;
					box.color = BoxUtil.T_COLOR;
					break;
				default:
					break;
			}
			return box;
		}

		/// <summary>
		/// 向左移动
		/// </summary>
		public void MoveLeft() {
			/* 检测碰撞 */
			if (position.X / size > -BoxUtil.getLeft(boxData) && 
					BoxUtil.CheckBoxByLeft(this, gameStage.stageBoxs)) {
				Vector2 pstion = position;
				pstion.X -= size;
				position = pstion;
			}
		}

		/// <summary>
		/// 向右移动
		/// </summary>
		public void MoveRight() {
			/* 检测碰撞 */
			int length = gameStage.stageBoxs.GetLength(1);
			if (position.X / size + BoxUtil.getRight(boxData) < length && 
					BoxUtil.CheckBoxByRight(this, gameStage.stageBoxs)) {
				Vector2 pstion = position;
				pstion.X += size;
				position = pstion;
			}
		}

		/// <summary>
		/// 自动下落方块
		/// </summary>
		public void AutoDrop() {
			while (true) {
				Thread.Sleep(Math.Max(1000 - (game.ctrl.pointStage.level - 1) * 100, 50));
				if (!Controller.IsGameOver) {
					MoveDown();
				}
			}
		}

		/// <summary>
		/// 向下移动
		/// </summary>
		public void MoveDown() {
			/* 检测碰撞 */
			int length = gameStage.stageBoxs.GetLength(0);
			if (position.Y / size + BoxUtil.getBottom(boxData) < length && 
					BoxUtil.CheckBoxByBottom(this, gameStage.stageBoxs)) {
				Vector2 pstion = position;
				pstion.Y += size;
				position = pstion;
			} else {
				/* 将方块放进游戏舞台里并重新落下方块 */
				gameStage.AddDropBox(this);
				DropNewBox(nextBox);

				/* 判断是否满足消除的条件 */
				Queue rows = gameStage.GetFullRows();
				if (rows.Count > 0) {
					gameStage.DeleteRowByRows(rows);
				}
			}
		}

		/// <summary>
		/// 左翻转
		/// </summary>
		public void TurnLeft() {
			int[,] tempData = new int[4, 4];

			/* 矩阵翻转 */
			for (int i = 0; i < boxData.GetLength(0); i++) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[i, j] == 1) {
						tempData[boxData.GetLength(0) - j - 1, i] = boxData[i, j];
					}
				}
			}
			/* 翻转 */
			if (CheckTurn(tempData)) {
				boxData = tempData;
			}
		}

		/// <summary>
		/// 右翻转
		/// </summary>
		public void TurnRight() {
			int[,] tempData = new int[4, 4];

			/* 矩阵翻转 */
			for (int i = 0; i < boxData.GetLength(0); i++) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[i, j] == 1) {
						tempData[j, boxData.GetLength(0) - i - 1] = boxData[i, j];
					}
				}
			}
			
			/* 翻转 */
			if (CheckTurn(tempData)) {
				boxData = tempData;
			}
		}

		/// <summary>
		/// 判断是否允许翻转(可能添加预翻转功能，即检测向左或右移动1~2格是否允许翻转，若允许则移动后翻转)
		/// </summary>
		/// <returns></returns>
		private bool CheckTurn(int[,] tempData) {
			/* 检测碰撞墙壁 */
			for (int i = 0; i < tempData.GetLength(0); i++) {
				for (int j = 0; j < tempData.GetLength(1); j++) {
					if (tempData[i, j] == 1) {
						int x = (int)position.X / size;
						int y = (int)position.Y / size;
						if (x + j < 0 || x + j > gameStage.stageBoxs.GetLength(1) - 1 || 
								y + i > gameStage.stageBoxs.GetLength(0) - 1 || y + i < 0) {
							return false;
						} else if (gameStage.stageBoxs[y + i, x + j] != null) {
							return false;
						}
					}
				}
			}
			return true;
		}

		/// <summary>
		/// 重新落下一个方块
		/// </summary>
		public void DropNewBox(string type) {
			Box box = GetBoxData(type);
			boxData = box.boxData;
			color = box.color;

			/* 随机翻转0-4次方块 */
			RandomTurn();

			/* 更改位置 */
			ReloadPosition();

			/* 检查是否游戏结束 */
			CheckGameOver();

			/* 生成下一个随机方块 */
			string[] strArr = new string[] { S_BOX, Z_BOX, L_BOX, J_BOX, I_BOX, O_BOX, T_BOX };
			Random rd = new Random();
			nextBox = strArr[rd.Next(strArr.Length)];

			game.ctrl.pointStage.nextBox = GetBoxData(nextBox);
		}

		/// <summary>
		/// 随机翻转0-4次方块
		/// </summary>
		public void RandomTurn() {
			Random rd = new Random();
			int count = rd.Next(5);
			for (int i = 0; i < count; i++) {
				int[,] tempData = new int[4, 4];

				for (int l = 0; l < boxData.GetLength(0); l++) {
					for (int j = 0; j < boxData.GetLength(1); j++) {
						if (boxData[l, j] == 1) {
							tempData[boxData.GetLength(0) - j - 1, l] = boxData[l, j];
						}
					}
				}
				boxData = tempData;
			}
		}

		/// <summary>
		/// 重新加载位置
		/// </summary>
		public void ReloadPosition() {
			Vector2 p = position;
			p.X = 3 * size;
			p.Y = 0 - BoxUtil.getTop(boxData) * size;
			position = p;
		}

		/// <summary>
		/// 判断是否游戏结束
		/// </summary>
		private void CheckGameOver() {
			/* 检测碰撞 */
			int length = gameStage.stageBoxs.GetLength(0);
			if (!(position.Y / size + BoxUtil.getBottom(boxData) < length && 
					BoxUtil.CheckBoxByBottom(this, gameStage.stageBoxs))) {
				Controller.IsGameOver = true;
			}
		}

		/// <summary>
		/// 根据position绘制方块组
		/// </summary>
		/// <param name="spriteBatch"></param>
		public override void Draw() {
			for (int i = 0; i < boxData.GetLength(0); i++ ) {
				for (int j = 0; j < boxData.GetLength(1); j++ ) {
					if (boxData[i, j] == 1) {
						game.spriteBatch.Draw(Background, new Rectangle(
							Int32.Parse(gameStage.Bounds.X + position.X + j * size + ""),
							Int32.Parse(gameStage.Bounds.Y + position.Y + i * size + ""), size, size), color);
					}
				}
			}
		}

        /// <summary>
        /// 加载
        /// </summary>
        public override void init() {
            gameStage = game.ctrl.gameStage;
			/* 随机一个形状 */
			string[] strArr = new string[] { S_BOX, Z_BOX, L_BOX, J_BOX, I_BOX, O_BOX, T_BOX };
			Random rd = new Random();
			DropNewBox(strArr[rd.Next(strArr.Length)]);
        }

		public DropBox(Tetris game) : base(game) {}

	}
}
