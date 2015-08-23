package com.yule27.toModel.components {
	/**
	 * 门实例
	 * @author: zsf
	 */
	public class SimpleDoor {
		private var _point3DArr : Vector.<Point3D>;
		
		public function SimpleDoor() {
		}
		
		public static function getInstance(points : Vector.<Number>) : SimpleDoor {
			var door : SimpleDoor = new SimpleDoor();
			door.point3DArr = new Vector.<Point3D>(points.length / 2);
			var j : int = 0;
			for (var i : int = 0; i < points.length - 1; i += 2) {
				j = (i + 1) / 2;
				door.point3DArr[j] = Point3D.Point3Dvector(new <Number>[points[i], points[i + 1]]);
			}
			return door;
		}
		
		public function set point3DArr(value : Vector.<Point3D>) : void {
			_point3DArr = value;
		}
		
		public function get point3DArr() : Vector.<Point3D> {
			return _point3DArr;
		}
	}
}