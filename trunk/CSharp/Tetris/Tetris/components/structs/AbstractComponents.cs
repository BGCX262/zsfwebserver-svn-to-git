using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Tetris.components.box {
    /// <summary>
    /// 游戏组件抽象类
    /// </summary>
    public abstract class AbstractComponents {
        protected Tetris game;

        private void setGame(Tetris game) {
            this.game = game;
        }

		/// <summary>
		/// 绘制
		/// </summary>
        public abstract void Draw();

		/// <summary>
		/// 加载
		/// </summary>
        public abstract void init();

        public AbstractComponents(Tetris game) {
			setGame(game);
            init();
        }

    }
}
