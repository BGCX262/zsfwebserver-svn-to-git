package com.yule27.toModel.util {
import com.yule27.toModel.components.Face;
import com.yule27.toModel.components.Point3D;
import com.yule27.toModel.components.Triangle;


/**
 *
 * @author zsf
 */
public class MathUtil {
	public function MathUtil() {
	}

	/**
	 * 判断点是否在直线内
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static function isPointInLine(p0 : Point3D, p1 : Point3D, p2 : Point3D) : Boolean {
		return Number(getLineLength(p1, p2).toFixed(2)) == Number(getLineLength(p0, p1).toFixed(2)) + Number(getLineLength(p0, p2).toFixed(2));
	}

	/**
	 * 计算直线长度
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static function getLineLength(p1 : Point3D, p2 : Point3D) : Number {
		var x : Number = p2.x - p1.x;
		var y : Number = p2.y - p1.y;
		var z : Number = p2.z - p1.z;
		return Math.pow(x * x + y * y + z * z, 0.5);
	}

	/**
	 * 判断两条线是否相交
	 * @param a
	 * @param b
	 * @param c
	 * @param d
	 * @param intersection
	 * @return
	 */
	private static function getIntersection(a : Point3D, b : Point3D, c : Point3D, d : Point3D, intersection : Point3D) : int {
		intersection.x = ((b.x - a.x) * (c.x - d.x) * (c.y - a.y) - c.x * (b.x - a.x) * (c.y - d.y) + a.x * (b.y - a.y) * (c.x - d.x)) / ((b.y - a.y) * (c.x - d.x) - (b.x - a.x) * (c.y - d.y));
		intersection.y = ((b.y - a.y) * (c.y - d.y) * (c.x - a.x) - c.y * (b.y - a.y) * (c.x - d.x) + a.y * (b.x - a.x) * (c.y - d.y)) / ((b.x - a.x) * (c.y - d.y) - (b.y - a.y) * (c.x - d.x));

		if ((intersection.x - a.x) * (intersection.x - b.x) <= 0 && (intersection.x - c.x) * (intersection.x - d.x) <= 0 && (intersection.y - a.y) * (intersection.y - b.y) <= 0 && (intersection.y - c.y) * (intersection.y - d.y) <= 0) {
			return 1; //'相交
		} else {
			return -1; //'相交但不在线段上
		}
	}

	/**
	 * 判断是否交叉，形成夹角不算
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param p4
	 * @return
	 */
	public static function isIntersection(p1 : Point3D, p2 : Point3D, p3 : Point3D, p4 : Point3D) : Boolean {
		var center : Point3D = new Point3D();
		var result : int = getIntersection(p1, p2, p3, p4, center);
		var temp : int = 0;
		if (result == 1) {
			temp += p1.x == center.x && p1.y == center.y ? 1 : 0;
			temp += p2.x == center.x && p2.y == center.y ? 1 : 0;
			temp += p3.x == center.x && p3.y == center.y ? 1 : 0;
			temp += p4.x == center.x && p4.y == center.y ? 1 : 0;
			return temp != 2;
		}
		return result == 1;
	}

	/**
	 * 根据余弦定理求两个线段夹角
	 * @param e
	 * @param o
	 * @param s
	 * @param isClockwise
	 * @return
	 */
	public static function angle(e : Point3D, o : Point3D, s : Point3D, isClockwise : Boolean) : Number {
		var cosfi : Number = 0;
		var fi : Number = 0;
		var norm : Number = 0;
		var dsx : Number = s.x - o.x;
		var dsy : Number = s.y - o.y;
		var dex : Number = e.x - o.x;
		var dey : Number = e.y - o.y;
		if (o.x == s.x && o.x == e.x) {
			dsx = s.z - o.z;
			dsy = s.y - o.y;
			dex = e.z - o.z;
			dey = e.y - o.y;
		} else if (o.y == s.y && o.y == e.y) {
			dsx = s.z - o.z;
			dsy = s.x - o.x;
			dex = e.z - o.z;
			dey = e.x - o.x;
		}

		cosfi = dsx * dex + dsy * dey;
		norm = (dsx * dsx + dsy * dsy) * (dex * dex + dey * dey);
		cosfi /= Math.sqrt(norm);

		fi = Math.acos(cosfi);

		if ((isClockwise ? -1 : 1) * (dsx * dey - dex * dsy) > 0) {
			return 180 * fi / Math.PI;
		} else {
			return 360 - 180 * fi / Math.PI;
		}
	}

	/**
	 * 计算三角形的面积
	 * @param traingle
	 * @return
	 */
	public static function area(traingle : Triangle) : Number {
		var a : Number = Math.sqrt(Math.pow(traingle.p1.x - traingle.p2.x, 2) + Math.pow(traingle.p1.y - traingle.p2.y, 2) + Math.pow(traingle.p1.z - traingle.p2.z, 2));
		var b : Number = Math.sqrt(Math.pow(traingle.p3.x - traingle.p2.x, 2) + Math.pow(traingle.p3.y - traingle.p2.y, 2) + Math.pow(traingle.p3.z - traingle.p2.z, 2));
		var c : Number = Math.sqrt(Math.pow(traingle.p1.x - traingle.p3.x, 2) + Math.pow(traingle.p1.y - traingle.p3.y, 2) + Math.pow(traingle.p1.z - traingle.p3.z, 2));
		var p : Number = (a + b + c) / 2;
		//((y1-y0)*(z2-z0) + (z1-z0)*(x2-x0) + (x1-x0)*(y2-y0)) - ((y2-y0)*(z1-z0) + (z2-z0)*(x1-x0) + (x2-x0)*(y1-y0))
		//return Math.round(Math.sqrt(p * (p - a) * (p - b) * (p - c)), 2);
		return Number(Math.sqrt(p * (p - a) * (p - b) * (p - c)).toFixed(2));
	}

	/**
	 * 判断一个点是否在一个三角形内
	 * @param target
	 * @param p
	 * @return
	 */
	public static function isPointInTraingle(target : Triangle, p : Point3D) : Boolean {
		var areaNum : Number = area(target);
		var sum : Number = 0;

		var traingle : Triangle = new Triangle();
		traingle.p1 = target.p1;
		traingle.p2 = target.p2;
		traingle.p3 = p;

		sum += area(traingle);

		traingle = new Triangle();
		traingle.p1 = target.p1;
		traingle.p2 = p;
		traingle.p3 = target.p3;

		sum += area(traingle);

		traingle = new Triangle();
		traingle.p1 = p;
		traingle.p2 = target.p2;
		traingle.p3 = target.p3;

		sum += area(traingle);
		//return Math.round(sum, 2) == areaNum;
		return Number(sum.toFixed(2)) == areaNum;
	}

	/**
	 * 计算三角形的面法线
	 * @param triangle
	 * @return
	 */
	public static function getTriangleNormal(triangle : Triangle) : Vector.<Number> {
		var temp1 : Vector.<Number> = new Vector.<Number>(3);
		var temp2 : Vector.<Number> = new Vector.<Number>(3);
		var normal : Vector.<Number> = new Vector.<Number>(3);
		var length : Number = 0;
		if (!isShun(triangle)) {
			if (triangle.p1.x == triangle.p2.x && triangle.p2.x == triangle.p3.x) {
				temp1[2] = triangle.p3.x - triangle.p1.x;
				temp1[1] = triangle.p3.y - triangle.p1.y;
				temp1[0] = triangle.p3.z - triangle.p1.z;
				temp2[2] = triangle.p3.x - triangle.p2.x;
				temp2[1] = triangle.p3.y - triangle.p2.y;
				temp2[0] = triangle.p3.z - triangle.p2.z;
			} else if (triangle.p1.y == triangle.p2.y && triangle.p2.y == triangle.p3.y) {
				temp1[1] = triangle.p3.x - triangle.p1.x;
				temp1[2] = triangle.p3.y - triangle.p1.y;
				temp1[0] = triangle.p3.z - triangle.p1.z;
				temp2[1] = triangle.p3.x - triangle.p2.x;
				temp2[2] = triangle.p3.y - triangle.p2.y;
				temp2[0] = triangle.p3.z - triangle.p2.z;
			} else if (triangle.p1.z == triangle.p2.z && triangle.p2.z == triangle.p3.z) {
				temp1[0] = triangle.p3.x - triangle.p1.x;
				temp1[1] = triangle.p3.y - triangle.p1.y;
				temp1[2] = triangle.p3.z - triangle.p1.z;
				temp2[0] = triangle.p3.x - triangle.p2.x;
				temp2[1] = triangle.p3.y - triangle.p2.y;
				temp2[2] = triangle.p3.z - triangle.p2.z;
			}
		} else {
			if (triangle.p1.x == triangle.p2.x && triangle.p2.x == triangle.p3.x) {
				temp1[2] = triangle.p3.x - triangle.p1.x;
				temp1[0] = triangle.p3.y - triangle.p1.y;
				temp1[1] = triangle.p3.z - triangle.p1.z;
				temp2[2] = triangle.p3.x - triangle.p2.x;
				temp2[0] = triangle.p3.y - triangle.p2.y;
				temp2[1] = triangle.p3.z - triangle.p2.z;
			} else if (triangle.p1.y == triangle.p2.y && triangle.p2.y == triangle.p3.y) {
				temp1[0] = triangle.p3.x - triangle.p1.x;
				temp1[2] = triangle.p3.y - triangle.p1.y;
				temp1[1] = triangle.p3.z - triangle.p1.z;
				temp2[0] = triangle.p3.x - triangle.p2.x;
				temp2[2] = triangle.p3.y - triangle.p2.y;
				temp2[1] = triangle.p3.z - triangle.p2.z;
			} else if (triangle.p1.z == triangle.p2.z && triangle.p2.z == triangle.p3.z) {
				temp1[1] = triangle.p3.x - triangle.p1.x;
				temp1[0] = triangle.p3.y - triangle.p1.y;
				temp1[2] = triangle.p3.z - triangle.p1.z;
				temp2[1] = triangle.p3.x - triangle.p2.x;
				temp2[0] = triangle.p3.y - triangle.p2.y;
				temp2[2] = triangle.p3.z - triangle.p2.z;
			}
		}

		normal[0] = temp1[1] * temp2[2] - temp1[2] * temp2[1];
		normal[1] = temp1[0] * temp2[2] - temp1[2] * temp2[0];
		normal[2] = temp1[0] * temp2[1] - temp1[1] * temp2[0];

		length = Math.sqrt(normal[0] * normal[0] + normal[1] * normal[1] + normal[2] * normal[2]);
		if (length == 0)
			length = 1;

		normal[0] /= length;
		normal[1] /= length;
		normal[2] /= length;

		return normal;
	}

	/**
	 * 计算三角形的纹理坐标
	 * @param triangle
	 */
	public static function getTriangleMap(triangle : Triangle) : void {
		var minPos : Vector.<Number>;
		var maxPos : Vector.<Number>;
		var points : Vector.<Point3D> = new Vector.<Point3D>();
		points.push(triangle.p1);
		points.push(triangle.p2);
		points.push(triangle.p3);
		//Help.getMinAndMaxPoint(points, minPos, maxPos);
		var v : Array = Help.getMinAndMaxPoint(points);
		minPos = v[0];
		maxPos = v[1];
		triangle.p1.mapX = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p1.mapY = tryExcept(triangle.p1.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p1.mapZ = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]);
		triangle.p2.mapX = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p2.mapY = tryExcept(triangle.p2.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p2.mapZ = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]);
		triangle.p3.mapX = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p3.mapY = tryExcept(triangle.p3.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p3.mapZ = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]);
	}
	
	/**
	 * 计算三角形的纹理坐标
	 * @param triangle
	 */
	public static function getTriangleMapDoor(triangle : Triangle) : void {
		var minPos : Vector.<Number>;
		var maxPos : Vector.<Number>;
		var points : Vector.<Point3D> = new Vector.<Point3D>();
		points.push(triangle.p1);
		points.push(triangle.p2);
		points.push(triangle.p3);
		//Help.getMinAndMaxPoint(points, minPos, maxPos);
		var v : Array = Help.getMinAndMaxPoint(points);
		minPos = v[0];
		maxPos = v[1];
		if (triangle.p1.x == triangle.p2.x && triangle.p2.x == triangle.p3.x) {
			triangle.p1.mapX = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p1.mapY = tryExcept(triangle.p1.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p1.mapZ = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p2.mapX = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p2.mapY = tryExcept(triangle.p2.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p2.mapZ = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p3.mapX = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p3.mapY = tryExcept(triangle.p3.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p3.mapZ = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]);
		} else if (triangle.p1.y == triangle.p2.y && triangle.p2.y == triangle.p3.y) {
			triangle.p1.mapZ = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p1.mapY = tryExcept(triangle.p1.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p1.mapX = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p2.mapZ = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p2.mapY = tryExcept(triangle.p2.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p2.mapX = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p3.mapZ = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p3.mapY = tryExcept(triangle.p3.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p3.mapX = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]);
		} else if (triangle.p1.z == triangle.p2.z && triangle.p2.z == triangle.p3.z) {
			triangle.p1.mapX = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p1.mapZ = tryExcept(triangle.p1.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p1.mapY = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p2.mapX = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p2.mapZ = tryExcept(triangle.p2.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p2.mapY = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]);
			triangle.p3.mapX = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]);
			triangle.p3.mapZ = tryExcept(triangle.p3.z - minPos[2], maxPos[2] - minPos[2]);
			triangle.p3.mapY = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]);
		}
	}

	/**
	 * 计算三角形的纹理坐标
	 * @param wallHeight
	 * @param triangle
	 */
	public static function getTriangleMapHeight(wallHeight : Number, triangle : Triangle) : void {
		var minPos : Vector.<Number>;
		var maxPos : Vector.<Number>;
		var points : Vector.<Point3D> = new Vector.<Point3D>();
		points.push(triangle.p1);
		points.push(triangle.p2);
		points.push(triangle.p3);
		var v : Array = Help.getMinAndMaxPoint(points);
		minPos = v[0];
		maxPos = v[1];
		minPos[2] = 0;
		maxPos[2] = wallHeight;
		triangle.p1.mapX = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p1.mapY = tryExcept(triangle.p1.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p1.mapZ = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]);
		triangle.p2.mapX = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p2.mapY = tryExcept(triangle.p2.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p2.mapZ = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]);
		triangle.p3.mapX = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]);
		triangle.p3.mapY = tryExcept(triangle.p3.z - minPos[2], maxPos[2] - minPos[2]);
		triangle.p3.mapZ = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]);
	}

	/**
	 * 计算三角形的纹理坐标
	 * @param face
	 * @param triangle
	 */
	public static function getTriangleMapFace(face : Face, triangle : Triangle) : void {
		var minPos : Vector.<Number>;
		var maxPos : Vector.<Number>;
		var v : Array = Help.getMinAndMaxPoint(face.points);
		minPos = v[0];
		maxPos = v[1];
		var width : Number = (maxPos[0] - minPos[0]) / 256;
		var height : Number = (maxPos[1] - minPos[1]) / 256;
		triangle.p1.mapX = tryExcept(triangle.p1.x - minPos[0], maxPos[0] - minPos[0]) * width;
		triangle.p1.mapY = tryExcept(triangle.p1.y - minPos[1], maxPos[1] - minPos[1]) * height;
		triangle.p2.mapX = tryExcept(triangle.p2.x - minPos[0], maxPos[0] - minPos[0]) * width;
		triangle.p2.mapY = tryExcept(triangle.p2.y - minPos[1], maxPos[1] - minPos[1]) * height;
		triangle.p3.mapX = tryExcept(triangle.p3.x - minPos[0], maxPos[0] - minPos[0]) * width;
		triangle.p3.mapY = tryExcept(triangle.p3.y - minPos[1], maxPos[1] - minPos[1]) * height;
	}

	/**
	 * 除法运算
	 * @param a
	 * @param b
	 * @return
	 */
	private static function tryExcept(a : Number, b : Number) : Number {
		if (a == 0 || b == 0)
			return 0;
		return a / b;
	}

	/**
	 * 判断三角形是否顺时针
	 * @param triangle
	 * @return
	 */
	private static function isShun(triangle : Triangle) : Boolean {
		var angleNum : Number = 0;
		angleNum = angle(triangle.p1, triangle.p2, triangle.p3, true);
		angleNum += angle(triangle.p2, triangle.p3, triangle.p1, true);
		angleNum += angle(triangle.p3, triangle.p1, triangle.p2, true);

		//return Math.round(angle, 2) == 180;
		return Number(angleNum.toFixed(2)) == 180;
	}

	/**
	 *  判断该面是否顺时针
	 * @param points
	 * @return
	 */
	public static function isShunPoint3D(points : Vector.<Point3D>) : Boolean {
		/* 遍历所有点，判断两个方向点的凹凸性 */
		var stCount : int = 0;
		var ntCount : int = 0;
		for (var i : int = 0; i < points.length; i++) {
			var s : Point3D = points[i - 1 < 0 ? points.length - 1 : i - 1];
			var c : Point3D = points[i];
			var e : Point3D = points[i + 1 >= points.length ? 0 : i + 1];

			c.isST = MathUtil.angle(s, c, e, true) <= 180;
			stCount += c.isST ? 1 : 0;
			c.isNT = MathUtil.angle(s, c, e, false) <= 180;
			ntCount += c.isNT ? 1 : 0;
		}
		return stCount > ntCount;
	}

	/**
	 * 计算共面的法线向量
	 * @param triangles
	 * @return
	 */
	public static function getTrianglesNormal(triangles : Vector.<Triangle>) : Vector.<Number> {
		var normals : Vector.<Number> = new Vector.<Number>(3);

		for each (var t : Triangle in triangles) {
			var d : Vector.<Number> = MathUtil.getTriangleNormal(t);
			normals[0] += d[0];
			normals[1] += d[1];
			normals[2] += d[2];
		}

		normals[0] = normals[0] / triangles.length;
		normals[1] = normals[1] / triangles.length;
		normals[2] = normals[2] / triangles.length;
		return normals;
	}

	public static function test() : void {
		var p1 : Point3D = Point3D.Point3DXYZ(0, 50, 0);
		var p2 : Point3D = Point3D.Point3DXYZ(0, 0, 0);
		var p3 : Point3D = Point3D.Point3DXYZ(50, 0, 0);

		var traingle : Triangle = new Triangle();
		traingle.p1 = p2;
		traingle.p2 = p3;
		traingle.p3 = p1;

		var ns : Vector.<Number> = getTriangleNormal(traingle);
		//Console.WriteLine(ns[0] + "," + ns[1] + "," + ns[2]);
	}

}
}