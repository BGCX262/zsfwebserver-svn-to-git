package com.yule27.toModel.logic {
import com.yule27.toModel.components.Face;
import com.yule27.toModel.components.Point3D;
import com.yule27.toModel.components.SimpleDoor;
import com.yule27.toModel.components.SimpleFloor;
import com.yule27.toModel.components.SimpleWindow;
import com.yule27.toModel.components.Triangle;
import com.yule27.toModel.util.Help;
import com.yule27.toModel.util.MathUtil;

import flash.net.FileFilter;
import flash.net.FileReference;
import flash.sampler.Sample;
import flash.utils.Dictionary;

/**
 *
 * @author zsf
 */
public class WriteDAE {
    public static var simpleDAE : String = "";

    public static var heightTOFloor : Number;
    public static var windowHeight : Number;
    public static var heightToDoor : Number;

    private static var n : int = 0;
    private static var c : int = 0;

    public function WriteDAE() {
    }

    /*static WriteDAE() {
       StreamReader sr = new StreamReader(System.AppDomain.CurrentDomain.BaseDirectory + @"SimpleDAE.xml");
       simpleDAE = sr.ReadToEnd();
       sr.Close();
     }*/
    /**
     * 生成DAE文件
     * @param faces
     * @param fileName
     * @param wallHeight
     */
    public static function makeDAEFile(faces : Vector.<Face>, fileName : String, wallHeight : Number) : String {
		/* 准备模型数据 */
		if (Contents.enableBayWindow) {
			replaceAll("<!--bayWindow", "");
			replaceAll("bayWindow-->", "");
		}
		if (Contents.enableWindow) {
			replaceAll("<!--window", "");
			replaceAll("window-->", "");
		}
		if (Contents.enableWindow && Contents.enableBayWindow) {
			replaceAll("<!--WWindowall", "");
			replaceAll("WWindowall-->", "");
		}
		if (Contents.enableDoor) {
			replaceAll("<!--door", "");
			replaceAll("door-->", "");
		}
		if (Contents.enableFloor) {
			replaceAll("<!--floor", "");
			replaceAll("floor-->", "");
		}
		if (Contents.enableWall) {
			replaceAll("<!--wall", "");
			replaceAll("wall-->", "");
		}
		
        /* 墙面数据准备 */
        var wall : Vector.<Face> = new Vector.<Face>();
        wall = faces;
        /* 移除顶部面，并添加到新的队列中 */
        var topWall : Vector.<Face> = new Vector.<Face>();
        for (var i : int = 0; i < wall.length; i++) {
            var face : Face = wall[i];
            var canRemove : Boolean = true;
            var canBRemove : Boolean = true;
            for each (var p1 : Point3D in face.points) {
                if (p1.z != wallHeight) {
                    canRemove = false;
                    break;
                }
            }
            for each (var p2 : Point3D in face.points) {
                if (p2.z != 0) {
                    canBRemove = false;
                    break;
                }
            }
            if (canRemove || canBRemove) {
                topWall.push(face);
                wall.splice(i, 1);
                i--;
            }
        }
        var triangles : Vector.<Triangle> = Help.getTriangles(wall);
        var topWallTriangles : Vector.<Triangle> = Help.getTriangles(topWall);
        var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
        for each (var f : Face in faces) {
            //allPoints.AddRange(face.points);
            allPoints = allPoints.concat(f.points);
        }
        Help.removeSamePoint(allPoints);

        /* 墙面 */
        var maxPos : Vector.<Number> = new Vector.<Number>(3);
        var minPos : Vector.<Number> = new Vector.<Number>(3);
        var v : Array = Help.getMinAndMaxPoint(allPoints);
        minPos = v[0];
        maxPos = v[1];
        var x : Number = ((maxPos[0] - minPos[0]) / 2 + minPos[0]) * -1;
        var y : Number = ((maxPos[1] - minPos[1]) / 2 + minPos[1]) * -1;
        /* 顶点 */
        var points : String = "";
        points = appendPoints(allPoints, x, y, points);
        /* 纹理坐标 */
        var maps : String = "";
        var mapsList : Vector.<Number> = new Vector.<Number>();
        var arr : Array = appendMap(triangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];
        maps += " ";
        arr = appendMap(topWallTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];

        /* 法线 */
        var normals : Vector.<Number> = getNormals(triangles);
        var topWallNormals : Vector.<Number> = getNormals(topWallTriangles);
        var normalsStr : String = "";
        normalsStr = appendNormal(normals, normalsStr);
        normalsStr += " ";
        normalsStr = appendNormal(topWallNormals, normalsStr);

        /* 三角形坐标 */
        var idxs : String = "";
        n = 0;
        c = 0;
        idxs = appendIndex(triangles, allPoints, idxs);

        replaceAll("$(time)", new Date().time.toString());
        replaceAll("$(position_count)", (allPoints.length * 3) + "");
        replaceAll("$(positions)", points);
        replaceAll("$(position_point_count)", allPoints.length + "");
        replaceAll("$(normals_count)", (normals.length + topWallNormals.length) + "");
        replaceAll("$(normals)", normalsStr);
        replaceAll("$(normals_point_count)", ((normals.length + topWallNormals.length) / 3) + "");
        replaceAll("$(maps_array_count)", mapsList.length + "");
        replaceAll("$(maps)", maps);
        replaceAll("$(maps_count)", (mapsList.length / 3) + "");
        replaceAll("$(triangles_count)", triangles.length + "");
        replaceAll("$(triangles)", idxs);

        idxs = "";
        idxs = appendIndex(topWallTriangles, allPoints, idxs);

        replaceAll("$(top_wall_count)", topWallTriangles.length + "");
        replaceAll("$(top_walls)", idxs);
        /* 墙面结束 */

        /* 地板 */
        n = 0;
        c = 0;
        makeFloor(simpleDAE);

        /* 窗户 */
        n = 0;
        c = 0;
        makeWindows(simpleDAE, wallHeight, x, y);

        /* 门 */
        n = 0;
        c = 0;
        makeDoors(simpleDAE, wallHeight, x, y);

        /*StreamWriter sw = new StreamWriter(@"d:\" + fileName + ".dae")";
           sw.Write(simpleDAE);
           sw.Flush();
         sw.Close();*/
        //trace("simpleDAE =" + simpleDAE);
        replaceAll("\n", "");
		replaceAll("<!--.*-->", "");
        return simpleDAE;
    }

    /**
     * 计算地板
     * @param simpleDAE
     */
    private static function makeFloor(simpleDAE : String) : void {
        var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
        for each (var f : SimpleFloor in Contents.floors) {
            //allPoints.AddRange(f.point3DArr);
            allPoints = allPoints.concat(f.point3DArr);
        }
        var maxPos : Vector.<Number> = new Vector.<Number>(3);
        var minPos : Vector.<Number> = new Vector.<Number>(3);
        var v : Array = Help.getMinAndMaxPoint(allPoints);
        minPos = v[0];
        maxPos = v[1];
        var x : Number = ((maxPos[0] - minPos[0]) / 2 + minPos[0]) * -1;
        var y : Number = ((maxPos[1] - minPos[1]) / 2 + minPos[1]) * -1;
        /* 地板数据准备 */
        var floorList : Vector.<Face> = new Vector.<Face>();
        var tpList : Vector.<Point3D> = new Vector.<Point3D>();
        tpList = allPoints;
		if (MathUtil.isShunPoint3D(tpList))
        	tpList.reverse();
        var floor : Face = Help.makeFace(tpList);
        floorList.push(floor);
        var floorTriangles : Vector.<Triangle> = Help.getTriangles(floorList);

        allPoints = new Vector.<Point3D>();
        //allPoints.AddRange(floor.points);
        allPoints = allPoints.concat(floor.points);
        /* 地板顶点 */
        var points : String = "";
        points = appendPoints(allPoints, x, y, points);

        /* 法线 */
        var normals : Vector.<Number> = getNormals(floorTriangles);
        var normalsStr : String = "";
        normalsStr = appendNormal(normals, normalsStr);

        /* 纹理坐标 */
        var maps : String = "";
        var mapsList : Vector.<Number> = new Vector.<Number>();
        var arr : Array = appendMapFace(floorTriangles, floor, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];

        /* 索引 */
        var idxs : String = "";
        idxs = appendIndex(floorTriangles, allPoints, idxs);

        replaceAll("$(floor_points_size)", (allPoints.length * 3) + "");
        replaceAll("$(floor_points)", points);
        replaceAll("$(floor_points_count)", allPoints.length + "");
        replaceAll("$(floor_normals_size)", normals.length + "");
        replaceAll("$(floor_normals)", normalsStr);
        replaceAll("$(floor_normals_count)", (normals.length / 3) + "");
        replaceAll("$(floor_array_count)", mapsList.length + "");
        replaceAll("$(floor_maps)", maps);
        replaceAll("$(floor_maps_count)", (mapsList.length / 3) + "");
        replaceAll("$(floor_triangles_size)", floorTriangles.length + "");
        replaceAll("$(floor_idxs)", idxs);
    }

    /**
     * 生成窗户
     * @param simpleDAE
     * @param wallHeight
     * @param x
     * @param y
     */
    private static function makeWindows(simpleDAE : String, wallHeight : Number, x : Number, y : Number) : void {
        /* 生成窗户下方的墙的所有面 */
        var faces : Vector.<Face> = new Vector.<Face>();
        var bheight : Number = wallHeight * heightTOFloor;
        var wheight : Number = wallHeight * windowHeight;
        for each (var windows : SimpleWindow in Contents.windows) {
            //faces.AddRange(Help.makeFaces(windows.point3DArr, height));
            faces = faces.concat(Help.makeFaces(windows.point3DArr, 0, bheight));
            faces = faces.concat(Help.makeFaces(windows.point3DArr, wheight, wallHeight));
        }
		for each (var bayWindows : SimpleWindow in Contents.bayWindows) {
			//faces.AddRange(Help.makeFaces(windows.point3DArr, height));
			faces = faces.concat(Help.makeFaces(bayWindows.point3DArr, 0, bheight));
			faces = faces.concat(Help.makeFaces(bayWindows.point3DArr, wheight, wallHeight));
		}
        if (faces.length <= 0)
            return;

        var wall : Vector.<Face> = new Vector.<Face>();
        wall = faces;
        /* 移除顶部面，并添加到新的队列中 */
        var topWall : Vector.<Face> = new Vector.<Face>();
        var btmWall : Vector.<Face> = new Vector.<Face>();
        for (var i : int = 0; i < wall.length; i++) {
            var face : Face = wall[i];
            var canRemove : Boolean = true;
            var canBRemove : Boolean = true;
            for each (var p1 : Point3D in face.points) {
                if (p1.z != bheight && p1.z != wheight) {
                    canRemove = false;
                    break;
                }
            }
            for each (var p2 : Point3D in face.points) {
                if (p2.z != 0 && p2.z != wallHeight) {
                    canBRemove = false;
                    break;
                }
            }
            if (canRemove) {
                topWall.push(face);
                wall.splice(i, 1);
                i--;
            } else if (canBRemove) {
                btmWall.push(face);
                wall.splice(i, 1);
                i--;
            }
        }
        var triangles : Vector.<Triangle> = Help.getTriangles(wall);
        var topWallTriangles : Vector.<Triangle> = Help.getTriangles(topWall);
        var btmWallTriangles : Vector.<Triangle> = Help.getTriangles(btmWall);
        var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
        for each (var f : Face in faces) {
            //allPoints.AddRange(face.points);
            allPoints = allPoints.concat(f.points);
        }
        Help.removeSamePoint(allPoints);

        /* 墙面 */
        /* 顶点 */
        var points : String = "";
        points = appendPoints(allPoints, x, y, points);
        /* 纹理坐标 */
        var maps : String = "";
        var mapsList : Vector.<Number> = new Vector.<Number>();
        var arr : Array = appendMapHeight(triangles, wallHeight, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];
        maps += " ";
        arr = appendMap(topWallTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];
        maps += " ";
        arr = appendMap(btmWallTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];

        /* 法线 */
        var normals : Vector.<Number> = getNormals(triangles);
        var topWallNormals : Vector.<Number> = getNormals(topWallTriangles);
        var btmWallNormals : Vector.<Number> = getNormals(btmWallTriangles);
        var normalsStr : String = "";
        normalsStr = appendNormal(normals, normalsStr);
        normalsStr += " ";
        normalsStr = appendNormal(topWallNormals, normalsStr);
        normalsStr += " ";
        normalsStr = appendNormal(btmWallNormals, normalsStr);

        /* 三角形坐标 */
        var idxs : String = "";
        idxs = appendIndex(triangles, allPoints, idxs);

        replaceAll("$(WindowWall_position_count)", (allPoints.length * 3) + "");
        replaceAll("$(WindowWall_positions)", points);
        replaceAll("$(WindowWall_position_point_count)", allPoints.length + "");
        replaceAll("$(WindowWall_normals_count)", (normals.length + topWallNormals.length + btmWallNormals.length) + "");
        replaceAll("$(WindowWall_normals)", normalsStr);
        replaceAll("$(WindowWall_normals_point_count)", ((normals.length + topWallNormals.length + btmWallNormals.length) / 3) + "");
        replaceAll("$(WindowWall_maps_array_count)", mapsList.length + "");
        replaceAll("$(WindowWall_maps)", maps);
        replaceAll("$(WindowWall_maps_count)", (mapsList.length / 3) + "");
        replaceAll("$(WindowWall_triangles_count)", triangles.length + "");
        replaceAll("$(WindowWall_triangles)", idxs);

        idxs = "";
        idxs = appendIndex(topWallTriangles, allPoints, idxs);

        replaceAll("$(WindowWall_top_wall_count)", topWallTriangles.length + "");
        replaceAll("$(WindowWall_top_walls)", idxs);

        idxs = "";
        idxs = appendIndex(btmWallTriangles, allPoints, idxs);

        replaceAll("$(WindowWall_btm_wall_count)", topWallTriangles.length + "");
        replaceAll("$(WindowWall_btm_walls)", idxs);
		
		/* 窗体 */
		makeWindowsEntry(bheight, wheight, x, y);
		
		/* 飘窗 */
		makeBayWindowsEntry(bheight, wheight, x, y);
    }
	
	/**
	 * 生成窗体
     * @param bheight
     * @param wheight
     * @param x
     * @param y
	 */
	private static function makeWindowsEntry(bheight : Number, wheight : Number, x : Number, y : Number) : void {
		var faces : Vector.<Face> = new Vector.<Face>();
		for each (var windows : SimpleWindow in Contents.windows) {
			faces = faces.concat(Help.makeFaces(Help.getOffset(windows.point3DArr, 9.5), bheight, wheight));
		}
		
		var triangles : Vector.<Triangle> = Help.getTriangles(faces);
		var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
		for each (var f : Face in faces) {
			allPoints = allPoints.concat(f.points);
		}
		Help.removeSamePoint(allPoints);
		
		/* 墙面 */
		/* 顶点 */
		var points : String = "";
		points = appendPoints(allPoints, x, y, points);
		/* 纹理坐标 */
		var maps : String = "";
		var mapsList : Vector.<Number> = new Vector.<Number>();
		var arr : Array = appendMap(triangles, maps, mapsList);
		maps = arr[0];
		mapsList = arr[1];
		
		/* 法线 */
		var normals : Vector.<Number> = getNormals(triangles);
		var normalsStr : String = "";
		normalsStr = appendNormal(normals, normalsStr);
		
		/* 三角形坐标 */
		n = 0;
		c = 0;
		var idxs : String = "";
		idxs = appendIndex(triangles, allPoints, idxs);
		
		replaceAll("$(window_points_size)", (allPoints.length * 3) + "");
		replaceAll("$(window_points)", points);
		replaceAll("$(window_points_count)", allPoints.length + "");
		replaceAll("$(window_normals_size)", (normals.length) + "");
		replaceAll("$(window_normals)", normalsStr);
		replaceAll("$(window_normals_count)", ((normals.length) / 3) + "");
		replaceAll("$(window_array_count)", mapsList.length + "");
		replaceAll("$(window_maps)", maps);
		replaceAll("$(window_maps_count)", (mapsList.length / 3) + "");
		replaceAll("$(window_triangles_size)", triangles.length + "");
		replaceAll("$(window_idxs)", idxs);
	}
	
	/**
	 * 生成飘窗
	 * @param bheight
	 * @param wheight
	 * @param x
	 * @param y
	 */
	private static function makeBayWindowsEntry(bheight : Number, wheight : Number, x : Number, y : Number) : void {
		var faces : Vector.<Face> = new Vector.<Face>();
		for each (var windows : SimpleWindow in Contents.bayWindows) {
			faces = faces.concat(Help.makeFaces(Help.getOffset(windows.point3DArr, 9.5), bheight, wheight));
		}
		
		var triangles : Vector.<Triangle> = Help.getTriangles(faces);
		var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
		for each (var f : Face in faces) {
			allPoints = allPoints.concat(f.points);
		}
		Help.removeSamePoint(allPoints);
		
		/* 墙面 */
		/* 顶点 */
		var points : String = "";
		points = appendPoints(allPoints, x, y, points);
		/* 纹理坐标 */
		var maps : String = "";
		var mapsList : Vector.<Number> = new Vector.<Number>();
		var arr : Array = appendMap(triangles, maps, mapsList);
		maps = arr[0];
		mapsList = arr[1];
		
		/* 法线 */
		var normals : Vector.<Number> = getNormals(triangles);
		var normalsStr : String = "";
		normalsStr = appendNormal(normals, normalsStr);
		
		/* 三角形坐标 */
		n = 0;
		c = 0;
		var idxs : String = "";
		idxs = appendIndex(triangles, allPoints, idxs);
		
		replaceAll("$(baywindow_position_count)", (allPoints.length * 3) + "");
		replaceAll("$(baywindow_positions)", points);
		replaceAll("$(baywindow_position_point_count)", allPoints.length + "");
		replaceAll("$(baywindow_normals_count)", (normals.length) + "");
		replaceAll("$(baywindow_normals)", normalsStr);
		replaceAll("$(baywindow_normals_point_count)", ((normals.length) / 3) + "");
		replaceAll("$(baywindow_maps_array_count)", mapsList.length + "");
		replaceAll("$(baywindow_maps)", maps);
		replaceAll("$(baywindow_maps_count)", (mapsList.length / 3) + "");
		replaceAll("$(baywindow_triangles_count)", triangles.length + "");
		replaceAll("$(baywindow_triangles)", idxs);
	}

    /**
     * 生成门
     * @param simpleDAE
     * @param wallHeight
     * @param x
     * @param y
     */
    private static function makeDoors(simpleDAE : String, wallHeight : Number, x : Number, y : Number) : void {
        /* 生成门下方的墙的所有面 */
        var faces : Vector.<Face> = new Vector.<Face>();
        var doorFaces : Vector.<Face> = new Vector.<Face>();
        var dheight : Number = wallHeight * heightToDoor;
        for each (var door : SimpleDoor in Contents.doors) {
            //faces.AddRange(Help.makeFaces(windows.point3DArr, height));
            faces = faces.concat(Help.makeFaces(door.point3DArr, dheight, wallHeight));
            doorFaces = doorFaces.concat(Help.makeFaces(Help.getOffset(door.point3DArr, 0), 0, dheight));
        }
        if (faces.length <= 0)
            return;

        var wall : Vector.<Face> = new Vector.<Face>();
        wall = faces;
        /* 移除顶部面，并添加到新的队列中 */
        var topWall : Vector.<Face> = new Vector.<Face>();
        var btmWall : Vector.<Face> = new Vector.<Face>();
        for (var i : int = 0; i < wall.length; i++) {
            var face : Face = wall[i];
            var canRemove : Boolean = true;
            var canBRemove : Boolean = true;
            for each (var p1 : Point3D in face.points) {
                if (p1.z != dheight) {
                    canRemove = false;
                    break;
                }
            }
            for each (var p2 : Point3D in face.points) {
                if (p2.z != 0 && p2.z != wallHeight) {
                    canBRemove = false;
                    break;
                }
            }
            if (canRemove) {
                topWall.push(face);
                wall.splice(i, 1);
                i--;
            } else if (canBRemove) {
                btmWall.push(face);
                wall.splice(i, 1);
                i--;
            }
        }
        var triangles : Vector.<Triangle> = Help.getTriangles(wall);
        var topWallTriangles : Vector.<Triangle> = Help.getTriangles(topWall);
        var btmWallTriangles : Vector.<Triangle> = Help.getTriangles(btmWall);
        var allPoints : Vector.<Point3D> = new Vector.<Point3D>();
        for each (var f : Face in faces) {
            //allPoints.AddRange(face.points);
            allPoints = allPoints.concat(f.points);
        }
        Help.removeSamePoint(allPoints);

        /* 墙面 */
        /* 顶点 */
        var points : String = "";
        points = appendPoints(allPoints, x, y, points);
        /* 纹理坐标 */
        var maps : String = "";
        var mapsList : Vector.<Number> = new Vector.<Number>();
        var arr : Array = appendMapHeight(triangles, wallHeight, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];
        maps += " ";
        arr = appendMap(topWallTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];
        maps += " ";
        arr = appendMap(btmWallTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];

        /* 法线 */
        var normals : Vector.<Number> = getNormals(triangles);
        var topWallNormals : Vector.<Number> = getNormals(topWallTriangles);
        var btmWallNormals : Vector.<Number> = getNormals(btmWallTriangles);
        var normalsStr : String = "";
        normalsStr = appendNormal(normals, normalsStr);
        normalsStr += " ";
        normalsStr = appendNormal(topWallNormals, normalsStr);
        normalsStr += " ";
        normalsStr = appendNormal(btmWallNormals, normalsStr);

        /* 三角形坐标 */
        var idxs : String = "";
        idxs = appendIndex(triangles, allPoints, idxs);

        replaceAll("$(doorWall_position_count)", (allPoints.length * 3) + "");
        replaceAll("$(doorWall_positions)", points);
        replaceAll("$(doorWall_position_point_count)", allPoints.length + "");
        replaceAll("$(doorWall_normals_count)", (normals.length + topWallNormals.length + btmWallNormals.length) + "");
        replaceAll("$(doorWall_normals)", normalsStr);
        replaceAll("$(doorWall_normals_point_count)", ((normals.length + topWallNormals.length + btmWallNormals.length) / 3) + "");
        replaceAll("$(doorWall_maps_array_count)", mapsList.length + "");
        replaceAll("$(doorWall_maps)", maps);
        replaceAll("$(doorWall_maps_count)", (mapsList.length / 3) + "");
        replaceAll("$(doorWall_triangles_count)", triangles.length + "");
        replaceAll("$(doorWall_triangles)", idxs);

        idxs = "";
        idxs = appendIndex(topWallTriangles, allPoints, idxs);

        replaceAll("$(doorWall_top_wall_count)", topWallTriangles.length + "");
        replaceAll("$(doorWall_top_walls)", idxs);

        idxs = "";
        idxs = appendIndex(btmWallTriangles, allPoints, idxs);

        replaceAll("$(doorWall_btm_wall_count)", btmWallTriangles.length + "");
        replaceAll("$(doorWall_btm_walls)", idxs);

        /* 门体 */
        var doorTriangles : Vector.<Triangle> = Help.getTriangles(doorFaces);
        allPoints = new Vector.<Point3D>();
        for each (var df : Face in doorFaces) {
            //allPoints.AddRange(face.points);
            allPoints = allPoints.concat(df.points);
        }
        Help.removeSamePoint(allPoints);

        /* 顶点 */
        points = "";
        points = appendPoints(allPoints, x, y, points);
        /* 纹理坐标 */
        maps = "";
        mapsList = new Vector.<Number>();
        arr = appendMapReal(doorTriangles, maps, mapsList);
        maps = arr[0];
        mapsList = arr[1];

        /* 法线 */
        normals = getNormals(doorTriangles);
        normalsStr = "";
        normalsStr = appendNormal(normals, normalsStr);

        /* 三角形坐标 */
        idxs = "";
        n = 0;
        c = 0;
        idxs = appendIndex(doorTriangles, allPoints, idxs);

        replaceAll("$(door_points_size)", (allPoints.length * 3) + "");
        replaceAll("$(door_points)", points);
        replaceAll("$(door_points_count)", allPoints.length + "");
        replaceAll("$(door_normals_size)", (normals.length) + "");
        replaceAll("$(door_normals)", normalsStr);
        replaceAll("$(door_normals_count)", ((normals.length) / 3) + "");
        replaceAll("$(door_array_count)", mapsList.length + "");
        replaceAll("$(door_maps)", maps);
        replaceAll("$(door_maps_count)", (mapsList.length / 3) + "");
        replaceAll("$(door_triangles_size)", doorTriangles.length + "");
        replaceAll("$(door_idxs)", idxs);
    }

    /**
     * 拼接顶点
     * @param points
     * @param x
     * @param y
     * @param str
     */
    private static function appendPoints(points : Vector.<Point3D>, x : Number, y : Number, str : String) : String {
        for each (var p : Point3D in points) {
            str += ((p.x + x) + " " + (p.y + y) + " " + (p.z) + " ");
        }
        str = str.substring(0, str.length - 1);
        return str;
    }

    /**
     * 拼接法线
     * @param normals
     * @param str
     */
    private static function appendNormal(normals : Vector.<Number>, str : String) : String {
        for each (var d : Number in normals) {
            str += d + " ";
        }
        str = str.substring(0, str.length - 1);
        return str;
    }

    /**
     * 拼接索引数据
     * @param triangles
     * @param allPoints
     * @param str
     */
    private static function appendIndex(triangles : Vector.<Triangle>, allPoints : Vector.<Point3D>, str : String) : String {
        for each (var t : Triangle in triangles) {
            var p1 : int = -1;
            var p2 : int = -1;
            var p3 : int = -1;
            for (var i : int = 0; i < allPoints.length; i++) {
                if (p1 == -1 && t.p1.positionSame(allPoints[i]))
                    p1 = i;
                if (p2 == -1 && t.p2.positionSame(allPoints[i]))
                    p2 = i;
                if (p3 == -1 && t.p3.positionSame(allPoints[i]))
                    p3 = i;
            }
            str += p1 + " " + (n) + " " + (c++) + " " + p2 + " " + (n) + " " + (c++) + " " + p3 + " " + (n++) + " " + (c++) + " ";
        }
        str = str.substring(0, str.length - 1);
        return str;
    }

    /**
     * 拼接纹理坐标
     * @param triangles
     * @param str
     * @param list
     */
    private static function appendMap(triangles : Vector.<Triangle>, str : String, list : Vector.<Number>) : Array {
        for each (var triangle : Triangle in triangles) {
            MathUtil.getTriangleMap(triangle);
            str += triangle.p1.map() + " " + triangle.p2.map() + " " + triangle.p3.map() + " ";
            /*list.AddRange(triangle.p1.mapList());
               list.AddRange(triangle.p2.mapList());
             list.AddRange(triangle.p3.mapList());*/
            list = list.concat(triangle.p1.mapList());
            list = list.concat(triangle.p3.mapList());
            list = list.concat(triangle.p2.mapList());
        }
        str = str.substring(0, str.length - 1);
        return new Array(str, list);
    }

    /**
     * 拼接纹理坐标
     * @param triangles
     * @param str
     * @param list
     */
    private static function appendMapReal(triangles : Vector.<Triangle>, str : String, list : Vector.<Number>) : Array {
        for each (var triangle : Triangle in triangles) {
            MathUtil.getTriangleMapDoor(triangle);
            str += triangle.p1.map() + " " + triangle.p2.map() + " " + triangle.p3.map() + " ";
            /*list.AddRange(triangle.p1.mapList());
               list.AddRange(triangle.p2.mapList());
             list.AddRange(triangle.p3.mapList());*/
            list = list.concat(triangle.p1.mapList());
            list = list.concat(triangle.p3.mapList());
            list = list.concat(triangle.p2.mapList());
        }
        str = str.substring(0, str.length - 1);
        return new Array(str, list);
    }

    /**
     * 拼接纹理坐标
     * @param triangles
     * @param wallHeight
     * @param str
     * @param list
     */
    private static function appendMapHeight(triangles : Vector.<Triangle>, wallHeight : Number, str : String, list : Vector.<Number>) : Array {
        for each (var triangle : Triangle in triangles) {
            MathUtil.getTriangleMapHeight(wallHeight, triangle);
            str += triangle.p1.map() + " " + triangle.p2.map() + " " + triangle.p3.map() + " ";
            /*list.AddRange(triangle.p1.mapList());
               list.AddRange(triangle.p2.mapList());
             list.AddRange(triangle.p3.mapList());*/
            list = list.concat(triangle.p1.mapList());
            list = list.concat(triangle.p2.mapList());
            list = list.concat(triangle.p3.mapList());
        }
        str = str.substring(0, str.length - 1);
        return new Array(str, list);
    }

    /**
     * 拼接纹理坐标
     * @param triangles
     * @param face
     * @param str
     * @param list
     */
    private static function appendMapFace(triangles : Vector.<Triangle>, face : Face, str : String, list : Vector.<Number>) : Array {
        str = "";
        list = new Vector.<Number>();
        for each (var triangle : Triangle in triangles) {
            MathUtil.getTriangleMapFace(face, triangle);
            str += triangle.p1.map() + " " + triangle.p2.map() + " " + triangle.p3.map() + " ";
            //list.AddRange(triangle.p1.mapList());
            //list.AddRange(triangle.p2.mapList());
            //list.AddRange(triangle.p3.mapList());
            list = list.concat(triangle.p1.mapList());
            list = list.concat(triangle.p2.mapList());
            list = list.concat(triangle.p3.mapList());
        }
        str = str.substring(0, str.length - 1);
        return new Array(str, list);
    }

    /**
     * 计算三角形中的点法线
     * @param triangles
     * @return
     */
    private static function getNormals(triangles : Vector.<Triangle>) : Vector.<Number> {
        var dict : Dictionary = new Dictionary();
        var normals : Vector.<Number> = new Vector.<Number>();
        for each (var i : Triangle in triangles) {
            var key1 : Point3D = i.p1;
            var key2 : Point3D = i.p2;
            var key3 : Point3D = i.p3;
            key1.name = i._id + ":" + key1.toString();
            key2.name = i._id + ":" + key2.toString();
            key3.name = i._id + ":" + key3.toString();
            var value1 : Vector.<Triangle> = new Vector.<Triangle>();
            var value2 : Vector.<Triangle> = new Vector.<Triangle>();
            var value3 : Vector.<Triangle> = new Vector.<Triangle>();
            value1.push(i);
            value2.push(i);
            value3.push(i);

            for each (var t : Triangle in triangles) {
                if (i.p1.positionSame(t.p1) || i.p1.positionSame(t.p2) || i.p1.positionSame(t.p3)) {
                    value1.push(t);
                } else if (i.p2.positionSame(t.p1) || i.p2.positionSame(t.p2) || i.p2.positionSame(t.p3)) {
                    value2.push(t);
                } else if (i.p3.positionSame(t.p1) || i.p3.positionSame(t.p2) || i.p3.positionSame(t.p3)) {
                    value3.push(t);
                }
            }
            dict[key1.name] = value1;
            dict[key2.name] = value2;
            dict[key3.name] = value3;
            var d1 : Vector.<Number> = MathUtil.getTriangleNormal(i);
            normals = normals.concat(d1);
            key1.normalX = d1[0];
            key1.normalY = d1[1];
            key1.normalZ = d1[2];
            key2.normalX = d1[0];
            key2.normalY = d1[1];
            key2.normalZ = d1[2];
            key3.normalX = d1[0];
            key3.normalY = d1[1];
            key3.normalZ = d1[2];
        }
        return normals;
    }

    /**
     * 替换simpleDAE中所有str1为str2
     * @param str1
     * @param str2
     */
    private static function replaceAll(str1 : String, str2 : String) : void {
        for (var i : int = 0; i < simpleDAE.length; i++) {
            if (simpleDAE.indexOf(str1) == -1) {
                break;
            }
            simpleDAE = simpleDAE.replace(str1, str2);
        }

    }
}
}