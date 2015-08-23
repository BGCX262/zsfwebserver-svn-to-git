package com.yule27.toModel.components {

/**
 * 数据结构——三角形
 * @author ZSF
 */
public class Triangle {
	public var _id : int;
	private var _p1 : Point3D;
	private var _p2 : Point3D;
	private var _p3 : Point3D;

	public function Triangle() {
	}

	public function set p1(value : Point3D) : void {
		_p1 = value;
	}

	public function get p1() : Point3D {
		return _p1;
	}

	public function set p2(value : Point3D) : void {
		_p2 = value;
	}

	public function get p2() : Point3D {
		return _p2;
	}

	public function set p3(value : Point3D) : void {
		_p3 = value;
	}

	public function get p3() : Point3D {
		return _p3;
	}

}
}