using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Tetris.components.controller;

namespace Tetris {
	/// <summary>
	/// ����˹������Ϸ
	/// </summary>
	public class Tetris : Microsoft.Xna.Framework.Game {
		public GraphicsDeviceManager graphics;		// ͼ���豸������
		public SpriteBatch spriteBatch;				// ���鼯Ⱥ

		public Controller ctrl;						// ��������

		public int speed = 10;						// ��Ϸ�ٶ�
		int FPS = 0;								// ��ǰFPS
		int count = 0;
		float time=0;

		public Tetris() {
			graphics = new GraphicsDeviceManager(this);
			Content.RootDirectory = "Content";
		}

		/// <summary>
		/// ��ʼ��
		/// </summary>
		protected override void Initialize() {
			/* ��ֱͬ�� */
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
		/// ע����Դ
		/// </summary>
		protected override void UnloadContent() {
			Content.Unload();
		}

		/// <summary>
		/// �༭��Ϸ�߼�,�����,�����ײ,�ռ�����,����������
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Update(GameTime gameTime) {
			ctrl.input.Update();
			ctrl.ListenKeyEvent();
			ctrl.CheckGameState();
		}

		/// <summary>
		/// ��Ⱦ�������Ļ����
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
