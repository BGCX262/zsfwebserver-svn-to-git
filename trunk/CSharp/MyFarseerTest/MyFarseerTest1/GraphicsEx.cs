using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace MyFarseerTest1 {
	public class GraphicsEx {
		private Game1 game;
		private GraphicsDeviceManager gdm;
		private SpriteBatch spBatch;
		private SpriteFont font;
		private BasicEffect effect;
		private Color color;
		private Color imageColor;
		private SpriteEffects flip;
		private float rotation;
		private Vector2 origin;
		private VertexDeclaration vd;

		public GraphicsEx(Game1 game) {
			this.game = game;
			this.gdm = game.graphics;
			this.gdm.PreferredBackBufferWidth = 1280;
			this.gdm.PreferredBackBufferHeight = 720;
		}

		public void LoadContent() {
			this.spBatch = new SpriteBatch(gdm.GraphicsDevice);
			this.font = game.Content.Load<SpriteFont>("chinesefont");
			this.effect = new BasicEffect(gdm.GraphicsDevice, null);
			this.effect.VertexColorEnabled = true;
			this.color = Color.White;
			this.imageColor = Color.White;
			this.flip = SpriteEffects.None;
			this.rotation = 0.0f;
			this.origin = Vector2.Zero;
			this.vd = new VertexDeclaration(gdm.GraphicsDevice,
				VertexPositionColor.VertexElements);
		}
		public GraphicsDeviceManager GetGraphicsDeviceManager() {
			return gdm;
		}
		public void SetColor(int red, int green, int blue) {
			this.color = new Color((byte)red, (byte)green, (byte)blue);
		}
		public void SetColor(int red, int green, int blue, int alpha) {
			this.color = new Color((byte)red, (byte)green, (byte)blue, (byte)alpha);
		}
		public void SetFont(String fontName) {
			font = game.Content.Load<SpriteFont>(fontName);
		}

		public float StringWidth(String text) {
			return font.MeasureString(text).X;
		}

		public float StringHeight(String text) {
			return font.MeasureString(text).Y;
		}

		public void SetImageColor(int red, int green, int blue) {
			this.imageColor = new Color((byte)red, (byte)green, (byte)blue);
		}

		public void SetImageColor(int red, int green, int blue, int alpha) {
			this.imageColor = new Color((byte)red, (byte)green, (byte)blue, (byte)alpha);
		}

		public void SetFlip(SpriteEffects flip) {
			this.flip = flip;
		}

		public void SetImageRotation(float rotation) {
			this.rotation = rotation;
		}

		public void SetImageOrigin(float x, float y) {
			this.origin = new Vector2(x, y);
		}

		public Texture2D LoadImage(String name) {
			return game.Content.Load<Texture2D>(name);
		}

		public Texture2D CreateImage(Texture2D image, int x, int y, int w, int h) {
			Rectangle srcRect = new Rectangle(x, y, w, h);
			byte[] data = new byte[srcRect.Width * srcRect.Height * 4];
			image.GetData(0, srcRect, data, 0, data.Length);
			Texture2D result = new ResolveTexture2D(
				gdm.GraphicsDevice,
				srcRect.Width, srcRect.Height, 0, SurfaceFormat.Color);
			result.SetData(data);
			return result;
		}

		public void Clear() {
			gdm.GraphicsDevice.Clear(color);
		}

		public void DrawString(String text, float x, float y) {
			if (text == null) return;
			Vector2 pos = new Vector2(x, y);
			spBatch.Begin();
			spBatch.DrawString(font, text, pos, color);
			spBatch.End();
		}

		public void DrawBoldString(String text, float x, float y) {
			DrawString(text, x - 1, y - 1);
			DrawString(text, x, y - 1);
			DrawString(text, x + 1, y - 1);
			DrawString(text, x - 1, y);
			DrawString(text, x + 1, y);
			DrawString(text, x - 1, y + 1);
			DrawString(text, x, y + 1);
			DrawString(text, x + 1, y + 1);
		}

		public void DrawImage(Texture2D image, float x, float y) {
			if (image == null) return;
			Rectangle rect = new Rectangle((int)x, (int)y, image.Width, image.Height);
			Rectangle srect = new Rectangle(0, 0, image.Width, image.Height);
			spBatch.Begin();
			spBatch.Draw(image, rect, srect, imageColor, rotation, origin, flip, 0);
			spBatch.End();
		}

		public void DrawImage(Texture2D image, float x, float y, float w, float h) {
			if (image == null) return;
			Rectangle rect = new Rectangle((int)x, (int)y, (int)w, (int)h);
			Rectangle srect = new Rectangle(0, 0, image.Width, image.Height);
			spBatch.Begin();
			spBatch.Draw(image, rect, srect, imageColor, rotation, origin, flip, 0);
			spBatch.End();
		}

		public void DrawImage(Texture2D image, float x, float y, float w, float h, float sx, float sy, float sw, float sh) {
			if (image == null) return;
			Rectangle rect = new Rectangle((int)x, (int)y, (int)w, (int)h);
			Rectangle srect = new Rectangle((int)sx, (int)sy, (int)sw, (int)sh);
			spBatch.Begin();
			spBatch.Draw(image, rect, srect, imageColor, rotation, origin, flip, 0);
			spBatch.End();
		}

		public void DrawLine(float x0, float y0, float x1, float y1) {
			float cw = gdm.PreferredBackBufferWidth / 2;
			float ch = gdm.PreferredBackBufferHeight / 2;
			effect.Begin();
			gdm.GraphicsDevice.VertexDeclaration = vd;
			VertexPositionColor[] v = new VertexPositionColor[2];
			v[0] = new VertexPositionColor(new Vector3(-1f + x0 / cw, 1f - y0 / ch, 0), color);
			v[1] = new VertexPositionColor(new Vector3(-1f + x1 / cw, 1f - y1 / ch, 0), color);
			foreach (EffectPass pass in effect.CurrentTechnique.Passes) {
				pass.Begin();
				gdm.GraphicsDevice.DrawUserPrimitives<VertexPositionColor>(
					PrimitiveType.LineList, v, 0, 1);
				pass.End();
			}
			effect.End();
		}

		public void DrawRect(float x, float y, float w, float h) {
			float cw = gdm.PreferredBackBufferWidth / 2;
			float ch = gdm.PreferredBackBufferHeight / 2;
			effect.Begin();
			gdm.GraphicsDevice.VertexDeclaration = vd;
			VertexPositionColor[] v = new VertexPositionColor[5];
			v[0] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y) / ch, 0), color);
			v[1] = new VertexPositionColor(new Vector3(-1f + (x + w) / cw, 1f - (y) / ch, 0), color);
			v[2] = new VertexPositionColor(new Vector3(-1f + (x + w) / cw, 1f - (y + h) / ch, 0), color);
			v[3] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y + h) / ch, 0), color);
			v[4] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y) / ch, 0), color);
			foreach (EffectPass pass in effect.CurrentTechnique.Passes) {
				pass.Begin();
				gdm.GraphicsDevice.DrawUserPrimitives<VertexPositionColor>(
					PrimitiveType.LineStrip, v, 0, 4);
				pass.End();
			}
			effect.End();
		}

		public void FillRect(float x, float y, float w, float h) {
			float cw = gdm.PreferredBackBufferWidth / 2;
			float ch = gdm.PreferredBackBufferHeight / 2;
			effect.Begin();
			gdm.GraphicsDevice.VertexDeclaration = vd;
			VertexPositionColor[] v = new VertexPositionColor[6];
			v[0] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y) / ch, 0), color);
			v[1] = new VertexPositionColor(new Vector3(-1f + (x + w) / cw, 1f - (y) / ch, 0), color);
			v[2] = new VertexPositionColor(new Vector3(-1f + (x + w) / cw, 1f - (y + h) / ch, 0), color);
			v[3] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y) / ch, 0), color);
			v[4] = new VertexPositionColor(new Vector3(-1f + (x + w) / cw, 1f - (y + h) / ch, 0), color);
			v[5] = new VertexPositionColor(new Vector3(-1f + (x) / cw, 1f - (y + h) / ch, 0), color);
			foreach (EffectPass pass in effect.CurrentTechnique.Passes) {
				pass.Begin();
				gdm.GraphicsDevice.DrawUserPrimitives<VertexPositionColor>(
					PrimitiveType.TriangleList, v, 0, 2);
				pass.End();
			}
			effect.End();
		}
	}
}