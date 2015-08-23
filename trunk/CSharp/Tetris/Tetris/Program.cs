
using System;

namespace Tetris {
	public static class Program {
		/// <summary>
		/// ³ÌĞòÈë¿Ú
		/// </summary>
		/// <param name="args"></param>
		[STAThread]
		public static void Main(string[] args) {
			using (Tetris game = new Tetris()) {
				game.Run();
				//Thread thread = new Thread(new ThreadStart(game.Run));
				//thread.Start();
			}
		}
	}
}

