package com.yule27.toModel.components {

/**
 * 数据结构-多段线
 * @author zsf
 */
public class SimplePolyline {
	private var _point3DArr : Vector.<Point3D>;

	public function SimplePolyline() {
	}

	public static function getInstance(points : Vector.<Number>) : SimplePolyline {
		var line : SimplePolyline = new SimplePolyline();
		line.point3DArr = new Vector.<Point3D>(points.length / 2);
		var j : int = 0;
		for (var i : int = 0; i < points.length - 1; i += 2) {
			j = (i + 1) / 2;
			line.point3DArr[j] = Point3D.Point3Dvector(new <Number>[points[i], points[i + 1]]);
		}
		return line;
	}

	public function set point3DArr(value : Vector.<Point3D>) : void {
		_point3DArr = value;
	}

	public function get point3DArr() : Vector.<Point3D> {
		return _point3DArr;
	}
}
}