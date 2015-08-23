using System.Collections;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.Graphics;

namespace Tetris.components.structs {
	/// <summary>
	/// 游戏资源库
	/// </summary>
	public class ResourcesLIB : Hashtable {

		private ContentManager Content;	// 资源管理器

		/// <summary>
		/// 加载所有资源
		/// </summary>
		private void initResources() {
			/* 图片资源 */
			Add("bg1",		Content.Load<Texture2D>(@"imgs\bg1"));
			Add("bg2",		Content.Load<Texture2D>(@"imgs\bg2"));
			Add("bg3",		Content.Load<Texture2D>(@"imgs\bg3"));
			Add("black",	Content.Load<Texture2D>(@"imgs\black"));
			Add("white",	Content.Load<Texture2D>(@"imgs\white"));
			Add("box",		Content.Load<Texture2D>(@"imgs\box"));
			Add("textfield",Content.Load<Texture2D>(@"imgs\textfield"));
			Add("break1",	Content.Load<Texture2D>(@"imgs\break\1"));
			Add("break2",	Content.Load<Texture2D>(@"imgs\break\2"));
			Add("break3",	Content.Load<Texture2D>(@"imgs\break\3"));
			Add("break4",	Content.Load<Texture2D>(@"imgs\break\4"));
			Add("break5",	Content.Load<Texture2D>(@"imgs\break\5"));
			Add("break6",	Content.Load<Texture2D>(@"imgs\break\6"));
			Add("break7",	Content.Load<Texture2D>(@"imgs\break\7"));
			Add("break8",	Content.Load<Texture2D>(@"imgs\break\8"));
			Add("break9",	Content.Load<Texture2D>(@"imgs\break\9"));
			Add("break10",	Content.Load<Texture2D>(@"imgs\break\10"));
			Add("break11",	Content.Load<Texture2D>(@"imgs\break\11"));
			Add("break12",	Content.Load<Texture2D>(@"imgs\break\12"));

			/* 字体资源 */
			Add("gameFont",	Content.Load<SpriteFont>(@"fonts\gameFont"));
		}

		public ResourcesLIB(ContentManager Content) {
			this.Content = Content;
			initResources();
		}
	}
}
