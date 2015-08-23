package com.yule27.toModel.util {


import com.yule27.toModel.components.Face;
import com.yule27.toModel.components.Point3D;
import com.yule27.toModel.components.SimpleLine;
import com.yule27.toModel.components.SimplePolyline;
import com.yule27.toModel.components.Triangle;
import com.yule27.toModel.logic.Contents;



/**
 * 帮助工具
 * @author zsf
 */
public class Help {
	public function Help() {
	}
	
	/**
	 * 计算偏移量
	 */
	public static function getOffset(points : Vector.<Point3D>, offset : Number) : Vector.<Point3D> {
		/* 判断窗户朝向 */
		var minPos : Vector.<Number> = new Vector.<Number>();
		var maxPos : Vector.<Number> = new Vector.<Number>();
		var v : Array = getMinAndMaxPoint(points);
		minPos = v[0];
		maxPos = v[1];
		var isHor : Boolean = maxPos[0] - minPos[0] > maxPos[1] - minPos[1];
		for each (var point : Point3D in points) {
			if (isHor) {
				point.y = point.y == minPos[1] ? point.y + offset : point.y;
				point.y = point.y == maxPos[1] ? point.y - offset : point.y;
			} else {
				point.x = point.x == minPos[0] ? point.x + offset : point.x;
				point.x = point.x == maxPos[0] ? point.x - offset : point.x;
			}
		}
		return points;
	}

	/**
	 * 生成面
	 * @param points
	 * @return
	 */
	public static function makeFace(points : Vector.<Point3D>) : Face {
		var face : Face = new Face();
		//face.points = new Vector.<Point3D>(points);
		face.points = points;
		face.lines = new Vector.<SimpleLine>();
		for (var i : int = 0; i < face.points.length; i++) {
			var p : Point3D = face.points[i];
			var pj : Point3D = face.points[i + 1 >= face.points.length ? 0 : i + 1];
			var line : SimpleLine = new SimpleLine();
			line.setStart(p);
			line.setEnd(pj);
			face.lines.push(line);
		}
		return face;
	}

	/**
	 * 计算所有面的三角形
	 * @param faces
	 * @return
	 */
	public static function getTriangles(faces : Vector.<Face>) : Vector.<Triangle> {
		var triangles : Vector.<Triangle> = new Vector.<Triangle>();
		var idx : int = 0;
		for each (var face : Face in faces) {
			/* 判断是否顺时针 */
			var isShun : Boolean = MathUtil.isShunPoint3D(face.points);

			/* 遍历所有点 */
			//var temp :Vector.<Point3D> = new Vector.<Point3D>(face.Points);
			var temp : Vector.<Point3D> = face.points.slice(); //复制出来，不改变原来的数据
			for (var i : int = 0, ac : int = 0; temp.length > 2 && ac < face.points.length * face.points.length; i++, ac++) {
				i = i >= temp.length ? 0 : i;
				var tuPoints : Vector.<Point3D> = new Vector.<Point3D>();
				var a : int;
				var b : int;
				a = i - 1 < 0 ? temp.length - 1 : i - 1;
				b = i + 1 >= temp.length ? 0 : i + 1;
				var s : Point3D = temp[a];
				var c : Point3D = temp[i];
				var e : Point3D = temp[b];

				/* 判断该点是否凸点 */
				if (MathUtil.angle(s, c, e, isShun) <= 180) {
					var t : Triangle = new Triangle();
					t.p1 = s;
					t.p2 = c;
					t.p3 = e;
					/* 判断该三角形内是否包含其他点 */
					var flag : Boolean = false;
					for (var j : int = 0; j < temp.length; j++) {
						if (j == i || j == a || j == b)
							continue;
						flag = MathUtil.isPointInTraingle(t, temp[j]);
						if (flag)
							break;
					}
					if (!flag) {
						t._id = idx++;
						triangles.push(t);
						temp.splice(i, 1);
						i--;
					}
				}
			}
		}
		return triangles;
	}

	/**
	 * 获得最小坐标和最大坐标
	 * @param points
	 * @param minPos
	 * @param maxPos
	 */
	public static function getMinAndMaxPoint(points : Vector.<Point3D>) : Array {
		var maxPos : Vector.<Number> = new Vector.<Number>(3);
		var minPos : Vector.<Number> = new Vector.<Number>(3);
		
		for (var i : Number = 0; i < 3; i++) {
			maxPos[i] = int.MIN_VALUE;
			minPos[i] = int.MAX_VALUE;
		}
		
		for each (var p : Point3D in points) {
			maxPos[0] = Math.max(maxPos[0], p.x);
			maxPos[1] = Math.max(maxPos[1], p.y);
			maxPos[2] = Math.max(maxPos[2], p.z);
			minPos[0] = Math.min(minPos[0], p.x);
			minPos[1] = Math.min(minPos[1], p.y);
			minPos[2] = Math.min(minPos[2], p.z);
		}
		return new Array(minPos, maxPos);
	}

	/**
	 * 获取所有顶点
	 * @param wallHeight
	 * @return
	 */
	public static function getFaces(wallHeight : Number) : Vector.<Face> {
		var faces : Vector.<Face> = new Vector.<Face>();
		if (Contents.lines.length > 0) {
			var startPoints : Vector.<Point3D> = new Vector.<Point3D>();
			for each (var line : SimpleLine in Contents.lines) {
				var sp : Point3D = line.startPoint();
				sp.z = 0;
				startPoints.push(sp);
			}
			faces = faces.concat(makeFaces(startPoints, 0, wallHeight));
		}
		for each (var spline : SimplePolyline in Contents.polylines) {
			var startPoints1 : Vector.<Point3D> = new Vector.<Point3D>();
			for (var i : int = 0; i < spline.point3DArr.length; i++) {
				var p : Point3D = spline.point3DArr[i];
				var v : Vector.<Number> = new <Number>[p.x, p.y, 0];
				startPoints1.push(Point3D.Point3Dvector(v));
			}
			faces = faces.concat(makeFaces(startPoints1, 0, wallHeight));
		}
		return faces;
	}

	/**
	 * 计算面
	 * @param startPoints
	 * @param wallHeight
	 * @return
	 */
	public static function makeFaces(startPoints : Vector.<Point3D>, startZ : Number, wallHeight : Number) : Vector.<Face> {
		var faces : Vector.<Face> = new Vector.<Face>();
		removeSamePoint(startPoints);

		/* 底面
		 /* 检测时针方向 */
		if (!MathUtil.isShunPoint3D(startPoints)) {
			startPoints.reverse();
		}
		var btmPoints : Vector.<Point3D> = new Vector.<Point3D>();
		for each (var p1 : Point3D in startPoints) {
			var np1 : Point3D = Point3D.Point3DXYZ(p1.x, p1.y, startZ);
			btmPoints.push(np1);
		}
		var bottom : Face = Help.makeFace(btmPoints);
		faces.push(bottom);

		/* 顶面 */
		var topPoints : Vector.<Point3D> = new Vector.<Point3D>();
		for each (var p2 : Point3D in startPoints) {
			var np2 : Point3D = Point3D.Point3DXYZ(p2.x, p2.y, p2.z + wallHeight);
			topPoints.push(np2);
		}
		topPoints.reverse();
		var top : Face = Help.makeFace(topPoints);
		faces.push(top);

		/* 所有侧面 */
		for each (var line : SimpleLine in bottom.lines) {
			var points : Vector.<Point3D> = new Vector.<Point3D>();
			points.push(line.startPoint());
			points.push(Point3D.Point3DXYZ(line.startPoint().x, line.startPoint().y, line.startPoint().z + wallHeight - startZ));
			points.push(Point3D.Point3DXYZ(line.endPoint().x, line.endPoint().y, line.endPoint().z + wallHeight - startZ));
			points.push(line.endPoint());
			faces.push(Help.makeFace(points));
		}
		return faces;
	}

	/**
	 * 移除多边形所有内部点
	 * @param list
	 * @return
	 */
	public static function removeInnerPoint(list : Vector.<Point3D>) : Vector.<Point3D> {
		var maxPos : Vector.<Number> = new Vector.<Number>(3);
		var minPos : Vector.<Number> = new Vector.<Number>(3);
		var v : Array = getMinAndMaxPoint(list);
		minPos = v[0];
		maxPos = v[1];
		/* 正轴扫描 */
		var resultList : Vector.<Point3D> = new Vector.<Point3D>();
		var minLoc : Number = Number.MAX_VALUE;
		var minIndex : int = -1;
		var minPoint : Point3D = Point3D.Point3Dvector(minPos);
		for (var m : int = 0; m < list.length; m++) {
			var p : Point3D = list[m];
			var length : Number = MathUtil.getLineLength(p, minPoint);
			if (length < minLoc) {
				minLoc = length;
			}
		}
		resultList.push(list[minIndex]);

		var i : int = 0;
		while (true) {
			i = i >= list.length ? 0 : i;
			var p1 : Point3D = list[i];
			if (p1.positionSame(resultList[resultList.length - 1])) {
				for (var j : int = i + 1 >= list.length ? 0 : i + 1; resultList.length <= list.length; j++) {
					j = j >= list.length ? 0 : j;
					var n : int = j - 2 < 0 ? (j == 0 ? list.length - 3 : list.length - 2) : j - 2;
					var pj : Point3D = list[j];
					var pf : Point3D = list[n];
					var length1 : Number = MathUtil.getLineLength(p1, pj);
					if (length1 != 20 && ((p1.x == pj.x || p1.y == pj.y) && (pj.x != pf.x && pj.y != pf.y))) {
						if (pj.positionSame(resultList[0])) {
							break;
						} else {
							resultList.push(pj);
							p = pj;
						}
					} //这个地方有问题，少了大括号
				}
				break;
			}
			i++;
		}
		list = resultList;

		/* 清理线上多余的点 */
		for (var l : int = 0; l < list.length; l++) {
			var o : int = l - 1 < 0 ? list.length - 1 : l - 1;
			var k : int = l + 1 >= list.length ? 0 : l + 1;
			var pj1 : Point3D = list[o];
			var pi : Point3D = list[l];
			var pk : Point3D = list[k];

			if (MathUtil.isPointInLine(pi, pj1, pk)) {
				list.splice(l, 1);
				l--;
			}
		}
		return list;
	}

	/**
	 * 清理重复的点
	 * @param points
	 */
	public static function removeSamePoint(points : Vector.<Point3D>) : void {
		for (var i : int = 0; i < points.length; i++) {
			var p1 : Point3D = points[i];
			for (var j : int = i + 1; j < points.length; j++) {
				if (p1.positionSame(points[j])) {
					points.splice(j, 1);
					j--;
				}
			}
		}
	}
}
}