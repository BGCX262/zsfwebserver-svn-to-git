package com.server.util;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.sf.json.JSONArray;

public class Configuration {
/*	public static final Properties PROPS = new Properties();
	static{
		String configFile = System.getProperty("config","config.properties");
		if(configFile != null){
			InputStream is = Configuration.class.getResourceAsStream(configFile);
			if(is == null){
				is = Configuration.class.getClassLoader().getResourceAsStream(configFile);
			}
			if(is == null){
				try{
					is = new FileInputStream("config/" + configFile);
				}catch(FileNotFoundException e){
				}
			}
			if(is != null){
				try{
					PROPS.load(is);
				}catch(IOException e){
				}finally{
					try{
						is.close();
					}catch(IOException e){
						
					}
				}
			}
			if(is == null){
				System.out.println("can't read the config.properties");
			}
		}
	}
	
	public static String get(String key){
		return PROPS.getProperty(key);
	}
	public static String get(String key, String defaultValue){
		return PROPS.getProperty(key, defaultValue);
	}
	public static void set(String key, String value){
		PROPS.setProperty(key, value);
	}*/
	
	
	private static final FileListener defaultProperties = FileListener.getInstanceByName("config/config.properties");
	
	static{
		defaultProperties.startListening();
	}
	
	public static String get(String key) {
		return defaultProperties.getProperty(key, null);
	}

	public static String get(String key, String defaultValue) {
		return defaultProperties.getProperty(key, defaultValue);
	}
	
	public static int getInt(String key, int defaultValue){
		String value = get(key);
		if(value != null){
			try{
				return Integer.parseInt(value);
			}catch(NumberFormatException e){
			}
		}
		return defaultValue;
	}
	
	public static double getDouble(String key, double defaultValue){
		String value = get(key);
		if(value != null){
			try{
				return Double.valueOf(value);
			}catch(NumberFormatException e){
			}
		}
		return defaultValue;
	}
	public static long getLong(String key, long defaultValue){
		String value = get(key);
		if(value != null){
			try{
				return Long.valueOf(value);
			}catch(NumberFormatException e){
			}
		}
		return defaultValue;
	}
	
	public static boolean getBoolean(String key, boolean defaultValue){
		String value = get(key);
		if("true".equalsIgnoreCase(value)){
			return true;
		}else if("false".equalsIgnoreCase(value)){
			return false;
		}
		return defaultValue;
	}
	
	public static int getDbindex(){
		return getInt("hibernate.index", 1);
	}
	
	public static int getSecond(){
		return getInt("second", 1000);
	}
	public static int getMinute(){
		return getInt("minute", 60000);
	}
	public static int getHour(){
		return getInt("hour", 3600000);
	}
	public static int getDay(){
		return getInt("day", 86400000);
	}
	public static int getTime(){
		return getInt("time", 2000);
	}
	/**
	 * @return
	 *      1 facebook
	 * 		2 myspace
	 */
	public static int getServerVersion() {
		return getInt("server.version", 1);
	}
	public static boolean isFacebookVersion() {
		if(getServerVersion() == 1){
			return false;
		}else{
			return false;
		}
	}
	public static boolean isMyspaceVersion() {
		if(getServerVersion() == 2){
			return false;
		}else{
			return false;
		}
	}
	public static boolean isLog(){
		return getBoolean("log.record", false);
	}
	public static int getDebugTime() {
		return getInt("com.server.dedug.time", 100);
	}
	
	public static int getCasCacTime() {
		return getInt("castle.cache.time", 30000);
	}
	public static int getFriCasCacTime() {
		return getInt("friend.castle.cache.time", 60000);
	}
	public static int getMasCasCacTime() {
		return getInt("master.castle.cache.time", 120000);
	}
	
	public static int getCitCacTime() {
		return getInt("city.cache.time", 20000);
	}
	public static int getFriCitCacTime() {
		return getInt("friend.city.cache.time", 60000);
	}
	public static int getMasCitCacTime() {
		return getInt("master.city.cache.time", 120000);
	}
	
	public static int getSlaCacTime() {
		return getInt("slaver.cache.time", 20000);
	}
	public static int getFriSlaCacTime() {
		return getInt("friend.slaver.cache.time", 60000);
	}
	public static int getMasSlaCacTime() {
		return getInt("master.slaver.cache.time", 120000);
	}
	
	public static int getStoCacTime() {
		return getInt("storage.cache.time", 20000);
	}
	public static int getFriStoCacTime() {
		return getInt("friend.storage.cache.time", 60000);
	}
	public static int getMasStoCacTime() {
		return getInt("master.storage.cache.time", 120000);
	}
	public static int getMasterValidTime() {
		return getInt("server.master.valid.time", 30 * 60000);
	}
	public static int getFriendValidTime() {
		return getInt("server.friend.valid.time", 5 * 60000);
	}
	public static int getDropTime() {
		return getInt("server.finance.drop.time", 5 * 60000);
	}
	public static int getPersistTime() {
		return getInt("server.finance.persist.time", 60000);
	}
	public static int getMaxPersistTime() {
		return getInt("server.finance.persist.max.time", 60000);
	}
	public static int getTimesMonsterNum(){
		return getInt("server.fight.times.monster.num", 20);
	}
	public static int getFightCacheTime() {
		return getInt("server.fight.cache.time", 600000);
	}
	public static double getPossessRate() {
		return getDouble("server.pick.scene.goods.possess.rate", 0.5);
	}
	public static int getSceneGoodsNum() {
		return getInt("server.scene.goods.num", 100);
	}
	public static int getBreakStoneGoodsID() {
		return getInt("server.goods.breakstone.goodsID", 100001);
	}
	public static int getPvpLevel() {
		return getInt("server.pvp.castle.level", 1);
	}
	public static int getTaskCacheTime() {
		return getInt("server.task.cache.time", 20000);
	}
	public static int getMasterTaskCacheTime() {
		return getInt("server.master.task.cache.time", 120000);
	}
	public static int getFriendTaskCacheTime() {
		return getInt("server.friend.task.cache.time", 60000);
	}
	public static int getSystemCreateGoodsNum() {
		return getInt("server.create.goods.num", 3);
	}
	public static int getChanllengeMinTimesNum() {
		return getInt("server.pve.chanllenge.min.times.num", 100000);
	}
	public static int getChanllengeMaxTimesNum() {
		return getInt("server.pve.chanllenge.max.times.num", 100010);
	}
	public static int getSaveEnemyNum() {
		return getInt("server.pvp.enemy.num", 20);
	}
	public static boolean getIsWriteGameLog() {
		return getBoolean("server.write.log", true);
	}
	public static int getInitCoin() {
		return getInt("server.init.coin", 2000);
	}
	public static int getInitCash() {
		return getInt("server.init.cash", 0);
	}
	public static int getInitRock() {
		return getInt("server.init.rock", 1000);
	}
	public static int getInitMetal() {
		return getInt("server.init.metal", 1000);
	}
	public static int getInitCrystal() {
		return getInt("server.init.crystal", 1000);
	}
	public static int getInitSystemCash() {
		return getInt("server.init.systemCash", 20);
	}
	public static int getInitSoul() {
		return getInt("server.init.soul", 0);
	}
	public static int getInitEnergy() {
		return getInt("server.init.energy", 15);
	}
	public static int getInitGlory() {
		return getInt("server.init.glory", 10);
	}
	public static JSONArray getInitElementBag() {
		return JSONArray.fromObject(get("server.init.storage.element", "[]"));
	}
	public static int getSoulCacheTime() {
		return getInt("soultower.cache.time", 60000);
	}
	public static int getMasterSoulCacheTime() {
		return getInt("master.soultower.cache.time", 5 * 60000);
	}
	
	public static int getTecCacheTime() {
		return getInt("tecnology.cache.time", 60000);
	}
	public static int getMasterTecCacheTime() {
		return getInt("master.tecnology.cache.time", 5 * 60000);
	}
	public static int getFreshUserProtectTime() {
		return getInt("fresh.user.protect.time", 604800000);
	}
	public static int getUserProtectTime() {
		return getInt("user.protect.time", 8 * 3600000);
	}
	public static int getGoodsRefreshTime() {
		return getInt("goods.refresh.time", 60000);
	}
	public static int getPickRock() {
		return getInt("pick.rock", 50);
	}
	public static int getPickCoin() {
		return getInt("pick.coin", 50);
	}
	public static int getPickMetal() {
		return getInt("pick.metal", 50);
	}
	public static int getPickCrystal() {
		return getInt("pick.crystal", 50);
	}
	public static int getPickIncrment() {
		return getInt("pick.increment", 100);
	}
	public static int getLotteryNum() {
		return getInt("lottery.num", 3);
	}
	public static int getLotterValidTime() {
		return getInt("lottery.goods.valid.time", Integer.MAX_VALUE);
	}
	public static boolean getIsWriteGameDate() {
		return getBoolean("server.write.gamedate", true);
	}
	public static boolean getWriteGameLog() {
		return getBoolean("server.write.gamelog", true);
	}
	public static boolean printProtocol() {
		return getBoolean("server.protocol.print", false);
	}
	public static int getSpPveConsume() {
		return getInt("sp.pve.consume", 1);
	}
	public static int getSpPvpConsume() {
		return getInt("sp.pvp.consume", 2);
	}
	public static int getSpPvnConsume() {
		return getInt("sp.pvn.consume", 2);
	}
	public static int getSpCityResetConsume() {
		return getInt("sp.city.reset.consume", 10);
	}
	public static int getSpTaskChargeConsume() {
		return getInt("sp.task.charge.consume", 3);
	}
	public static double getCancleCastleUpdateRetrun() {
		return getDouble("castle.update.cancel.return", 0);
	}
	public static double getCancleCastleRepairReturn() {
		return getDouble("castle.repair.cancel.return", 0);
	}
	public static double getCancelSoultowerUpdateReturn() {
		return getDouble("soultower.update.cancel.return", 0);
	}
	public static double getCancelTecUpdateReturn() {
		return getDouble("technology.update.cancel.return", 0);
	}
	public static double getCancelRockmineUpdateReturn() {
		return getDouble("rockmine.update.cancel.return", 0);
	}
	public static double getCancelMetalmineUpdateReturn() {
		return getDouble("metalmine.update.cancel.return", 0);
	}
	public static double getCancelCrystalmineUpdateReturn() {
		return getDouble("srystalmine.update.cancel.return", 0);
	}
	public static boolean isSendOnlineNum() {
		return getBoolean("online.num.send", true);
	}

	public static String getShutdownTime() {
		return get("server.shutdownTime", null);
	}

	public static String getActivityEndTime() {
		return get("server.activity.endTime", null);
	}

	public static int getRepairCastleCostCoin() {
		return getInt("castle.repaire.cost.coin", 1);
	}

	public static int getRepairCastleCostRock() {
		return getInt("castle.repaire.cost.rock", 2);
	}

	public static int getRepairCastleCostMetal() {
		return getInt("castle.repaire.cost.metal", 2);
	}

	public static int getRepairCastleCostCrystal() {
		return getInt("castle.repaire.cost.crystal", 2);
	}

	public static int getInitBadge() {
		return getInt("server.init.badge", 0);
	}

	public static int getResetLimitLevel() {
		return getInt("server.race.reset.limit.level", 3);
	}

	public static int getCopyCacheTime() {
		return getInt("server.copy.cache.time", 2000);
	}

	public static int getMasterCopyCacheTime() {
		return getInt("server.master.copy.cache.time", 600000);
	}
	public static int getFriendCopyCacheTime() {
		return getInt("server.friend.copy.cache.time", 300000);
	}

	public static int getMaxPveCity() {
		return getInt("server.pve.max.city", 12);
	}

	public static int getFriendNumLimit() {
		return getInt("friend.num.limit", 200);
	}

	public static boolean canOpenMoreClient() {
		return getBoolean("server.client.open.more", false);
	}
	
	public static int getMineCacheTime() {
		return getInt("server.mine.cache.time", 2000);
	}

	public static int getMasterMineCacheTime() {
		return getInt("server.master.mine.cache.time", 600000);
	}
	
	public static String getSlaverWorkPrize() {
		return get("server.slaver.work.prize", "[[60006,100]]");
	}
	
	public static String getStatDestAddr() {
		return get("stat.dest.addr", "http://67.208.221.190:8847/SNSDate/online.jsp");
	}

	public static boolean needAddMainTask() {
		return getBoolean("need.add.main.task", true);
	}

	public static boolean isPvpProtect() {
		return getBoolean("pvp.protect", false);
	}

	public static int getMaxCastleLevel() {
		return getInt("castle.max.level", 10);
	}

	public static int getMaxMineLevel() {
		return getInt("mine.max.level", 10);
	}
	
	public static int getMaxTecTreeLevel() {
		return getInt("technology.tree.max.level", 5);
	}

	public static int getNormalStorageLimit() {
		return getInt("storge.num.limit", 27);
	}

	public static int getSecurityTime() {
		return getInt("security.time", 60 * 5);
	}

	public static int getOnlineType() {
		return getInt("online.type", 1);
	}
}
