using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Tetris.components.controller;

namespace Tetris {
	/// <summary>
	/// 俄罗斯方块游戏
	/// </summary>
	public class Tetris : Microsoft.Xna.Framework.Game {
		public GraphicsDeviceManager graphics;		// 图形设备管理器
		public SpriteBatch spriteBatch;				// 精灵集群

		public Controller ctrl;						// 控制中心

		public int speed = 10;						// 游戏速度
		int FPS = 0;								// 当前FPS
		int count = 0;
		float time=0;

		public Tetris() {
			graphics = new GraphicsDeviceManager(this);
			Content.RootDirectory = "Content";
		}

		/// <summary>
		/// 初始化
		/// </summary>
		protected override void Initialize() {
			/* 垂直同步 */
			//graphics.SynchronizeWithVerticalRetrace = false;
			IsFixedTimeStep = true;
			TargetElapsedTime = new TimeSpan(0, 0, 0, 0, speed);

			spriteBatch = new SpriteBatch(GraphicsDevice);
			ctrl = new Controller(this);
			ctrl.init();

			Window.Title = "Tetris - Author: D-io";

			IsMouseVisible = true;
		}

		/// <summary>
		/// 注销资源
		/// </summary>
		protected override void UnloadContent() {
			Content.Unload();
		}

		/// <summary>
		/// 编辑游戏逻辑,如更新,检测碰撞,收集输入,播放声音等
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Update(GameTime gameTime) {
			ctrl.input.Update();
			ctrl.ListenKeyEvent();
			ctrl.CheckGameState();
		}

		/// <summary>
		/// 渲染对象和屏幕背景
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Draw(GameTime gameTime) {
			GraphicsDevice.Clear(Color.Black);

			time += (float) gameTime.ElapsedRealTime.TotalSeconds;

			count++;
			if (time > 1.0f) {
				time -= (int)time;
				FPS = count;
				count = 0;
			}

			spriteBatch.Begin(SpriteBlendMode.AlphaBlend);
			ctrl.Draw();
			spriteBatch.DrawString(ctrl.gameFont, "FPS: " + FPS, new Vector2(10, 10), Color.White);
			spriteBatch.End();
		}
	}
}
