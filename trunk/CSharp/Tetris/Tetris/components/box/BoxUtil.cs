using Microsoft.Xna.Framework.Graphics;

namespace Tetris.components.box {
	/// <summary>
	/// 方块的工具类，记录方块的属性
	/// S、Z、L、J、I、O、T
	/// </summary>
	public class BoxUtil {
		/********************************/
		/*			方块的形状			*/
		/********************************/
		public static readonly int[,] S_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 0, 1, 0},
			{0, 1, 1, 0},
			{0, 1, 0, 0}
		};

		public static readonly int[,] Z_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 1, 0},
			{0, 0, 1, 0}
		};

		public static readonly int[,] L_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 1, 0}
		};

		public static readonly int[,] J_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 0, 1, 0},
			{0, 0, 1, 0},
			{0, 1, 1, 0}
		};

		public static readonly int[,] I_BOX = new int[,] { 
			{0, 0, 1, 0},
			{0, 0, 1, 0},
			{0, 0, 1, 0},
			{0, 0, 1, 0}
		};

		public static readonly int[,] O_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 1, 0},
			{0, 0, 0, 0}
		};

		public static readonly int[,] T_BOX = new int[,] { 
			{0, 0, 0, 0},
			{0, 1, 0, 0},
			{0, 1, 1, 0},
			{0, 1, 0, 0}
		};

		/********************************/
		/*			方块对应的颜色		*/
		/********************************/
		public static readonly Color S_COLOR = Color.Yellow;
		public static readonly Color Z_COLOR = Color.Violet;
		public static readonly Color L_COLOR = Color.Turquoise;
		public static readonly Color J_COLOR = Color.YellowGreen;
		public static readonly Color I_COLOR = Color.SpringGreen;
		public static readonly Color O_COLOR = Color.SkyBlue;
		public static readonly Color T_COLOR = Color.SeaGreen;


		/********************************/
		/*			基本判断方法			*/
		/********************************/
		/// <summary>
		/// 获得方块的左边距离
		/// </summary>
		/// <returns></returns>
		public static int getLeft(int[,] boxData) {
			int i = 0;

			/* 检测左边距离 */
			for (; i < boxData.GetLength(0); i++) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[j, i] == 1)
						return i;
				}
			}
			return i;
		}

		/// <summary>
		/// 获得方块的右边距离
		/// </summary>
		/// <returns></returns>
		public static int getRight(int[,] boxData) {
			int i = boxData.GetLength(1) - 1;

			/* 检测右边距离 */
			for (; i >= 0; i--) {
				for (int j = boxData.GetLength(1) - 1; j >= 0; j--) {
					if (boxData[j, i] == 1)
						return i + 1;
				}
			}
			return i + 1;
		}

		/// <summary>
		/// 获得方块的底部距离
		/// </summary>
		/// <returns></returns>
		public static int getBottom(int[,] boxData) {
			int i = boxData.GetLength(0) - 1;

			/* 检测底部距离 */
			for (; i >= 0; i--) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[i, j] == 1)
						return i + 1;
				}
			}
			return i + 1;
		}

		/// <summary>
		/// 获得方块顶部的距离
		/// </summary>
		/// <param name="boxData"></param>
		/// <returns></returns>
		public static int getTop(int[,] boxData) {
			int i = 0;

			/* 检测底部距离 */
			for (; i < boxData.GetLength(0); i++) {
				for (int j = 0; j < boxData.GetLength(1); j++) {
					if (boxData[i, j] == 1)
						return i;
				}
			}
			return i;
		}

		/// <summary>
		/// 下落时检查方块碰撞
		/// </summary>
		/// <returns></returns>
		public static bool CheckBoxByBottom(DropBox box, DropedBox[,] stageBoxs) {
			/* 检测方块碰撞 */
			for (int i = 0, c = 0; i < box.boxData.GetLength(0) && c < 4; i++) {
				for (int j = box.boxData.GetLength(1) - 1; j >= 0; j--) {
					if (box.boxData[j, i] == 1) {
						c++;
						if ((int)box.position.Y / DropBox.size + j + 1 < stageBoxs.GetLength(0)) {
						    if (stageBoxs[(int)box.position.Y / DropBox.size + j + 1, 
										(int)box.position.X / DropBox.size + i] != null) {
						        return false;
						    }
						}
					}
				}
			}
			return true;
		}

		/// <summary>
		/// 右移时检查方块碰撞
		/// </summary>
		/// <returns></returns>
		public static bool CheckBoxByRight(DropBox box, DropedBox[,] stageBoxs) {
			/* 检测方块碰撞 */
			for (int i = 0, c = 0; i < box.boxData.GetLength(0) && c < 4; i++) {
				for (int j = box.boxData.GetLength(1) - 1; j >= 0; j--) {
					if (box.boxData[j, i] == 1) {
						c++;
						if ((int)box.position.X / DropBox.size + i + 1 < stageBoxs.GetLength(1)) {
						    if (stageBoxs[(int)box.position.Y / DropBox.size + j, 
										(int)box.position.X / DropBox.size + i + 1] != null) {
						        return false;
						    }
						}
					}
				}
			}
			return true;
		}

		/// <summary>
		/// 右移时检查方块碰撞
		/// </summary>
		/// <returns></returns>
		public static bool CheckBoxByLeft(DropBox box, DropedBox[,] stageBoxs) {
			/* 检测方块碰撞 */
			for (int i = 0, c = 0; i < box.boxData.GetLength(0) && c < 4; i++) {
				for (int j = box.boxData.GetLength(1) - 1; j >= 0; j--) {
					if (box.boxData[j, i] == 1) {
						c++;
						if ((int)box.position.X / DropBox.size + i - 1 > 0) {
						    if (stageBoxs[(int)box.position.Y / DropBox.size + j, 
										(int)box.position.X / DropBox.size + i - 1] != null) {
						        return false;
						    }
						}
					}
				}
			}
			return true;
		}
	}
}
