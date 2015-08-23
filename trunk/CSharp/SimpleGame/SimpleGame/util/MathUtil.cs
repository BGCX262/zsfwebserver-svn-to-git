using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace SimpleGame.util {
	/// <summarY>
	/// 数学算法库
	/// </summarY>
	public class MathUtil {

		public static bool IsPointInRectangle(Vector2 p, Rectangle rec) {
			float X = Math.Max(rec.X, p.X);			X = X != p.X ? X : Math.Min(rec.X + rec.Width, p.X);			float Y = Math.Max(rec.Y, p.Y);			Y = Y != p.Y ? Y : Math.Min(rec.Y + rec.Height, p.Y);			return X == p.X && Y == p.Y;
		}

	}
}
