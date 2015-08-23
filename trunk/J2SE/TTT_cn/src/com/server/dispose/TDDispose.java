package com.server.dispose;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cindy.run.connect.dispose.Dispose;
import com.cindy.run.connect.handler.InformationFormat;
import com.cindy.run.connect.instance.Instance;
import com.cindy.run.connect.instance.InstanceFactory;
import com.cindy.run.connect.instance.Instance.AttachObject;
import com.cindy.run.connect.instance.Instance.Attribute;
import com.cindy.run.util.DataFactory;
import com.database.model.bean.LoginRecord;
import com.server.cache.UserMemory;
import com.server.gameDate.statistics.GameDateOperate;
import com.server.gameDate.statistics.GameDateOperateInterf;
import com.server.goods.Goods;
import com.server.user.operation.AccountOp;
import com.server.user.operation.ActivityOp;
import com.server.user.operation.BlueprintOp;
import com.server.user.operation.CancelHandleOp;
import com.server.user.operation.CastleOp;
import com.server.user.operation.ChestOp;
import com.server.user.operation.CityOp;
import com.server.user.operation.CopyOp;
import com.server.user.operation.EnemyOp;
import com.server.user.operation.ExpGloryOp;
import com.server.user.operation.FightOp;
import com.server.user.operation.FriendOp;
import com.server.user.operation.LoginAwardOp;
import com.server.user.operation.LoginOp;
import com.server.user.operation.LotteryBoxOp;
import com.server.user.operation.MessageOp;
import com.server.user.operation.MineOp;
import com.server.user.operation.MissionOp;
import com.server.user.operation.PVPRecordOp;
import com.server.user.operation.PickOp;
import com.server.user.operation.PresentOp;
import com.server.user.operation.PropOp;
import com.server.user.operation.ReceiveGoodsOp;
import com.server.user.operation.SackOp;
import com.server.user.operation.SessionKeyCheckOp;
import com.server.user.operation.ShopOp;
import com.server.user.operation.SlaverOp;
import com.server.user.operation.SoulTowerOp;
import com.server.user.operation.StorageOp;
import com.server.user.operation.TaskOp;
import com.server.user.operation.TechnologyOp;
import com.server.util.Cmd;
import com.server.util.Configuration;

public class TDDispose implements Dispose{
	private static final Log log = LogFactory.getLog(TDDispose.class);
	private static Object LOCK = new Object();
	//private static TDDispose instance;
	private static boolean DEBUG = Configuration.printProtocol();
	public static final int PERIOD = Configuration.getDebugTime();
	public static final String SESSION_DESTINATION = "session_destination";
	public static final Object ROLE_ID = "uid";
	private static final Object ROLE_NAME = "name";
	public static final String CENTER_SERVER = "center config";
	public static final Object SCENE = "scene";
	private static final boolean CAN_OPEN_MORE_CLIENT = Configuration.canOpenMoreClient();//能否多开
	private Protocol protocol;
	public static GameDateOperateInterf statisticLog = GameDateOperate.getInstance();
	private static final InstanceFactory IF = InstanceFactory.getFactory();
	public TDDispose(){
		 protocol = new Protocol();
	}
	
	@Override
	public void dispose(InformationFormat infor) throws Exception {
		try{
			printProtocol(infor);
			final Instance role = infor.getInstance();
			byte[] information = infor.getInformation();
			final long time = System.currentTimeMillis();
//			Object controls = role.getAttribute("controls");
//			if (controls == null) {
//				role.setAttribute("loginTime", time);
//				List<Cmd> cmds = new LinkedList<Cmd>();
//				cmds.add(Cmd.getInstance(information).getCmdResponse(0, 4));
//				role.setAttribute("controls", cmds);
//			} else {
//				Long loginTime = (Long) role.getAttribute("loginTime");
//				if (loginTime != null && loginTime < time && loginTime > time - 1000 * 60 * 10) {
//					List<Cmd> cmds = (List<Cmd>) role.getAttribute("controls");
//					cmds.add(Cmd.getInstance(information).getCmdResponse(0, 4));
//					role.setAttribute("controls", cmds);
//				}
//			}
			if (protocol.isLogin(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x02;
				byte[] logReturn = new byte[]{0x01};
				final long id = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				
				if (AccountOp.isLock(id)) {
					logReturn = new byte[]{0x03};
				}else{
					if(TDDispose.initRoleInformation(infor)){
						int num = IF.getInstances().size();
						log.info("online_player's_number:" + num);
						int timezone = DataFactory.getInt(DataFactory.get(information, information.length - 8, 4));
						String name = new String((byte[])role.getAttribute(ROLE_NAME),"UTF-8");
						final int source = DataFactory.getInt(DataFactory.get(information, information.length - 4, 4));
						System.out.println(role.getSession().getRemoteAddress().toString());
						final String IP = role.getSession().getRemoteAddress().toString().substring(1).split(":")[0];
						long begin = System.currentTimeMillis();
						final LoginRecord loginRecord = LoginOp.online(id, IP, timezone, name, source, role);
						System.out.println("login: " + (System.currentTimeMillis() - begin) + "ms");
						role.setAttachObject(AttachObject.BEFORE_CLEAR, new Runnable(){
							public void run() {
//								try {
//									Object attribute = role.getAttribute("controls");
//									if (attribute != null) {
//										List<Cmd> controls = (LinkedList<Cmd>) attribute;
//										BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\protrol\\"+id +","+time +".txt"));
//										for (Iterator<Cmd> iter = controls.iterator(); iter.hasNext(); ) {
//											bw.write(iter.next().toString());
//											bw.newLine();
//										}
//										bw.close();
//									}
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
								LoginOp.offline(id);
							}
						});
						if(loginRecord.isFirst()){
							logReturn = new byte[]{0x02};
						}else{
							logReturn = new byte[]{0x00};
						}
						if(loginRecord.isInitialized()){
							logReturn = DataFactory.addByteArray(logReturn, new byte[]{0x00});
						}else{
							logReturn = DataFactory.addByteArray(logReturn, new byte[]{0x01});
						}
						logReturn = DataFactory.addByteArray(logReturn, LoginOp.loginReturn(id,loginRecord));
						logReturn = DataFactory.addByteArray(logReturn, DataFactory.doubleToXiaoTouByte(loginRecord.getTodayOnline()));
					}
					logReturn = DataFactory.addByteArray(head, logReturn);
					sendInformation(role, logReturn);
				}
				//Online.online(id);
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("Login spend time :"+ spend);
				}
				return;
			}else if(protocol.isEnterScene(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x04;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				if(canDo(id, sceneID)){
					sendInformation(role, DataFactory.addByteArray(head, LoginOp.enterScene(id, sceneID)));
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("enter scene spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetTime(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0xf2;
				sendInformation(role, DataFactory.addByteArray(head, DataFactory.doubleToXiaoTouByte(System.currentTimeMillis())));
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get Time spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetStorage(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x06;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						/* 领取物品 
						if (!ReceiveGoodsOp.receiveGoods(id)) {
							log.info("player " + id + " receive goods failed!");
						}*/
						sendInformation(role, PresentOp.recivedPresents(id, information));
						sendInformation(role, DataFactory.addByteArray(head, StorageOp.getStorage(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E1".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get storage spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetAppFriendInfo(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x08;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, FriendOp.getAppFriend(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E2".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get app friend spend time :"+ spend);
				}
				return;
			}else if(protocol.isMixBlueprint(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x40;
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, BlueprintOp.mixBlueprint(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E4".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("mix blueprint spend time :"+ spend);
				}
				return;
			}else if(protocol.isUpBlueprint(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x0d;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, BlueprintOp.upBlueprint(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E5".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrade blueprint spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetPVE(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x14;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, FightOp.pve(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E9".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get pve information spend time :"+ spend);
				}
				return;
			}else if(protocol.isPVEResult(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x79;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, FightOp.pveResult(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E10".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("deal pve result spend time :"+ spend);
				}
				return;
			}else if(protocol.isPickSceneGoods(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x17;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				if(canDo(id, sceneID)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.pickupGoods(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E11".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("pick up scene goods spend time :"+ spend);
				}
				return;
			}else if(protocol.isBuildTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x41;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.buildTower(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E12".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("build tower spend time :"+ spend);
				}
				return;
			}else if(protocol.isCatchSlaver(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x1B;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, SlaverOp.catchSlaver(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E13".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("cacth slaver spend time :"+ spend);
				}
				return;
			}else if(protocol.isUpgradTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x4f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, CityOp.upgradeTower(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E14".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrad tower spend time :"+ spend);
				}
				return;
			}else if(protocol.isRepairTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x4f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, CityOp.repairTower(id, information)));;
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E15".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("repair tower spend time :"+ spend);
				}
				return;
			}else if(protocol.isRemoveTower(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						CityOp.removeTower(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E16".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("remove tower spend time :"+ spend);
				}
				return;
			}else if(protocol.isCreateTowerOrMonster(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x20;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SoulTowerOp.createMonster(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E17".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("create tower or monster spend time :"+ spend);
				}
				return;
			}else if(protocol.isStoreTowerOrMonster(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						MissionOp.store(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E18".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("store queue tower and monster spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetMissionQueue(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x23;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, MissionOp.getMissionQueue(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E19".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get mission queue spend time :"+ spend);
				}
				return;
			}else if(protocol.isRepairCastle(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x4f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, CastleOp.repairCastle(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E20".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("repair castle spend time :"+ spend);
				}
				return;
			}else if(protocol.isUpgradCastle(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = 0x4f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role, DataFactory.addByteArray(head, CastleOp.upgradCastle(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E21".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrad castle spend time :"+ spend);
				}
				return;
			}else if(protocol.isCancelCreateQueue(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						SoulTowerOp.cancelQueue(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E22".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("cancel create queue spend time :"+ spend);
				}
				return;
			}else if(protocol.isEnterCity(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x28;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.getCity(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E23".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("enter city spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetTitle(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						ExpGloryOp.getTitle(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E24".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("buy title spend time :"+ spend);
				}
				return;
			}else if(protocol.isUseTitle(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						ExpGloryOp.useTitle(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E25".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("use title spend time :"+ spend);
				}
				return;
			}else if(protocol.isFlipCard(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x2c;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						byte[] re = CityOp.flipCard(id, information);
						if(re != null){
							sendInformation(role,DataFactory.addByteArray(head, re));
						}
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E26".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("flip card spend time :"+ spend);
				}
				return;
			}/*else if(protocol.isTempBoxToStorage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						StorageOp.transferBox(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E27".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("store from tempbox to storage spend time :"+ spend);
				}
				return;
			}*/else if(protocol.isResetCity(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x31;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.resetCity(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E28".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("reset city spend time :" + spend);
				}
				return;
			}/*else if(protocol.isLockStorage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						StorageOp.lock(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E29".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("lock storage spend time :"+ spend);
				}
				return;
			}*/else if(protocol.isDeleteStorage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						StorageOp.delete(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E30".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("delete storage spend time :"+ spend);
				}
				return;
			}else if(protocol.isGetPVPList(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x35;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.getPVPList(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E31".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
			
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get pvp defender List  spend time :" + spend);
				}
				return;
			}else if(protocol.isGetPVP(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x37;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.pvp(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E32".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get pvp defender infomation spend time :" + spend);
				}
				return;
			}else if(protocol.isPvpResult(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x3a;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.pvpResult(id, information, IF)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E33".getBytes("UTF-8");
						sendException(role, head, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("deal pvp fight result infomation spend time :" + spend);
				}
				return;
			}else if(protocol.isRecivePVPNotify(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						PVPRecordOp.sysData(id);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E34".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("syschronize the pvp information spend time :" + spend);
				}
				return;
			}else if(protocol.isGetTaskList(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x43;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, TaskOp.getTaskList(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E35".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get task list spend time :" + spend);
				}
				return;
			}else if(protocol.isAcceptTask(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x45;
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, TaskOp.acceptTask(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E36".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("accpet task spend time :" + spend);
				}
				return;
			}else if(protocol.isTaskReward(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x47;
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, TaskOp.taskReward(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E37".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("task reward spend time :" + spend);
				}
				return;
			}else if(protocol.isGetMessage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x49;
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, MessageOp.getMessages(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E38".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get messages spend time :" + spend);
				}
				return;
			}else if(protocol.isGetEnemy(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x4b;
				long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				if(canDo(id, sceneID)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, EnemyOp.getEnemy(sceneID, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E39".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get enemy spend time :" + spend);
				}
				return;
			}else if(protocol.isBugGoods(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x4d;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						byte[] re = ShopOp.buyGoods(id, information);
						sendInformation(role,DataFactory.addByteArray(head, re));
						int goodsID = DataFactory.getInt(DataFactory.get(information, 10, 4));
						if(re[0] == 0x00 && Goods.getCate(goodsID) == 7){
							SlaverOp.sendSlaverList(id, role, head);
						} 
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E40".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("bug goods spend time :" + spend);
				}
				return;
			}else if(protocol.isPickMaterial(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x52;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				if(canDo(id, sceneID)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PickOp.pickMaterialBox(id, sceneID, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E41".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("bug goods spend time :" + spend);
				}
				return;
			}else if(protocol.isModifyRace(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						CastleOp.modifyRaceAndName(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E42".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("modify race spend time :" + spend);
				}
				return;
			}else if(protocol.isGetSinglePvpList(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x55;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
				if(canDo(id, sceneID)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.getSinglePVPList(sceneID)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E43".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get single pvp List spend time :" + spend);
				}
				return;
			}else if(protocol.isGetSoulTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x57;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SoulTowerOp.getUserSoulTower(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E44".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get soul tower spend time :" + spend);
				}
				return;
			}else if(protocol.isUpgradeSoulTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x5a;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SoulTowerOp.upgrade(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E45".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrade soul tower spend time :" + spend);
				}
				return;
			}else if(protocol.isCancelUpgradeSoulTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x5b;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CancelHandleOp.cancelHandle(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E46".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("cancel handle spend time :" + spend);
				}
				return;
			}else if(protocol.isUpgradeMine(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x5d;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, MineOp.upgrade(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E47".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrade mine spend time :" + spend);
				}
				return;
			}else if(protocol.isCollectMine(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x5f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, MineOp.collect(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E48".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("collect mine spend time :" + spend);
				}
				return;
			}else if(protocol.isGetMine(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x62;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, MineOp.getMine(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E49".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get mine spend time :" + spend);
				}
				return;
			}else if(protocol.isSpeedup(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x64;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PropOp.use(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E50".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("use speedup prop spend time :" + spend);
				}
				return;
			}else if(protocol.isGetUserTec(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x6b;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						long masterID = id;
						if(information.length > 10){
							masterID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
						}
						sendInformation(role,DataFactory.addByteArray(head, TechnologyOp.getUserTec(masterID)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E51".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get user's technology spend time :" + spend);
				}
				return;
			}else if(protocol.isUpgradeTecTower(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x69;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, TechnologyOp.upgradeTecTower(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E52".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrade user's technology tower spend time :" + spend);
				}
				return;
			}else if(protocol.isUpgradeTecTree(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x67;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, TechnologyOp.upgradeTecTree(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E53".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("upgrade user's technology tree spend time :" + spend);
				}
				return;
			}else if(protocol.isOpenLotteryBox(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x6d;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, LotteryBoxOp.operLottery(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E54".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("open lottery box spend time :" + spend);
				}
				return;
			}else if(protocol.isGetLotteryPrize(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x6f;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, LotteryBoxOp.getLotteryPrize(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E55".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get lottery box prize spend time :" + spend);
				}
				return;
			}else if(protocol.isOpenPresentPack(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x71;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PresentOp.openPresentsPack(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E56".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("open present pack spend time :" + spend);
				}
				return;
			}else if(protocol.isExpandStorage(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x74;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, StorageOp.expandStorage(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E57".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("expand storage spend time :" + spend);
				}
				return;
			}else if(protocol.isReadMessage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						MessageOp.readMessage(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E58".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("read message spend time :" + spend);
				}
				return;
			}else if(protocol.isGetActivity(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x78;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, ActivityOp.getActivityEndTime()));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E59".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get activity end time spend time :" + spend);
				}
				return;
			}else if(protocol.isDeleteMessage(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						MessageOp.deleteMessage(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E60".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("delete message spend time :" + spend);
				}
				return;
			}else if(protocol.isGetLoginAwardInfo(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x7c;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, LoginAwardOp.getLoginAwardInfo(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E61".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get login award info spend time :" + spend);
				}
				return;
			}else if(protocol.isGetLoginAward(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x7e;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, LoginAwardOp.award(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E62".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get login award spend time :" + spend);
				}
				return;
			}else if(protocol.isGetSlaverList(information)){
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						SlaverOp.sendSlaverList(id, role, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E63".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get slaver list spend time :" + spend);
				}
				return;
			}else if(protocol.isGetCopy(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x81;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CopyOp.getCopy(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E64".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get copy spend time :" + spend);
				}
				return;
			}else if(protocol.isOpenCopy(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x83;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CopyOp.openCopy(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E65".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("open copy spend time :" + spend);
				}
				return;
			}else if(protocol.isRequestLogin(information)){
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x86;
				try{
					long id = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
					sendInformation(role,DataFactory.addByteArray(head, requestLogin(id, information)));
				}catch(Exception e){
					log.error(e, e);
					byte[] content = "E66".getBytes("UTF-8");
					sendException(role, information, content);
				}
				
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("request login spend time :" + spend);
				}
				return;
			} else if (protocol.isOpenChest(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x88;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, ChestOp.openChest(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E66".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("open chest spend time :" + spend);
				}
				return;
			} else if (protocol.isGetPVPState(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x90;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.getPVPState(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E67".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("get PVP state spend time :" + spend);
				}
				return;
			} else if (protocol.isOpenSack(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x92;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SackOp.openSack(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E68".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("open sack spend time :" + spend);
				}
				return;
			} else if (protocol.isSynthesizeBlueprint(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x94;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, BlueprintOp.synthesizeBlueprint(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E69".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("Synthesize Blueprint spend time :" + spend);
				}
				return;
			} else if (protocol.isTurntable(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x96;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PropOp.turntable(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E70".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("turntable spend time :" + spend);
				}
				return;
			} else if (protocol.isReset(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x99;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PropOp.resetFB(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E72".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("turntable spend time :" + spend);
				}
				return;
			} else if (protocol.isStartPVE(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x01;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.startPve(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E72".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("turntable spend time :" + spend);
				}
				return;
			} else if (protocol.isSpeedTower(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x03;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.towerSpeedUp(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E73".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			} else if (protocol.openLuckBox(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x06;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, PropOp.openPropsBox(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E74".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			} else if (protocol.isPvn(information)) {
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x08;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, FightOp.pvn(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E75".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isCompensate(information)){
				/* 可以补偿的物品  */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x10;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, ReceiveGoodsOp.getGoods(id)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E76".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isUpdateCompensate(information)){
				/* 领取物品  */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x12;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, ReceiveGoodsOp.updateReceived(id, role, information)));
						
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E77".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isAddSp(information)){
				/* 增加SP */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x14;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CastleOp.addSp(id, information)));
						
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E78".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isRemoveSlaver(information)){
				/* 解雇好友 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x16;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SlaverOp.removeSlaver(id, information)));
						
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E79".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isInviteFriends(information)){
				/* 邀请好友 */
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						//sendInformation(role,DataFactory.addByteArray(head, SlaverOp.removeSlaver(id, information)));
						CityOp.inviteFriends(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isCashInviteFriends(information)){
				/* 点卷补齐邀请好友 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x19;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.useCashInviteFriend(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isBatchPickup(information)){
				/* 批量拾取资源 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x21;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.batchPickupGoods(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isDoTask(information)){
				/* 批量拾取资源 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x21;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						TaskOp.protocolDoTask(id, information);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isPickFriend(information)){
				/* 好友点塔 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x23;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CityOp.pickFriend(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isGetPeoples(information)){
				/* 获取指定异性列表 */
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x25;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CastleOp.getPeoplesBySex(id, information)));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isSendLove(information)){
				/* 获取指定异性列表 */
				Cmd cmd = Cmd.getInstance(information);
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						CastleOp.sendLove(id, cmd);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isGetSendedLoves(information)){
				/* 获取已发送的玩家列表 */
				Cmd cmd = Cmd.getInstance(information);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x28;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CastleOp.getSendedLoves(id, cmd).getResponse()));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isAddMaxSp(information)){
				/* 增加SP上限 */
				Cmd cmd = Cmd.getInstance(information);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x30;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, CastleOp.addMaxSp(id, cmd).getResponse()));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isSlaverWork(information)){
				/* 工人打工 */
				Cmd cmd = Cmd.getInstance(information);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x32;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SlaverOp.slaverWork(id, cmd).getResponse()));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isGetSlaverWorkPriz(information)){
				/* 领取工人打工奖励 */
				Cmd cmd = Cmd.getInstance(information);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x34;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						sendInformation(role,DataFactory.addByteArray(head, SlaverOp.getSlaverWorkPriz(id, cmd).getResponse()));
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}else if(protocol.isCancleSlaverWork(information)){
				/* 领取工人打工奖励 */
				Cmd cmd = Cmd.getInstance(information);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte) 0x34;
				head[1] = (byte) 0x01;
				long id = DataFactory.doubleBytesToLong((byte[]) role.getAttribute(ROLE_ID));
				if(canDo(id, id)){
					try{
						//sendInformation(role,DataFactory.addByteArray(head, SlaverOp.getSlaverWorkPriz(id, cmd).getResponse()));
						SlaverOp.cancleSlaverWork(id, cmd);
					}catch(Exception e){
						log.error(e, e);
						byte[] content = "E80".getBytes("UTF-8");
						sendException(role, information, content);
					}
				}else{
					kill(role);
				}
				long spend = System.currentTimeMillis() - time;
				if (spend > PERIOD) {
					log.info("speed tower spend time :" + spend);
				}
				return;
			}
			
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static byte[] compressData(byte[] data) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DeflaterOutputStream zos = new DeflaterOutputStream(bos);
		zos.write(data);
		zos.close();
		byte[] byteArray = bos.toByteArray(); 
		return byteArray;
	}
	
	public static byte[] decompressData(byte[] data) throws IOException, DataFormatException {
		Inflater decompresser = new Inflater();
		decompresser.setInput(data); 
		byte[] result = new byte[1048576];
		int resultLength = decompresser.inflate(result); 
		decompresser.end();
		return Arrays.copyOf(result, resultLength);
	}
	
	public void sendError(Instance role, byte information[], String msg) throws Exception{
		byte[] head = DataFactory.get(information, 0, 10);
		head[0] = (byte) 0xF0;
		byte[] msessage = new byte[0];
		msessage = DataFactory.addByteArray(head, DataFactory.addNoContainLength(0, msg.getBytes("UTF-8")));
		sendInformation(role, msessage);
	}
	
	/**
	 * role.setRoleAttribute(ROLE_ID)
	 * role.setRoleAttribute(ROLE_NAME)
	 * role.setRoleAttribute(ROLE_IP)
	 * role.setAttachObject(AttachObject.SESSION)
	 * 
	 */
	public static boolean initRoleInformation(InformationFormat infor) throws UnsupportedEncodingException {
		byte[] information = infor.getInformation();
		//ID(8B)	name(2B+NB)	sex(1B)	URL(2B+NB)	Session_Key (2B+NB) timezone(4B)
		int nameLength = DataFactory.getInt(DataFactory.get(information, 18, 2));
		String name = new String(information, 20, nameLength, "utf-8");
		boolean sex = information[nameLength + 20] == 0x01;
		int URLLength = DataFactory.getInt(DataFactory.get(information, nameLength + 21, 2));
		//int SessionLength = DataFactory.getInt(DataFactory.get(information, URLLength + nameLength + 23, 2));
		//String session = new String(information, URLLength + nameLength + 25, SessionLength, "utf-8") ;
		int URLStartIndex = nameLength + 23;
		//String URL = new String(information, URLStartIndex, URLLength, "utf-8");
		Instance role = infor.getInstance();
		role.setAttribute(SESSION_DESTINATION, DataFactory.get(information, 0, 10));
		long id = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
		role.setAttribute(Attribute.UID, id);
		role.setAttribute("sex", sex);
		role.setAttribute(ROLE_ID, DataFactory.get(information, 10, 8));
		role.setAttribute(ROLE_NAME, DataFactory.get(information, 20, nameLength));
		byte[] nameAndURL = DataFactory.get(information,10, nameLength + 11);
		nameAndURL = DataFactory.addByteArray(nameAndURL, DataFactory.get(information, URLStartIndex - 2, URLLength + 2));
		//role.setRoleAttribute(ROLE_NAME_AND_URL, nameAndURL);
		//role.setRoleAttribute(ROLE_SESSION_AND_URL, DataFactory.get(information, 10, information.length - 10));
		int sessionStart = 25 + nameLength + URLLength;
		int sessionKeyLen = DataFactory.getInt(DataFactory.get(information, sessionStart - 2, 2));
		byte[] sessionkey =  DataFactory.get(information, sessionStart, sessionKeyLen);
		role.setAttachObject(AttachObject.SESSION, sessionkey);
		log.info("Login role infomation(ID: " + id + " Name: " + name + ") ");
		//role.setAttachObject(AttachObject.ROLE_BEAN,user); 
		if(SessionKeyCheckOp.check(id, new String(sessionkey, "UTF-8"))){
			return true;
		}else{
			kill(role);
			return false;
		}
	}
	
	public String getSessionKey(byte[] session_and_url){
		return null;
	}
	
	public static void sendInformation(Instance role, byte[] information) throws IOException {
		role.send(information);
		/*if(DEBUG){
			if(DataFactory.get(information, 0, 1)[0] != (byte)0xf2 && DataFactory.get(information, 0, 1)[0] != (byte)0x84){
				Object obj = role.getAttribute(ROLE_ID);
				if(obj != null){
					long id = DataFactory.doubleBytesToLong((byte[]) obj);
					log.info("send:" + id + "+" + DataFactory.getHexBytes(information));
				}
			}else{
				log.info("send:" + "+" + DataFactory.getHexBytes(information));
			}
		}*/
	}
	
	private class Protocol {
		public boolean isLogin(byte[] infor){
			if (infor[0] == 0x01 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}

		public boolean isEnterScene(byte[] infor) {
			if (infor[0] == (byte)0x03 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetTime(byte[] infor) {
			if (infor[0] == (byte)0xf1 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetStorage(byte[] infor) {
			if (infor[0] == 0x05 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetAppFriendInfo(byte[] infor) {
			if (infor[0] == 0x07 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetTempBox(byte[] infor) {
			if (infor[0] == 0x09 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isMixBlueprint(byte[] infor) {
			if (infor[0] == 0x0b && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isUpBlueprint(byte[] infor) {
			if (infor[0] == 0x0c && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isMixStone(byte[] infor) {
			if (infor[0] == 0x0e && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isStore(byte[] infor) {
			if (infor[0] == 0x10 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isBreakBP(byte[] infor) {
			if (infor[0] == 0x11 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetPVE(byte[] infor) {
			if (infor[0] == 0x13 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isPVEResult(byte[] infor) {
			if (infor[0] == 0x15 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isPickSceneGoods(byte[] infor) {
			if (infor[0] == 0x16 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isBuildTower(byte[] infor) {
			if (infor[0] == 0x19 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isCatchSlaver(byte[] infor) {
			if (infor[0] == 0x1a && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isUpgradTower(byte[] infor) {
			if (infor[0] == 0x1c && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isRepairTower(byte[] infor) {
			if (infor[0] == 0x1d && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isRemoveTower(byte[] infor) {
			if (infor[0] == 0x1e && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isCreateTowerOrMonster(byte[] infor) {
			if (infor[0] == 0x1f && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isStoreTowerOrMonster(byte[] infor) {
			if (infor[0] == 0x21 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetMissionQueue(byte[] infor) {
			if (infor[0] == 0x22 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isRepairCastle(byte[] infor) {
			if (infor[0] == 0x24 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isUpgradCastle(byte[] infor) {
			if (infor[0] == 0x25 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isCancelCreateQueue(byte[] infor) {
			if (infor[0] == 0x26 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isEnterCity(byte[] infor) {
			if (infor[0] == 0x27 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetTitle(byte[] infor) {
			if (infor[0] == 0x29 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isUseTitle(byte[] infor) {
			if (infor[0] == 0x2a && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isFlipCard(byte[] infor) {
			if (infor[0] == 0x2b && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isTempBoxToStorage(byte[] infor) {
			if (infor[0] == 0x2d && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isResetCity(byte[] infor) {
			if (infor[0] == 0x2f && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isLockStorage(byte[] infor) {
			if (infor[0] == 0x32 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isDeleteStorage(byte[] infor) {
			if (infor[0] == 0x33 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetPVPList(byte[] infor) {
			if (infor[0] == 0x34 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetPVP(byte[] infor) {
			if (infor[0] == 0x36 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isPvpResult(byte[] infor) {
			if (infor[0] == 0x38 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isRecivePVPNotify(byte[] infor) {
			if (infor[0] == 0x3f && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isGetTaskList(byte[] infor) {
			if (infor[0] == 0x42 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		public boolean isAcceptTask(byte[] infor) {
			if (infor[0] == 0x44 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isTaskReward(byte[] infor) {
			if (infor[0] == 0x46 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetMessage(byte[] infor) {
			if (infor[0] == 0x48 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetEnemy(byte[] infor) {
			if (infor[0] == 0x4a && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isBugGoods(byte[] infor) {
			if (infor[0] == 0x4c && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isPickMaterial(byte[] infor) {
			if (infor[0] == 0x51 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isModifyRace(byte[] infor) {
			if (infor[0] == 0x53 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetSinglePvpList(byte[] infor) {
			if (infor[0] == 0x54 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetSoulTower(byte[] infor) {
			if (infor[0] == 0x56 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isUpgradeSoulTower(byte[] infor) {
			if (infor[0] == 0x58 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isCancelUpgradeSoulTower(byte[] infor) {
			if (infor[0] == 0x59 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isUpgradeMine(byte[] infor) {
			if (infor[0] == 0x5c && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isCollectMine(byte[] infor) {
			if (infor[0] == 0x5e && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetMine(byte[] infor) {
			if (infor[0] == 0x61 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isSpeedup(byte[] infor) {
			if (infor[0] == 0x63 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetUserTec(byte[] infor) {
			if (infor[0] == 0x6a && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isUpgradeTecTower(byte[] infor) {
			if (infor[0] == 0x68 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isUpgradeTecTree(byte[] infor) {
			if (infor[0] == 0x66 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isOpenLotteryBox(byte[] infor) {
			if (infor[0] == 0x6c && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetLotteryPrize(byte[] infor) {
			if (infor[0] == 0x6e && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isOpenPresentPack(byte[] infor) {
			if (infor[0] == 0x70 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isExpandStorage(byte[] infor) {
			if (infor[0] == 0x73 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isReadMessage(byte[] infor) {
			if (infor[0] == 0x75 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetActivity(byte[] infor) {
			if (infor[0] == 0x77 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isDeleteMessage(byte[] infor) {
			if (infor[0] == 0x7a && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetLoginAwardInfo(byte[] infor) {
			if (infor[0] == 0x7b && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetLoginAward(byte[] infor) {
			if (infor[0] == 0x7d && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetSlaverList(byte[] infor) {
			if (infor[0] == 0x7f && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetCopy(byte[] infor) {
			if (infor[0] == (byte)0x80 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isOpenCopy(byte[] infor) {
			if (infor[0] == (byte)0x82 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isRequestLogin(byte[] infor) {
			if (infor[0] == (byte)0x85 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}

		public boolean isOpenChest(byte[] infor) {
			if (infor[0] == (byte)0x87 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}

		public boolean isGetPVPState(byte[] infor) {
			if (infor[0] == (byte)0x89 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isOpenSack(byte[] infor) {
			if (infor[0] == (byte)0x91 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isSynthesizeBlueprint(byte[] infor) {
			if (infor[0] == (byte)0x93 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isTurntable(byte[] infor) {
			if (infor[0] == (byte)0x95 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isReset(byte[] infor) {
			if (infor[0] == (byte)0x98 && infor[1] == 0x00 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isStartPVE(byte[] infor) {
			if (infor[0] == (byte)0x00 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isSpeedTower(byte[] infor) {
			if (infor[0] == (byte)0x02 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean openLuckBox(byte[] infor) {
			if (infor[0] == (byte)0x05 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isPvn(byte[] infor) {
			if (infor[0] == (byte)0x07 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isCompensate(byte[] infor){
			if (infor[0] == (byte)0x09 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isUpdateCompensate(byte[] infor){
			if (infor[0] == (byte)0x11 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isAddSp(byte[] infor){
			if (infor[0] == (byte)0x13 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isRemoveSlaver(byte[] infor){
			if (infor[0] == (byte)0x15 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isInviteFriends(byte[] infor){
			if (infor[0] == (byte)0x17 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isCashInviteFriends(byte[] infor){
			if (infor[0] == (byte)0x18 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isBatchPickup(byte[] infor){
			if (infor[0] == (byte)0x20 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isDoTask(byte[] infor){
			if (infor[0] == (byte)0x04 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isPickFriend(byte[] infor){
			if (infor[0] == (byte)0x22 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetPeoples(byte[] infor){
			if (infor[0] == (byte)0x24 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isSendLove(byte[] infor){
			if (infor[0] == (byte)0x26 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetSendedLoves(byte[] infor){
			if (infor[0] == (byte)0x27 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isAddMaxSp(byte[] infor){
			if (infor[0] == (byte)0x29 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isSlaverWork(byte[] infor){
			if (infor[0] == (byte)0x31 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isGetSlaverWorkPriz(byte[] infor){
			if (infor[0] == (byte)0x33 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
		
		public boolean isCancleSlaverWork(byte[] infor){
			if (infor[0] == (byte)0x35 && infor[1] == 0x01 && infor[2] == 0x00) {
				return true;
			}
			return false;
		}
	}
	
	private void printProtocol(InformationFormat infor){
		if(DEBUG){
			try {
				Instance role = infor.getInstance();
				byte[] information = infor.getInformation();
				String protocol = DataFactory.getHexBytes(information);
				log.info("receive protocol:" + protocol);
				Object obj =  role.getAttribute(ROLE_ID);
				if(obj != null){
					log.info("receive:" + DataFactory.doubleBytesToLong((byte[]) obj) + "+" + protocol);
				}else{
					log.error("user doesn't login and protocol is:" + protocol);
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}	
	
	/*public static TDDispose getInstance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new TDDispose();
				}
			}
		}
		return instance;
	}*/

	public static void kill(Instance role){
		role.close();
	}
	
	public static boolean canDo(long id, long sceneID){
		if(id == sceneID){
			return UserMemory.canDo(id);
		}else{
			UserMemory.createFriendMem(sceneID);
			return true;
		}
	}
	
	public static void sendException(Instance role, byte[] information, byte[] content) throws IOException {
		byte[] head = DataFactory.get(information, 0, 10);
		head[0] = (byte) 0xf3;
		role.send(DataFactory.addByteArray(head, content));
	}

	public static byte[] requestLogin(long id, byte[] information){
		if(!CAN_OPEN_MORE_CLIENT){
			final Instance role = IF.getInstaceByAttr(Instance.Attribute.UID, id);
			if (role != null) {
				log.info("id:" + id + " alread login and role:" + role);
				byte[] head = DataFactory.get(information, 0, 10);
				head[0] = (byte)0x84;
				try {
					sendInformation(role, head);
				} catch (IOException e) {
					log.error(e, e);
				}
				new Thread() {
					public void run() {
						try {
							Thread.sleep(5);
						} catch (InterruptedException e) {
							log.error(e, e);
						}
						kill(role);
					}
				}.start();
			}
			return new byte[]{0x00};
		} else {
			return new byte[]{0x00};
		}
	}
	
	public static int getOnlieNum(){
		return IF.getInstances().size();
	}
	
	public static Collection<Instance> getOnlieUser(){
		return IF.getInstances().values();
	}
}
 