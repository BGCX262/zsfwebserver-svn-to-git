package com.yule27.toModel.components {

/**
 * 窗户实例
 * @author zsf
 */
public class SimpleWindow {
	private var _point3DArr : Vector.<Point3D>;

	public function SimpleWindow() {
	}

	public static function getInstance(points : Vector.<Number>) : SimpleWindow {
		var window : SimpleWindow = new SimpleWindow();
		window.point3DArr = new Vector.<Point3D>(points.length / 2);
		var j : int = 0;
		for (var i : int = 0; i < points.length - 1; i += 2) {
			j = (i + 1) / 2;
			window.point3DArr[j] = Point3D.Point3Dvector(new <Number>[points[i], points[i + 1]]);
		}
		return window;
	}

	public function set point3DArr(value : Vector.<Point3D>) : void {
		_point3DArr = value;
	}

	public function get point3DArr() : Vector.<Point3D> {
		return _point3DArr;
	}
}
}