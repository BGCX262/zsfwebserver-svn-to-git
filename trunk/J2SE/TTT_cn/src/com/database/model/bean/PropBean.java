package com.database.model.bean;

public class PropBean {// 道具
	private int id;
	private int goodID;// 商品ID
	private int cate;// 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private int type;// 1加速道具 2加精力值道具 3pve道具 4怪物卡道具 5加coin道具 6加石头道具 7加金属道具
						// 8加水晶道具9加灵魂道具10pvp道具 11重置道具 12加徽章道具
	private String name;// 道具名称
	private long time;// 时效或者有效时间
	private String result;// 道具作用、设置的具体数值
	private String resourceName;// 道具资源编号
	private int sortID;// 排序
	private String comment;
	private int color;
	private String keyIDs; // 需要开启的物品ID. 多个ID用,分割

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGoodID() {
		return goodID;
	}

	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getSortID() {
		return sortID;
	}

	public void setSortID(int sortID) {
		this.sortID = sortID;
	}

	public String getKeyIDs() {
		return keyIDs;
	}

	public void setKeyIDs(String keyIDs) {
		this.keyIDs = keyIDs;
	}

}
