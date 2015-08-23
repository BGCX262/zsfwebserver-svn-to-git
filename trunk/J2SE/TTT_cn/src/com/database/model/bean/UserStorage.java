package com.database.model.bean;

public class UserStorage {
	private long id;
	private int boxID;
	private long masterID;
	private int type;//1存储在普通仓库  2存储在暂存仓库 3 隐形背包 4 无限背包
	private boolean full;
	private volatile int change;//0没改变   1改变:save  2改变 :update  3null值
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11储物箱及其他
	private int goodID;
	private int num;
	private long validTime = Long.MAX_VALUE;//有效期
	private String markIDs;//物品markID串
	private boolean locked;
		
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		if((this.change == 1 && change == 2)){
			return;
		}else{
			this.change = change;
		}
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getMarkIDs() {
		return markIDs;
	}
	public void setMarkIDs(String markIDs) {
		this.markIDs = markIDs;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public int getBoxID() {
		return boxID;
	}
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}
	public long getValidTime() {
		return validTime;
	}
	public void setValidTime(long validTime) {
		this.validTime = validTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public boolean isFull() {
		return full;
	}
	public void setFull(boolean full) {
		this.full = full;
	}
	
	public String toString() {
		return "{" + goodID + ",1}";
	}
	/*public static List<UserStorage> decodeFromJa(JSONArray array){
		List<UserStorage> list = new LinkedList<UserStorage>();
		try{
			if(array != null){
				for(int i = 0; i < array.size(); i++){
					UserStorage storage = new UserStorage();
					JSONArray ja = array.getJSONArray(i);
					storage.setCate(ja.getInt(0));
					storage.setGoodID(ja.getInt(1));
					storage.setId(ja.getInt(2));
					storage.setLocked(ja.getBoolean(3));
					storage.setNum(ja.getInt(4));
					storage.setMarkIDs(ja.getString(5));
					storage.setBoxID(ja.getInt(6));
					list.add(storage);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return list;
	}
	public static JSONArray encode2Ja(List<UserStorage> list){
		JSONArray array = new JSONArray();
		try{
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					UserStorage storage = list.get(i);
					JSONArray ja = new JSONArray();
					ja.add(storage.getCate());
					ja.add(storage.getGoodID());
					ja.add(storage.getId());
					ja.add(storage.isLocked());
					ja.add(storage.getNum());
					ja.add(storage.getMarkIDs());
					ja.add(storage.getBoxID());
					array.add(ja);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return array;
	}*/
}
