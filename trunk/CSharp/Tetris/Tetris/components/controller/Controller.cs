using System;
using System.Collections;
using System.Reflection;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Tetris.components.stage;
using Tetris.components.box;
using System.Threading;
using Tetris.components.structs;

namespace Tetris.components.controller {
	/// <summary>
	/// 游戏控制器
	/// </summary>
	public class Controller {

		/********************************/
		/*			游戏逻辑声明		*/
		/********************************/
		int gameStageWidth			= 250;		// 游戏舞台宽度
		int gameStageHeight			= 500;		// 游戏舞台高度
		int boxSize					= 25;		// 游戏的方块大小
		float AlphaChange			= 0.01f;	// 背景透明度过渡值
		public int inputInterval	= 50;		// 操作间隔
		public int currentBG		= 1;		// 当前背景图片索引

		public Tetris tetris;					// 程序入口
		public InputState input;				// 输入设备监听
		public Hashtable table;					// 键盘注册映射对象
		public Thread DropBoxThread;			// 方块掉落线程
		public static bool IsGameOver;		// 游戏是否已结束

		/********************************/
		/*			游戏对象声明		*/
		/********************************/
		public BigStage bigStage;				// 界面舞台
		public GameStage gameStage;				// 游戏舞台
		public PointStage pointStage;			// 计分舞台
		public DropBox dropBox;					// 掉落的方块组
		public SpriteFont gameFont;				// 游戏字体
		public ResourcesLIB lib;				// 资源库

		/// <summary>
		/// 初始化对象
		/// </summary>
		public void InitObjects() {
			Rectangle clientBounds = tetris.Window.ClientBounds;

			BigStage.AlphaChange = AlphaChange;
			bigStage = new BigStage(tetris);

			/* 在整个场景的中间绘制游戏场景 */
			DropBox.size = boxSize;
			gameStage = new GameStage(new Rectangle((clientBounds.Width - gameStageWidth) / 2,
				(clientBounds.Height - gameStageHeight) / 2, gameStageWidth, gameStageHeight), tetris);

			pointStage = new PointStage(tetris);

			dropBox = new DropBox(tetris);

			input = new InputState();

			table = new Hashtable();

			DropBoxThread = new Thread(new ThreadStart(dropBox.AutoDrop));

			lib = new ResourcesLIB(tetris.Content);

		}

		/// <summary>
		/// 初始化属性
		/// </summary>
		public void InitParameters() {

			/* 加载资源 */
			bigStage.Background		= (Texture2D)	lib["bg1"];
			gameStage.Background	= (Texture2D)	lib["black"];
			gameStage.BorderColor	= (Texture2D)	lib["white"];
			dropBox.Background		= (Texture2D)	lib["box"];

			/* 加载字体 */
			gameFont				= (SpriteFont)	lib["gameFont"];

			/* 初始化参数 */
			DropBoxThread.IsBackground = true;
		}

		/// <summary>
		/// 重新初始化
		/// </summary>
		public void Reload() {

			/* 游戏舞台 */
			gameStage.stageBoxs = new DropedBox[Bounds.Height / DropBox.size, Bounds.Width / DropBox.size];

			/* 计分面板 */
			pointStage.point = 0;
			pointStage.level = 1;

			/* 下落方块 */

		}

		/// <summary>
		/// 注册按键事件
		/// </summary>
		public void RegistHotKeys() {
			string path = "Tetris.components.handlers.";

			table.Add(Keys.Left,	path + "MoveLeftHandler");
			table.Add(Keys.Right,	path + "MoveRightHandler");
			table.Add(Keys.Down,	path + "MoveDownHandler");
			table.Add(Keys.C,		path + "FastDropHandler");
			table.Add(Keys.Z,		path + "TurnLeftHandler");
			table.Add(Keys.X,		path + "TurnRightHandler");
			table.Add(Keys.Space,	path + "ChangeBackgroundHandler");
			table.Add(Keys.Escape,	path + "QuiteHandler");
			table.Add(Keys.F10,		path + "PrintScreenHandler");
		}

		/// <summary>
		/// 监听键盘按键
		/// </summary>
		public void ListenKeyEvent() {
			/* 当有按键按下的时候进入判断 */
			if (input.CurrentState.GetPressedKeys().Length > 0) {
				/* 遍历所有按下的键 */
				for (int i = 0; i < input.CurrentState.GetPressedKeys().Length; i++) {
					/* 获取当前按键 */
					Keys thisKey = input.CurrentState.GetPressedKeys()[i];
					Object obj = table[thisKey];
					if (obj == null) continue;
					string classpath = obj.ToString();
					if (classpath == null || "".Equals(classpath)) continue;

					/* 通过反射获得对应的按键处理类 */
					Assembly asm = Assembly.GetExecutingAssembly();
					Type t = asm.GetType(classpath);
					if (t == null) continue;
					Object obj1 = Activator.CreateInstance(t);

					/* 执行处理类 */
					IInputHandler handler = obj1 as IInputHandler;
					if (handler == null) continue;
					handler.setController(this);
					handler.setKey(thisKey);
					handler.HandlerInput(input);
				}
			}
		}

		/// <summary>
		/// 检查游戏状态，如果游戏结束则停止接收控制
		/// </summary>
		public void CheckGameState() {
			if (IsGameOver) {
			    table.Remove(Keys.Left);
			    table.Remove(Keys.Right);
			    table.Remove(Keys.Down);
			    table.Remove(Keys.C);
			    table.Remove(Keys.Up);
			    table.Remove(Keys.Z);
			    table.Remove(Keys.X);
			}
		}

		/// <summary>
		/// 调用所有对象的绘图方法
		/// </summary>
		public void Draw() {
			bigStage.Draw();
			gameStage.Draw();
			dropBox.Draw();
			pointStage.Draw();
		}

		/// <summary>
		/// 加载
		/// </summary>
		public void init() {
			InitObjects();
			InitParameters();
			RegistHotKeys();
			DropBoxThread.Start();
		}

		/// <summary>
		/// 构造函数
		/// </summary>
		/// <param name="tetris">俄罗斯方块主类</param>
		public Controller(Tetris tetris) {
			this.tetris = tetris;
		}

	}
}
