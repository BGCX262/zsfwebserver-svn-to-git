package com.yule27.toModel.logic {
import com.adobe.serialization.json.JSON;
import com.yule27.toModel.components.SimpleDoor;
import com.yule27.toModel.components.SimpleFloor;
import com.yule27.toModel.components.SimplePolyline;
import com.yule27.toModel.components.SimpleWindow;

import flash.display.JointStyle;
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.net.URLLoader;
import flash.net.URLLoaderDataFormat;
import flash.net.URLRequest;

/**
 *
 * @author zsf
 */
public class CadLoader {
    /**
     * Cad内容读取器
     */
    public function CadLoader() {
    }

    /*	public static void LoadCad(string filePath) {
       using (AutoCADConnection connector = new AutoCADConnection()) {
       try {
       if (connector == null) {
       return;
       }
       string progid = "ObjectDBX.AxDbDocument.17";
       AcadApplication acadApp = connector.Application;
       dbx.AxDbDocument dbxDoc = (dbx.AxDbDocument)acadApp.GetInterfaceObject(progid);

       dbxDoc.Open(filePath, "");
       Contents.Clear();
       foreach (dbx.AcadEntity entity in dbxDoc.ModelSpace) {
       Console.WriteLine(entity.EntityName);
       if (entity.EntityName.Equals("AcDbLine")) {
       dbx.AcadLine line = (dbx.AcadLine)entity;
       double[] startPoints = (double[])line.StartPoint;
       double[] endPoints = (double[])line.EndPoint;
       Contents.AddLine(SimpleLine.GetInstance(startPoints, endPoints));
       } else if (entity.EntityName.Equals("AcDbPolyline")) {
       dbx.AcadLWPolyline line = (dbx.AcadLWPolyline)entity;
       double[] points = (double[])line.Coordinates;
       Contents.polylines.Add(SimplePolyline.getInstance(points));
       }
       }
       Contents.SortLines(ref Contents.lines);
       } catch (Exception e) {
       Console.WriteLine(e.StackTrace);
       }
       }
     }*/
    /**
     * 从txt文件中读取
     * @param filePath
     */
    public static function loadFile(filePath : String) : void {
        /*try {
           var str :String= "";
           var sr :StreamReader= new StreamReader(filePath);
           var temp :String;
           while ((temp = sr.ReadLine()) != null) {
           str += temp;
           }
           loadValue(str);
           } catch (Exception e) {
           //Console.WriteLine(e.StackTrace);
         }*/
        var loader : URLLoader = new URLLoader();
        loader.dataFormat = URLLoaderDataFormat.TEXT;
        var request : URLRequest = new URLRequest(filePath);
        try {
            loader.load(request);
            loader.addEventListener(Event.COMPLETE, __completeHandler);
            loader.addEventListener(IOErrorEvent.IO_ERROR, __ioErrorHandler);
        } catch (e : Error) {
            trace("Error loading requested document: " + filePath);
        }

    }

    /**
     * filePath加载成功
     * @param e
     */
    private static function __completeHandler(e : Event) : void {
        var loader : URLLoader = URLLoader(e.target);
        switch (loader.dataFormat) {
            case URLLoaderDataFormat.TEXT:
                trace("completeHandler (text): " + loader.data);
                break;
            case URLLoaderDataFormat.BINARY:
                trace("completeHandler (binary): " + loader.data);
                break;
            case URLLoaderDataFormat.VARIABLES:
                trace("completeHandler (variables): " + loader.data);
                break;
        }
        var str : String = String(loader.data);
        //loadValue(str);
    }

    /**
     * filePath加载失败
     * @param e
     */
    private static function __ioErrorHandler(e : IOErrorEvent) : void {
        trace("加载错误");
    }

    /**
     * 参数中读取
     * @param value
     */
    public static function loadValue(value : String) : void {
        try {
            var allJso : Array = JSON.decode(value);
            var jso : Array = allJso[0] as Array;
            var windows : Array = allJso[1] as Array;
            var doors : Array = allJso[2] as Array;
            var floor : Array = allJso[3] as Array;
            //JArray jso = (JArray) JsonConvert.DeserializeObject(value);
			
			Contents.enableDoor = false;
			Contents.enableFloor = false;
			Contents.enableWall = false;
			Contents.enableWindow = false;
			Contents.enableBayWindow = false;

            /* 墙壁 */
            Contents.polylines = new Vector.<SimplePolyline>();
            for each (var obj1 : Object in jso) {
                var all1 : Array = obj1 as Array;
                var points1 : Vector.<Number> = new Vector.<Number>();
                for each (var allInn1 : Object in all1) {
                    //JObject jObj = (JObject)allInn;
                    points1.push(allInn1.x * 2);
                    points1.push(allInn1.y * -2);
                }
                //double[] ps = points.ToArray<Double>();
                Contents.polylines.push(SimplePolyline.getInstance(points1));
				Contents.enableWall = true;
            }

            /* 窗户 */
            Contents.windows = new Vector.<SimpleWindow>();
            for each (var obj2 : Object in windows[0]) {
                var all2 : Array = obj2 as Array;
                var points2 : Vector.<Number> = new Vector.<Number>();
                for each (var allInn2 : Object in all2) {
                    //JObject jObj = (JObject)allInn;
                    points2.push(allInn2.x * 2);
                    points2.push(allInn2.y * -2);
                }
                //double[] ps = points.ToArray<Double>();
                if (points2.length == 8) {
                    Contents.windows.push(SimpleWindow.getInstance(points2));
					Contents.enableWindow = true;
				}
            }
			for each (var obj3 : Object in windows[1]) {
				var all3 : Array = obj3 as Array;
				var points3 : Vector.<Number> = new Vector.<Number>();
				for each (var allInn3 : Object in all3) {
					//JObject jObj = (JObject)allInn;
					points3.push(allInn3.x * 2);
					points3.push(allInn3.y * -2);
				}
				//double[] ps = points.ToArray<Double>();
				if (points3.length == 8) {
					Contents.bayWindows.push(SimpleWindow.getInstance(points3));
					Contents.enableBayWindow = true;
				}
			}

            /* 门 */
            Contents.doors = new Vector.<SimpleDoor>();
            for each (var obj4 : Object in doors) {
                var all4 : Array = obj4 as Array;
                var points4 : Vector.<Number> = new Vector.<Number>();
                for each (var allInn4 : Object in all4) {
                    //JObject jObj = (JObject)allInn;
                    points4.push(allInn4.x * 2);
                    points4.push(allInn4.y * -2);
                }
                //double[] ps = points.ToArray<Double>();
                if (points4.length == 8) {
                    Contents.doors.push(SimpleDoor.getInstance(points4));
					Contents.enableDoor = true;
				}
            }

            /* 地板 */
            Contents.floors = new Vector.<SimpleFloor>();
            var list : Vector.<Number> = new Vector.<Number>();
            for each (var obj5 : Object in floor) {
                //JObject jObj = (JObject)obj;
                list.push(obj5.x * 2);
                list.push(obj5.y * -2);
            }
            //double[] pArr = list.ToArray<Double>();
            Contents.floors.push(SimpleFloor.getInstance(list));
			Contents.enableFloor = true;
        } catch (e : Error) {
            //Console.WriteLine(e.ToString());
        }
    }
}
}