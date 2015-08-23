#region Using statements
using System;
using System.Collections.Generic;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Storage;
using FarseerGames.FarseerPhysics;
using FarseerGames.FarseerPhysics.Dynamics;
using FarseerGames.FarseerPhysics.Dynamics.Joints;
using FarseerGames.FarseerPhysics.Factories;
using FarseerGames.FarseerPhysics.Collisions;
using FarseerGames.FarseerPhysics.Dynamics.Springs;

#endregion

namespace MyFarseerTest1 {
	/// <summary>
	/// This is the main type for your game
	/// </summary>
	public class Game1 : Microsoft.Xna.Framework.Game {
		public GraphicsDeviceManager graphics;		// 图形设备管理器
		SpriteBatch spriteBatch;			// 精灵集群
		GraphicsEx gex;

		Texture2D box1;		// 正方形
		Texture2D box2;		// 圆
		Texture2D box3;		// 矩形（地）

		PhysicsSimulator physicsSimulator;		// 物理引擎全局

		//Body boxBody1;		// 正方形的占位
		Body boxBody2;		// 圆的占位
		Body boxBody3;		// 大地的占位

		//Geom boxGeometry1;	// 正方形的碰撞规则
		Geom boxGeometry2;	// 圆的碰撞规则
		Geom boxGeometry3;	// 大地的碰撞规则

		SliderJoint sliderJoint;	// 连接两个对象

		Geom _pickedGeom;		//
		FixedLinearSpring _mousePickSpring;

		public Game1() {
			/* 初始化图形设备管理器 */
			graphics = new GraphicsDeviceManager(this);

			/* 设置材质资源管理器的根目录 */
			Content.RootDirectory = "Content";		
		}

		/// <summary>
		/// 初始化
		/// </summary>
		protected override void Initialize() {
			/* 设置最大100fps */
			IsFixedTimeStep = true;
			/* 1毫秒渲染100fps */
			TargetElapsedTime = new TimeSpan(0, 0, 0, 0, 1);

			/* 初始化物理引擎全局，设置为9.8向下重力 */
			physicsSimulator = new PhysicsSimulator(new Vector2(0f, 1000f));
			/* 设置摩擦为平均值 */
			//physicsSimulator.FrictionType = FrictionType.Minimum;

			/* 初始化正方形的占位，给定大小，质量还有位置 */
			//boxBody1 = BodyFactory.Instance.CreateRectangleBody(physicsSimulator, 50, 50, 79 * 50 * 50);
			//boxBody1.Position = new Vector2(400, 100);

			/* 设置减速比率 */
			//boxBody1.LinearDragCoefficient = 1.5f;

			/* 将正方形固定在初始化处 */
			//boxBody1.IsStatic = true;

			/* 初始化正方形碰撞检测 */
			//boxGeometry1 = GeomFactory.Instance.CreateRectangleGeom(physicsSimulator, boxBody1, 50, 50);

			/* 初始化圆形的占位，给定大小，质量还有位置 */
			boxBody2 = BodyFactory.Instance.CreateCircleBody(physicsSimulator, 25, 1);
			boxBody2.Position = new Vector2(400, 400);

			/* 减速比率 */
			boxBody2.LinearDragCoefficient = 0.9f;
			/* 滑行减速比率 */
			boxBody2.LinearVelocity = new Vector2(10f);
			/* 减转速比率 */
			boxBody2.RotationalDragCoefficient = 25f;
	

			/* 初始化正方形碰撞检测 */
			boxGeometry2 = GeomFactory.Instance.CreateCircleGeom(physicsSimulator, boxBody2, 25, 60);

			/* 初始化大地的占位，给定大小，质量还有位置 */
			boxBody3 = BodyFactory.Instance.CreateRectangleBody(physicsSimulator, 1440, 10, 79f * (4 / 3 * 3.1416f * 25 * 25 * 25));
			boxBody3.Position = new Vector2(500, 580);
			boxBody3.LinearDragCoefficient = 0.9f;

			/* 将大地固定在初始化处 */
			boxBody3.IsStatic = true;

			/* 初始化大地碰撞检测 */
			boxGeometry3 = GeomFactory.Instance.CreateRectangleGeom(physicsSimulator, boxBody3, 1440, 10);

			/* 将正方形和圆形组合起来，最大距离为300，最小距离为50 */
			//sliderJoint = JointFactory.Instance.CreateSliderJoint(physicsSimulator,
			//	boxBody1, new Vector2(25, 25), boxBody2, new Vector2(25, 25), 50, 300);

			gex = new GraphicsEx(this);
			gex.LoadContent();

			/* 鼠标可见 */
			IsMouseVisible = true;

			base.Initialize();
		}

		/// <summary>
		/// 加载纹理资源
		/// </summary>
		/// <param name="loadAllContent"></param>
		[Obsolete]
		protected override void LoadGraphicsContent(bool loadAllContent) {
			if (loadAllContent) {
				spriteBatch = new SpriteBatch(graphics.GraphicsDevice);

				box1 = Content.Load<Texture2D>("1");
				box2 = Content.Load<Texture2D>("2c");
				box3 = Content.Load<Texture2D>("3");
			}
		}

		/// <summary>
		/// 注销纹理资源
		/// </summary>
		/// <param name="unloadAllContent"></param>
		[Obsolete]
		protected override void UnloadGraphicsContent(bool unloadAllContent) {
			if (unloadAllContent) {
				Content.Unload();
			}
		}

		/// <summary>
		/// 编辑游戏逻辑,如更新,检测碰撞,收集输入,播放声音等 (循环方法)
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Update(GameTime gameTime) {
			/* 手柄第一个键退出 */
			if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
				this.Exit();

			/* 键盘esc退出 */
			if (Keyboard.GetState().IsKeyDown(Keys.Escape))
				Exit();

			/*------------------------移动圆形监听--------------------------*/
			if (Keyboard.GetState().IsKeyDown(Keys.Left)) {
				boxBody2.ApplyForce(new Vector2(-400, 0));
				boxBody2.ApplyTorque(-5000f);
			}

			if (Keyboard.GetState().IsKeyDown(Keys.Right)) {
				boxBody2.ApplyForce(new Vector2(400, 0));
				boxBody2.ApplyTorque(5000f);
			}

			if (Keyboard.GetState().IsKeyDown(Keys.Up))
				boxBody2.ApplyForce(new Vector2(0, -400));

			if (Keyboard.GetState().IsKeyDown(Keys.Down))
				boxBody2.ApplyForce(new Vector2(0, 400));
			/*-----------------------移动圆形监听结束-----------------------*/

			/* 物理引擎运行，注释后物理引擎失效 */
			physicsSimulator.Update(gameTime.ElapsedGameTime.Milliseconds * 0.001f);

			//HandlerMouseInput();

			base.Update(gameTime);
		}

		private void HandlerMouseInput() {
			MouseState state = Mouse.GetState();

			Vector2 point = new Vector2(state.X, state.Y);
			if (state.LeftButton == ButtonState.Pressed) {
				//create mouse spring
				_pickedGeom = physicsSimulator.Collide(point);
				if (_pickedGeom != null) {
					_mousePickSpring = SpringFactory.Instance.CreateFixedLinearSpring(physicsSimulator,
																					  _pickedGeom.Body,
																					  _pickedGeom.Body.
																						  GetLocalPosition(point),
																					  point, 100, 50);
				}
			} else if (state.LeftButton == ButtonState.Released) {
				//destroy mouse spring
				if (_mousePickSpring != null && _mousePickSpring.IsDisposed == false) {
					_mousePickSpring.Dispose();
					_mousePickSpring = null;
				}
			}

			//move anchor point
			if (state.LeftButton == ButtonState.Pressed && _mousePickSpring != null) {
				_mousePickSpring.WorldAttachPoint = point;
			}
		}

		/// <summary>
		/// 渲染对象和屏幕背景 (循环方法)
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Draw(GameTime gameTime) {
			GraphicsDevice.Clear(Color.CornflowerBlue);

			spriteBatch.Begin(SpriteBlendMode.AlphaBlend);

			/* 绘制三个形状 */
			//spriteBatch.Draw(box1, boxBody1.Position, null, Color.White, boxBody1.Rotation,
			//    new Vector2(box1.Width / 2, box1.Height / 2), 1, SpriteEffects.None, 0);
			spriteBatch.Draw(box2, boxBody2.Position, null, Color.White, boxBody2.Rotation,
				new Vector2(box2.Width / 2, box2.Height / 2), 1, SpriteEffects.None, 0);
			spriteBatch.Draw(box3, boxBody3.Position, null, Color.White, boxBody3.Rotation,
				new Vector2(box3.Width / 2, box3.Height / 2), 1, SpriteEffects.None, 0);

			if (_mousePickSpring != null) {
				Vector2 start = _mousePickSpring.Body.GetWorldPosition(_mousePickSpring.BodyAttachPoint);
				Vector2 end = _mousePickSpring.WorldAttachPoint;
				gex.DrawLine(start.X, start.Y, end.X, end.Y);
				Window.Title = "start   x: " + start.X + "\ty: " + start.Y + "\tend    x: " + end.X + "\ty: " + end.Y;
				//gex.DrawLine(5, 5, 1440, 900);
			}

			spriteBatch.End();

			base.Draw(gameTime);
		}
	}
}
