package com.yule27.toModel.logic {
import com.yule27.toModel.components.SimpleDoor;
import com.yule27.toModel.components.SimpleFloor;
import com.yule27.toModel.components.SimpleLine;
import com.yule27.toModel.components.SimplePolyline;
import com.yule27.toModel.components.SimpleWindow;


/**
 *
 * @author zsf
 */
public class Contents {
	// 所有线条集合
	public static var lines : Vector.<SimpleLine> = new Vector.<SimpleLine>();
	// 多段线集合
	public static var polylines : Vector.<SimplePolyline> = new Vector.<SimplePolyline>();
	// 窗户集合
	public static var windows : Vector.<SimpleWindow> = new Vector.<SimpleWindow>();
	// 飘窗集合
	public static var bayWindows : Vector.<SimpleWindow> = new Vector.<SimpleWindow>();
	// 门集合
	public static var doors : Vector.<SimpleDoor> = new Vector.<SimpleDoor>();
	// 地板集合
	public static var floors : Vector.<SimpleFloor> = new Vector.<SimpleFloor>();
	
	public static var enableWall : Boolean = false;
	public static var enableWindow : Boolean = false;
	public static var enableBayWindow : Boolean = false;
	public static var enableDoor : Boolean = false;
	public static var enableFloor : Boolean = false;

	public function Contents() {
	}

	/**
	 * 添加直线
	 * @param line
	 */
	public static function addLine(line : SimpleLine) : void {
		for each (var item : SimpleLine in lines) {
			if (item.startX == line.startX && line.startY == item.startY && line.startZ == item.startZ &&
				item.endX == line.endX && item.endY == line.endY && item.endZ == line.endZ)
				return;
		}
		lines.push(line);
	}

	/**
	 * 排序直线
	 * @param lines
	 */
	public static function sortLines(lines : Vector.<SimpleLine>) : void {
		if (lines.length <= 0)
			return;
		var temp : Vector.<SimpleLine> = new Vector.<SimpleLine>();
		for (var i : int = 0; i < lines.length; i++) {
			var li : SimpleLine = lines[i] as SimpleLine;
			temp.push(li);
			for (var j : int = i + 1; j < lines.length; j++) {
				var lj : SimpleLine = lines[j] as SimpleLine;
				if ((li.endX == lj.startX && li.endY == lj.startY && li.endZ == lj.startZ) && j - i > 1) {
					lines.splice(i + 1, 0, lj); //这个地方有点问题
					lines.splice(j + 1, 1);
				}
			}
		}
		lines = temp;
	}

	/**
	 * 清理所有内容
	 */
	public static function clear() : void {
		lines = null;
		polylines = null;
	}
}
}