package {
import com.yule27.toModel.logic.CadLoader;
import com.yule27.toModel.logic.WriteDAE;
import com.yule27.toModel.util.Help;

import flash.display.Sprite;
import flash.events.Event;
import flash.events.IOErrorEvent;
import flash.net.FileReference;
import flash.net.URLLoader;
import flash.net.URLLoaderDataFormat;
import flash.net.URLRequest;
import flash.utils.ByteArray;


/**
 *
 * @author wf
 */
public class CAD extends Sprite {
	[Embed(source='SimpleDAE.txt', mimeType="application/octet-stream")]
	private var modelClass : Class;
	
	[Embed(source='3.txt', mimeType="application/octet-stream")]
	private var val : Class;

	private var backStr : String;

	public function CAD(str : String, wallHeight : Number, h1 : Number, h2 : Number, h3 : Number) {
		var txt : ByteArray = new modelClass() as ByteArray;
		var value : ByteArray = new val() as ByteArray;
		str = value.readMultiByte(value.length, "utf-8");
		WriteDAE.simpleDAE = txt.readMultiByte(txt.length, 'utf-8');
		WriteDAE.heightTOFloor = h1;
		WriteDAE.windowHeight = h2;
		WriteDAE.heightToDoor = h3;

		CadLoader.loadValue(str);
		var fileName : String = "simple";
		backStr = WriteDAE.makeDAEFile(Help.getFaces(wallHeight), fileName, wallHeight);
	}

	/**
	 * 获取更新数据的字符串
	 * @return
	 */
	public function getBack() : String {

		return backStr;
	}
}
}