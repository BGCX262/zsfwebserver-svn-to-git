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
		public GraphicsDeviceManager graphics;		// ͼ���豸������
		SpriteBatch spriteBatch;			// ���鼯Ⱥ
		GraphicsEx gex;

		Texture2D box1;		// ������
		Texture2D box2;		// Բ
		Texture2D box3;		// ���Σ��أ�

		PhysicsSimulator physicsSimulator;		// ��������ȫ��

		//Body boxBody1;		// �����ε�ռλ
		Body boxBody2;		// Բ��ռλ
		Body boxBody3;		// ��ص�ռλ

		//Geom boxGeometry1;	// �����ε���ײ����
		Geom boxGeometry2;	// Բ����ײ����
		Geom boxGeometry3;	// ��ص���ײ����

		SliderJoint sliderJoint;	// ������������

		Geom _pickedGeom;		//
		FixedLinearSpring _mousePickSpring;

		public Game1() {
			/* ��ʼ��ͼ���豸������ */
			graphics = new GraphicsDeviceManager(this);

			/* ���ò�����Դ�������ĸ�Ŀ¼ */
			Content.RootDirectory = "Content";		
		}

		/// <summary>
		/// ��ʼ��
		/// </summary>
		protected override void Initialize() {
			/* �������100fps */
			IsFixedTimeStep = true;
			/* 1������Ⱦ100fps */
			TargetElapsedTime = new TimeSpan(0, 0, 0, 0, 1);

			/* ��ʼ����������ȫ�֣�����Ϊ9.8�������� */
			physicsSimulator = new PhysicsSimulator(new Vector2(0f, 1000f));
			/* ����Ħ��Ϊƽ��ֵ */
			//physicsSimulator.FrictionType = FrictionType.Minimum;

			/* ��ʼ�������ε�ռλ��������С����������λ�� */
			//boxBody1 = BodyFactory.Instance.CreateRectangleBody(physicsSimulator, 50, 50, 79 * 50 * 50);
			//boxBody1.Position = new Vector2(400, 100);

			/* ���ü��ٱ��� */
			//boxBody1.LinearDragCoefficient = 1.5f;

			/* �������ι̶��ڳ�ʼ���� */
			//boxBody1.IsStatic = true;

			/* ��ʼ����������ײ��� */
			//boxGeometry1 = GeomFactory.Instance.CreateRectangleGeom(physicsSimulator, boxBody1, 50, 50);

			/* ��ʼ��Բ�ε�ռλ��������С����������λ�� */
			boxBody2 = BodyFactory.Instance.CreateCircleBody(physicsSimulator, 25, 1);
			boxBody2.Position = new Vector2(400, 400);

			/* ���ٱ��� */
			boxBody2.LinearDragCoefficient = 0.9f;
			/* ���м��ٱ��� */
			boxBody2.LinearVelocity = new Vector2(10f);
			/* ��ת�ٱ��� */
			boxBody2.RotationalDragCoefficient = 25f;
	

			/* ��ʼ����������ײ��� */
			boxGeometry2 = GeomFactory.Instance.CreateCircleGeom(physicsSimulator, boxBody2, 25, 60);

			/* ��ʼ����ص�ռλ��������С����������λ�� */
			boxBody3 = BodyFactory.Instance.CreateRectangleBody(physicsSimulator, 1440, 10, 79f * (4 / 3 * 3.1416f * 25 * 25 * 25));
			boxBody3.Position = new Vector2(500, 580);
			boxBody3.LinearDragCoefficient = 0.9f;

			/* ����ع̶��ڳ�ʼ���� */
			boxBody3.IsStatic = true;

			/* ��ʼ�������ײ��� */
			boxGeometry3 = GeomFactory.Instance.CreateRectangleGeom(physicsSimulator, boxBody3, 1440, 10);

			/* �������κ�Բ�����������������Ϊ300����С����Ϊ50 */
			//sliderJoint = JointFactory.Instance.CreateSliderJoint(physicsSimulator,
			//	boxBody1, new Vector2(25, 25), boxBody2, new Vector2(25, 25), 50, 300);

			gex = new GraphicsEx(this);
			gex.LoadContent();

			/* ���ɼ� */
			IsMouseVisible = true;

			base.Initialize();
		}

		/// <summary>
		/// ����������Դ
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
		/// ע��������Դ
		/// </summary>
		/// <param name="unloadAllContent"></param>
		[Obsolete]
		protected override void UnloadGraphicsContent(bool unloadAllContent) {
			if (unloadAllContent) {
				Content.Unload();
			}
		}

		/// <summary>
		/// �༭��Ϸ�߼�,�����,�����ײ,�ռ�����,���������� (ѭ������)
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Update(GameTime gameTime) {
			/* �ֱ���һ�����˳� */
			if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
				this.Exit();

			/* ����esc�˳� */
			if (Keyboard.GetState().IsKeyDown(Keys.Escape))
				Exit();

			/*------------------------�ƶ�Բ�μ���--------------------------*/
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
			/*-----------------------�ƶ�Բ�μ�������-----------------------*/

			/* �����������У�ע�ͺ���������ʧЧ */
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
		/// ��Ⱦ�������Ļ���� (ѭ������)
		/// </summary>
		/// <param name="gameTime"></param>
		protected override void Draw(GameTime gameTime) {
			GraphicsDevice.Clear(Color.CornflowerBlue);

			spriteBatch.Begin(SpriteBlendMode.AlphaBlend);

			/* ����������״ */
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
