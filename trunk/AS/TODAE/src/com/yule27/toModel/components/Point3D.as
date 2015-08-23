package com.yule27.toModel.components {
import flash.geom.Point;

/**
 * 3D点描述类
 * @author zsf
 */
public class Point3D {
	private var _x : Number = 0;
	private var _y : Number = 0;
	private var _z : Number = 0;
	private var _normalX : Number = 0;
	private var _normalY : Number = 0;
	private var _normalZ : Number = 0;
	private var _mapX : Number = 0;
	private var _mapY : Number = 0;
	private var _mapZ : Number = 0;
	public var isST : Boolean;
	public var isNT : Boolean;
	public var name : String;

	public function Point3D() {
	}

	public static function Point3Dvector(ps : Vector.<Number>) : Point3D {
		var p : Point3D = new Point3D();
		p.x = ps[0];
		if (ps.length >= 2)
			p.y = ps[1];
		if (ps.length >= 3)
			p.z = ps[2];
		p.x = Number(p.x.toFixed(2));
		p.y = Number(p.y.toFixed(2));
		p.z = Number(p.z.toFixed(2));
		return p;
	}

	public static function Point3DXYZ(x : Number, y : Number, z : Number) : Point3D {
		var p : Point3D = new Point3D();
		p.x = x;
		p.y = y;
		p.z = z;
		p.x = Number(p.x.toFixed(2));
		p.y = Number(p.y.toFixed(2));
		p.z = Number(p.z.toFixed(2));
		return p;
	}

	public function to2D(type : int) : Point {
		return new Point();
	}

	public function positionSame(p : Point3D) : Boolean {
		return _x == p.x && _y == p.y && _z == p.z;
	}

	public function positionSamePO(p : Point3D, offset : Point3D) : Boolean {
		return _x == (p.x + offset.x) && _y == (p.y + offset.y) && _z == (p.z + offset.y);
	}

	public function mapSame(p : Point3D) : Boolean {
		return _mapX == p.mapX && _mapY == p.mapY && _mapZ == p.mapZ;
	}

	public function map() : String {
		return _mapX + " " + _mapY + " " + _mapZ;
	}

	public function mapList() : Vector.<Number> {
		var list : Vector.<Number> = new Vector.<Number>();
		list.push(_mapX);
		list.push(_mapY);
		list.push(_mapZ);
		return list;
	}

	public function set x(value : Number) : void {
		_x = value;
	}

	public function get x() : Number {
		return _x;
	}

	public function set y(value : Number) : void {
		_y = value;
	}

	public function get y() : Number {
		return _y;
	}

	public function set z(value : Number) : void {
		_z = value;
	}

	public function get z() : Number {
		return _z;
	}

	public function set normalX(value : Number) : void {
		_normalX = value;
	}

	public function get normalX() : Number {
		return _normalX;
	}

	public function set normalY(value : Number) : void {
		_normalY = value;
	}

	public function get normalY() : Number {
		return _normalY;
	}

	public function set normalZ(value : Number) : void {
		_normalZ = value;
	}

	public function get normalZ() : Number {
		return _normalZ;
	}

	public function set mapX(value : Number) : void {
		_mapX = value;
	}

	public function get mapX() : Number {
		return _mapX;
	}

	public function set mapY(value : Number) : void {
		_mapY = value;
	}

	public function get mapY() : Number {
		return _mapY;
	}

	public function set mapZ(value : Number) : void {
		_mapZ = value;
	}

	public function get mapZ() : Number {
		return _mapZ;
	}

	public function toString() : String {
		return "{ " + _x + ", " + _y + ", " + _z + " }";
	}
}
}