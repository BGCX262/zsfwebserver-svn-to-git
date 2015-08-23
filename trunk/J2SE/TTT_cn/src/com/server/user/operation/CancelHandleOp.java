package com.server.user.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;

public class CancelHandleOp {
	private static Log log = LogFactory.getLog(CancelHandleOp.class);
	//private static final double CASTLE_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancleCastleUpdateRetrun();
	//private static final double CASTLE_REPAIR_CANCEL_RETURN_RATE = Configuration.getCancleCastleRepairReturn();
	//private static final double SOULTOWER_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancelSoultowerUpdateReturn();
	//private static final double TECHNOLOGYTOWER_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancelTecUpdateReturn();
	//private static final double ROCKMINE_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancelRockmineUpdateReturn();
	//private static final double METALMINE_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancelMetalmineUpdateReturn();
	//private static final double CRYSTALMINE_UPDATE_CANCEL_RETURN_RATE = Configuration.getCancelCrystalmineUpdateReturn();
	
	public static byte[] cancelHandle(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		int what = DataFactory.getInt(DataFactory.get(information, 10, 4));
		switch(what){
			case 1:
				if(CastleOp.cancel(id, 0)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 2:
				if(CastleOp.cancel(id, 0)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 3:
				if(SoulTowerOp.cancel(id)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 4:
				if(TechnologyOp.cancel(id, 0)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 5:
				if(MineOp.cancel(id, 1)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 6:
				if(MineOp.cancel(id, 2)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 7:
				if(MineOp.cancel(id, 3)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 8:
				int towerID = DataFactory.getInt(DataFactory.get(information, 14, 4));
				if(CityOp.cancle(id, 0, towerID, 0)){
					re = new byte[]{0x00};
					//re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 9:
				int tower = DataFactory.getInt(DataFactory.get(information, 14, 4));
				if(CityOp.cancle(id, 0, tower, 1)){
					re = new byte[]{0x00};
					//re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			case 10:
				int tecTreeGoodID = DataFactory.getInt(DataFactory.get(information, 14, 4));
				if(TechnologyOp.cancelTecTree(id, 0, tecTreeGoodID)){
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(0));
				}
				break;
			default:
				break;
		}
		return re;
	}
}
