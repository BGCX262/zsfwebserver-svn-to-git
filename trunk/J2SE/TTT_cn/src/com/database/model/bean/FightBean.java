package com.database.model.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class FightBean {

	private int timesID;// 波次ID
	private List<AttackMonster> monsters;
	private List<AttackMonster> bosses;

	public int getTimesID() {
		return timesID;
	}

	public void setTimesID(int timesID) {
		this.timesID = timesID;
	}

	public List<AttackMonster> getMonsters() {
		return monsters;
	}

	public void setMonsters(List<AttackMonster> monsters) {
		this.monsters = monsters;
	}

	public List<AttackMonster> getBosses() {
		return bosses;
	}

	public void setBosses(List<AttackMonster> bosses) {
		this.bosses = bosses;
	}

	public String encode2Ja() {
		JSONArray ja = new JSONArray();
		ja.add(this.timesID);
		JSONArray jar = new JSONArray();
		if (monsters != null && monsters.size() > 0) {
			Iterator<AttackMonster> monsterIte = monsters.iterator();
			while (monsterIte.hasNext()) {
				AttackMonster monster = monsterIte.next();
				jar.add(monster.encode2Ja());
			}
		}
		ja.add(jar.toString());

		JSONArray jar2 = new JSONArray();
		if (bosses != null && bosses.size() > 0) {
			Iterator<AttackMonster> monsterIte = bosses.iterator();
			while (monsterIte.hasNext()) {
				AttackMonster monster = monsterIte.next();
				jar2.add(monster.encode2Ja());
			}
		}
		ja.add(jar2.toString());
		return ja.toString();
	}

	public static FightBean decode2Bean(String strJa) {
		FightBean bean = null;
		if (strJa != null) {
			JSONArray ja = JSONArray.fromObject(strJa);
			if (ja != null && ja.size() > 0) {
				bean = new FightBean();
				bean.setTimesID(ja.getInt(0));
				List<AttackMonster> monsters = new LinkedList<AttackMonster>();
				JSONArray jar = ja.getJSONArray(1);
				for (int i = 0; i < jar.size() && jar.size() > 0; i++) {
					String str = jar.getString(i);
					if (str != null) {
						monsters.add(AttackMonster.decode2Bean(str));
					}
				}
				bean.setMonsters(monsters);

				List<AttackMonster> bosses = new LinkedList<AttackMonster>();
				JSONArray jar2 = ja.getJSONArray(2);
				for (int i = 0; i < jar2.size() && jar2.size() > 0; i++) {
					String str = jar2.getString(i);
					if (str != null) {
						bosses.add(AttackMonster.decode2Bean(str));
					}
				}
				bean.setBosses(bosses);
			}
		}
		return bean;
	}
}