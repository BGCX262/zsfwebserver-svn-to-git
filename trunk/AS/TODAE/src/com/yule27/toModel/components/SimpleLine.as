package com.yule27.toModel.components {

/**
 * 数据结构-线
 * @author zsf
 */
public class SimpleLine {
	private var _startX : Number;
	private var _startY : Number;
	private var _startZ : Number;
	private var _endX : Number;
	private var _endY : Number;
	private var _endZ : Number;

	public function SimpleLine() {
	}

	public static function getInstance(startPoints : Vector.<Number>, endPoints : Vector.<Number>) : SimpleLine {
		var line : SimpleLine = new SimpleLine();
		line.startX = startPoints[0];
		line.startY = startPoints[1];
		line.startZ = startPoints[2];
		line.endX = endPoints[0];
		line.endY = endPoints[1];
		line.endZ = endPoints[2];
		return line;
	}

	public function setStart(p : Point3D) : void {
		_startX = p.x;
		_startY = p.y;
		_startZ = p.z;
	}

	public function setEnd(p : Point3D) : void {
		_endX = p.x;
		_endY = p.y;
		_endZ = p.z;
	}

	public function startPoint() : Point3D {
		return Point3D.Point3DXYZ(_startX, _startY, _startZ);
	}

	public function endPoint() : Point3D {
		return Point3D.Point3DXYZ(_endX, _endY, _endZ);
	}

	public function set startX(value : Number) : void {
		_startX = value;
	}

	public function get startX() : Number {
		return _startX;
	}

	public function set startY(value : Number) : void {
		_startY = value;
	}

	public function get startY() : Number {
		return _startY;
	}

	public function set startZ(value : Number) : void {
		_startZ = value;
	}

	public function get startZ() : Number {
		return _startZ;
	}

	public function set endX(value : Number) : void {
		_endX = value;
	}

	public function get endX() : Number {
		return _endX;
	}

	public function set endY(value : Number) : void {
		_endY = value;
	}

	public function get endY() : Number {
		return _endY;
	}

	public function set endZ(value : Number) : void {
		_endZ = value;
	}

	public function get endZ() : Number {
		return _endZ;
	}

	public function toString() : String {
		return startPoint().toString() + "; " + endPoint().toString();
	}

}
}