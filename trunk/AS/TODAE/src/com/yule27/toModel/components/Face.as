package com.yule27.toModel.components {

/**
 * 数据结构-面
 * @author zsf
 */
public class Face {
	private var _lines : Vector.<SimpleLine>;
	private var _points : Vector.<Point3D>;

	public function Face() {
	}

	public function set lines(value : Vector.<SimpleLine>) : void {
		_lines = value;
	}

	public function get lines() : Vector.<SimpleLine> {
		return _lines;
	}

	public function set points(value : Vector.<Point3D>) : void {
		_points = value;
	}

	public function get points() : Vector.<Point3D> {
		return _points;
	}
	
	public function toString():String
	{
		var str : String = "";
		for each (var p : Point3D in _points) {
			str += p.toString() + " ";
		}
		return super.toString();
	}
}
}