package com.server.user.operation;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.LoginLog;
import com.database.model.bean.TaskBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserMine;
import com.database.model.bean.UserSlavers;
import com.database.model.bean.UserTask;
import com.database.model.bean.UserTasks;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.goods.PrizeType;
import com.server.util.Cmd;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class TaskOp {

	private static Log log = LogFactory.getLog(TaskOp.class);
	private static final int DAY = 3600000 * 24;
	private static List<TaskBean> DAILYTASK = null;
	private static List<TaskBean> INITTASK = null;
	private static Finance financeImpl = FinanceImpl.instance();
	private static final boolean NEED_ADD_MATINTASK = Configuration.needAddMainTask();
	static {
		DAILYTASK = new LinkedList<TaskBean>();
		INITTASK = new LinkedList<TaskBean>();
		List<TaskBean> taskBeanList = Goods.getGoodsByCate(GoodsCate.TASKBEAN);
		if (taskBeanList != null) {
			Iterator<TaskBean> ite = taskBeanList.iterator();
			while (ite.hasNext()) {
				TaskBean taskBean = ite.next();
				if (taskBean.getTaskType() == 2 || taskBean.getTaskType() == 6 || taskBean.getTaskType() == 7) {
					DAILYTASK.add(taskBean);
				}
				if ((taskBean.getTaskType() == 1 || taskBean.getTaskType() == 4) && taskBean.getPreTask() == 0
						&& taskBean.getAcceptMinLevel() <= 1) {
					INITTASK.add(taskBean);
				}
			}
		}
	}

	public static UserTasks intiTask(long id) {
		UserTasks tasks = new UserTasks();
		tasks.setId(id);
		if (tasks != null) {
			List<UserTask> taskList = new LinkedList<UserTask>();
			if (INITTASK != null) {
				Iterator<TaskBean> ite = INITTASK.iterator();
				while (ite.hasNext()) {
					TaskBean taskBean = ite.next();
					if (taskBean != null && taskBean.getAcceptMinLevel() <= 1) {
						UserTask newTask = new UserTask();
						newTask.setTaskType(taskBean.getTaskType());
						newTask.setMasterID(id);
						newTask.setTaskID(taskBean.getId());
						newTask.setState(1);
						newTask.setChange(1);
						// newTask.setAcceptTime(System.currentTimeMillis());
						taskList.add(newTask);
					} else {
						log.error("Game_Error:init task is not exist");
					}
					tasks.setTasks(taskList);
				}
			}
		}
		if (tasks != null) {
			tasks.setTime(System.currentTimeMillis());
		}
		return tasks;
	}

	public static boolean resetTask(long id, Session session) {
		session.createSQLQuery("delete from usertask where masterID = " + id).executeUpdate();
		List<UserTask> taskList = TaskOp.intiTask(id).getTasks();
		if (taskList != null) {
			Iterator<UserTask> taskIte = taskList.iterator();
			while (taskIte.hasNext()) {
				UserTask task = taskIte.next();
				session.save(task);
			}
		}
		session.createSQLQuery("update loginlog set lastIssueTime = " + 0 + " where masterID = " + id).executeUpdate();
		return true;
	}

	public static Long getIssueTime(long id) {
		//Long issueTime = MemcacheOp.getIssueTime(id);
		Long issueTime = null;
		if (issueTime == null) {
			LoginLog login = (LoginLog) DBUtil.get(id, LoginLog.class);
			if (login != null) {
				issueTime = login.getLastIssueTime();
			}
		}
		if (issueTime == null) {
			issueTime = 0l;
		}
		return issueTime;
	}

	public static void setIssueTime(long id, long issueTime) {
		MemcacheOp.setIssueTime(id, issueTime, 24 * 3600);
		DBUtil.executeUpdate(id, "update loginlog set lastIssueTime = " + issueTime + " where masterID = " + id);
	}

	public static void removeDailyTask(long id) {
		UserTasks userTasks = UserMemory.getTasks(id);
		List<UserTask> taskList = userTasks.getTasks();
		if (taskList != null) {
			Iterator<UserTask> ite = taskList.iterator();
			while (ite.hasNext()) {
				UserTask task = ite.next();
				if (task.getTaskType() == 2 || task.getTaskType() == 6 || task.getTaskType() == 7) {
					task.setChange(3);
				}
			}
		}
	}

	public static void issueDailyTask(long id) {
		long issueTime = getIssueTime(id);
		//long issueTime = 0;
		if (new Date(System.currentTimeMillis()).getDate() != new Date(issueTime).getDate()
				|| System.currentTimeMillis() - issueTime > DAY) {
			removeDailyTask(id);
			UserTasks userTasks = UserMemory.getTasks(id);
			UserCastle castle = UserMemory.getCastle(id);
			int castleId = 1;
			if (castle != null)
				castleId = castle.getCastleID();
			CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castleId);
			List<UserTask> taskList = userTasks.getTasks();
			if (DAILYTASK != null) {
				Iterator<TaskBean> ite = DAILYTASK.iterator();
				while (ite.hasNext()) {
					TaskBean taskBean = ite.next();
					TaskBean preTask = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, taskBean.getPreTask());
					if (taskBean.getAcceptMinLevel() <= bean.getLevel() && 
							taskBean.getAcceptMaxLevel() >= bean.getLevel()) {
						if (taskBean.getPreTask() == 0 ? true : preTask != null && isAlreadAddedTask(taskList, preTask, 3)) {
							UserTask userTask = new UserTask();
							userTask.setMasterID(id);
							userTask.setTaskType(taskBean.getTaskType());
							userTask.setTaskID(taskBean.getId());
							if (taskBean.getTaskType() == 7)
								userTask.setState(2);
							else
								userTask.setState(1);
							userTask.setChange(1);
							taskList.add(userTask);
						}
					}
				}
			}
			setIssueTime(id, System.currentTimeMillis());
		}
	}

	public static byte[] getTaskList(long id, byte[] information) throws Exception {
		byte[] re = DataFactory.getbyte(0);
		UserTasks tasks = UserMemory.getTasks(id);
		if (tasks != null) {
			List<UserTask> taskList = tasks.getTasks();
			if (taskList != null && taskList.size() > 0) {
				addIfNoMainTaks(id, taskList);
				Iterator<UserTask> ite = taskList.iterator();
				int count = 0;
				while (ite.hasNext()) {
					UserTask task = ite.next();
					if (task.getChange() != 3) {
						re = DataFactory.addByteArray(re, DataFactory.getbyte(task.getTaskType()));
						re = DataFactory.addByteArray(re, DataFactory.getbyte(task.getTaskID()));
						re = DataFactory.addByteArray(re, DataFactory.getbyte(task.getState()));
						re = DataFactory.addByteArray(re, DataFactory.getbyte((int) (task.getFinishRate() * 100)));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(task.getAcceptTime()));
						count++;
					}
				}
				DataFactory.replace(re, 0, DataFactory.getbyte(count));
			}
		}
		return re;
	}

	public static void addIfNoMainTaks(long id, List<UserTask> taskList) {
		if (NEED_ADD_MATINTASK) {
			boolean haveMainTask = false;
			if (taskList != null && taskList.size() > 0) {
				List<TaskBean> taskBeanList = Goods.getGoodsByCate(GoodsCate.TASKBEAN);
				Iterator<TaskBean> ite = taskBeanList.iterator();
				while (ite.hasNext()) {
					TaskBean task = ite.next();
					if (task.getTaskType() == 1) {
						TaskBean preTask = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, task.getPreTask());
						if (preTask != null && isAlreadAddedTask(taskList, preTask, 3) && !isAlreadAddedTask(taskList, task)) {
							addBeAcceptTask(id, preTask.getId());
						}
					}
				}
			}

			if (!haveMainTask) {
				// addBeAcceptTask(id, 10064);
			}
		}
	}

	public static byte[] acceptTask(long id, byte[] information) throws Exception {
		byte[] re = DataFactory.get(information, 10, 4);
		re = DataFactory.addByteArray(re, new byte[] { 0x01 });
		int taskID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserTask accpetTask = getTask(id, taskID, 1);
		if (accpetTask != null) {
			accpetTask.setAcceptTime(System.currentTimeMillis());
			accpetTask.setChange(2);
			accpetTask.setState(2);
			DataFactory.replace(re, 4, new byte[] { 0x00 });
		} else {
			TaskBean taskBean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, taskID);
			if (taskBean != null) {
				UserTask finishTask = getTask(id, taskBean.getPreTask(), 4);
				if (finishTask != null) {
					accpetTask = new UserTask();
					accpetTask.setMasterID(id);
					accpetTask.setTaskID(taskID);
					accpetTask.setTaskType(taskBean.getTaskType());
					accpetTask.setAcceptTime(System.currentTimeMillis());
					accpetTask.setChange(2);
					accpetTask.setState(2);
					DataFactory.replace(re, 4, new byte[] { 0x00 });
				}
			} else {
				log.info("Game_Info:the taskID:" + taskID + " is not exist!");
			}
		}
		return re;
	}

	public static void doTask(long id, int taskID, double finishRate) {
		UserTasks tasks = UserMemory.getTasks(id);
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		
		if (tasks != null && checkLevel(id, taskID, bean.getLevel())) {
			UserTask targetTask = getTask(id, taskID, 1);
			if (targetTask != null) {
				targetTask.setFinishRate(targetTask.getFinishRate() + finishRate);
				if (targetTask.getFinishRate() >= 1) {
					targetTask.setState(2);
				}
				targetTask.setChange(2);
			}
		}
	}
	
	/**
	 * 检查成长任务参数，并完成任务
	 * @param id
	 * @param param
	 * @param type
	 */
	public static void checkDoTask(long id, int param, int type, int morethan) {
		List<UserTask> tasks = UserMemory.getTasks(id).getTasks();
		for (Iterator<UserTask> iter = tasks.iterator(); iter.hasNext(); ) {
			UserTask next = iter.next();
			if (next.getTaskType() == 3) {
				TaskBean bean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, next.getTaskID());
				String[] params = bean.getTargetPara().split(",");
				if ((type + "").equals(params[0])) {
					/* type：1城堡等级要求，2通关关卡要求，3副本通关要求 */
					if ((morethan == 1 && param >= Integer.valueOf(params[1]).intValue()) || 
							(morethan == 2 && param == Integer.valueOf(params[1]).intValue()) ||
							(morethan == 3 && param > Integer.valueOf(params[1]).intValue())) {
						TaskOp.doTask(id, bean.getId(), 1);
					}
				}
			}
		}
			
	}

	public static boolean isAlreadAddedTask(List<UserTask> taskList, TaskBean taskBean) {
		if (taskList != null) {
			Iterator<UserTask> ite = taskList.iterator();
			while (ite.hasNext()) {
				UserTask task = ite.next();
				if (task != null && task.getTaskID() == taskBean.getId() && task.getChange() != 3) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isAlreadAddedTask(List<UserTask> taskList, TaskBean taskBean, int state) {
		if (taskList != null) {
			Iterator<UserTask> ite = taskList.iterator();
			while (ite.hasNext()) {
				UserTask task = ite.next();
				if (task != null && task.getChange() != 3 && task.getTaskID() == taskBean.getId() && task.getState() == state) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean addBeAcceptTask(long id, int taskID) {
		List<UserTask> taskList = UserMemory.getTasks(id).getTasks();
		return addBeAcceptTask(id, taskList, taskID);
	}

	public static boolean addBeAcceptTask(long id, List<UserTask> taskList, int taskID) {
		boolean suc = false;
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		TaskBean tempTaskBean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, taskID);
		List<TaskBean> taskBeanList = Goods.getGoodsByCate(GoodsCate.TASKBEAN);
		if (taskBeanList != null && taskList != null) {
			Iterator<TaskBean> ite = taskBeanList.iterator();
			while (ite.hasNext()) {
				TaskBean taskBean = ite.next();
				if (taskBean.getPreTask() == taskID && !isAlreadAddedTask(taskList, taskBean)) {
					UserTask newTask = new UserTask();
					newTask.setTaskID(taskBean.getId());
					newTask.setMasterID(id);
					newTask.setState(1);
					newTask.setTaskType(taskBean.getTaskType());
					newTask.setAcceptTime(System.currentTimeMillis());
					newTask.setChange(1);
					taskList.add(newTask);
					if (tempTaskBean.getTaskType() == taskBean.getTaskType()) {
						suc = true;
					}
				}
			}
		}
		return suc;
	}

	public static void addFllowTask(long id, List<UserTask> taskList) {
		if (taskList != null) {
			int size = taskList.size();
			for (int i = 0; i < size; i++) {
				UserTask task = taskList.get(i);
				if (task != null && task.getState() == 3) {
					if (addBeAcceptTask(id, taskList, task.getTaskID())) {
						task.setChange(3);
					}
				}
			}
		}
	}

	private static UserTask getTask(long id, int taskID, int state) {
		UserTask task = null;
		UserTasks tasks = UserMemory.getTasks(id);
		if (tasks != null) {
			List<UserTask> taskList = tasks.getTasks();
			if (taskList != null) {
				Iterator<UserTask> ite = taskList.iterator();
				while (ite.hasNext()) {
					UserTask tempTask = ite.next();
					if (tempTask.getTaskID() == taskID && tempTask.getState() == state) {
						if (task == null) {
							task = tempTask;
						} else if (tempTask.getAcceptTime() < task.getAcceptTime()) {
							task = tempTask;
						}
					}
				}
			}
		}
		return task;
	}

	public static byte[] taskReward(long id, byte[] information) throws Exception {
		byte[] re = DataFactory.get(information, 10, 4);
		re = DataFactory.addByteArray(re, new byte[] { 0x01 });
		int taskID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserCastle castle = UserMemory.getCastle(id);
		doOtherTask(id);
		boolean normalReward = false;
		if (DataFactory.get(information, 14, 1)[0] == 0x00) {
			normalReward = true;
		}
		UserTask finishTask = getTask(id, taskID, 2);
		if (finishTask != null) {
			TaskBean taskBean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, finishTask.getTaskID());
			if (taskBean != null) {
				if (normalReward) {
					if (taskBean.getPrize() != null) {
						JSONArray ja = JSONArray.fromObject(taskBean.getPrize());
						re = DataFactory.addByteArray(re, DataFactory.getbyte(ja.size()));
						for (int i = 0; i < ja.size(); i++) {
							JSONArray jar = ja.getJSONArray(i);
							int goodsID = jar.getInt(0);
							int num = jar.getInt(1);
							StorageOp.storeGoods(id, goodsID, num);
							re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsID));
							re = DataFactory.addByteArray(re, DataFactory.getbyte(num));
						}
						DataFactory.replace(re, 4, new byte[] { 0x00 });
					}
				} else {
					UserCity city = UserMemory.getCurrentCity(id);
					if (city != null) {
						DropGoodsBean goods =
								Goods.fitGoods(PrizeType.TASKPRIZE, city.getCityID(), null, castle.getRace());
						re = DataFactory.addByteArray(re, DataFactory.getbyte(1));
						re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodsID()));
						re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
						StorageOp.storeGoods(id, goods.getGoodsID(), goods.getNum());
						DataFactory.replace(re, 4, new byte[] { 0x00 });
					} else {
						throw new Exception();
					}
				}
				finishTask.setState(3);
				if (finishTask.getTaskID() == 40014) {
					UserTask otherTask = getTask(id, 40015, 1);
					otherTask.setState(3);
					otherTask.setChange(2);
				} else if (finishTask.getTaskID() == 40015) {
					UserTask otherTask = getTask(id, 40014, 1);
					otherTask.setState(3);
					otherTask.setChange(2);
				}
				if (addBeAcceptTask(id, taskID)) {
					finishTask.setChange(2);
				} else {
					finishTask.setChange(2);
				}
				doOtherTask(id);
			}
		}
		return re;
	}
	
	/**
	 * 判断是否有已完成任务
	 * @param id
	 */
	public static void doOtherTask(long id) {
		try {
			UserCity city = UserMemory.getCity(id, 1);
			UserCastle castle = UserMemory.getCastle(id);
			CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			UserCity maxCity = UserMemory.getCity(id, castle.getMaxCity());
			CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, maxCity.getCityID());
			UserSlavers slaver = UserMemory.getSlavers(id);
			
			if (city.getCurrTimesNum() > 2) {
				TaskOp.doTask(id, 10016, 1);
			} else if (city.getCurrTimesNum() > 1) {
				TaskOp.doTask(id, 10002, 1);
			}
			
			if (bean.getLevel() > 1) {
				TaskOp.doTask(id, 10006, 1);
			}
			if (slaver.getSlavers().size() >= 3)
				TaskOp.doTask(id, 10021, 1);
			
			checkDoTask(id, bean.getLevel(), 1, 1);
			checkDoTask(id, castle.getMaxCity() - (maxCity.getCurrTimesNum() > cityBean.getLastTimes() ? 0 : 1), 2, 1);
			doUpMineTask(id);
			
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	
	public static void doUpMineTask(long id) {
		UserMine userMine = UserMemory.getUserMine(id);

		List<UserTask> tasks = UserMemory.getTasks(id).getTasks();
		for (Iterator<UserTask> iter = tasks.iterator(); iter.hasNext(); ) {
			UserTask next = iter.next();
			if (next.getTaskType() == 3) {
				TaskBean bean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, next.getTaskID());
				String[] params = bean.getTargetPara().split(",");
				if (("7").equals(params[0])) {
					if ("150001".equals(params[1])) {
						int level = userMine.getRockMineState() == 0 ? userMine.getRockMineLevel() : userMine.getRockMineLevel() + 1;
						if (level >= bean.getAcceptMinLevel())
							TaskOp.doTask(id, bean.getId(), 1);
					} else if ("150002".equals(params[1])) {
						int level = userMine.getMetalMineState() == 0 ? userMine.getMetalMineLevel() : userMine.getMetalMineLevel() + 1;
						if (level >= bean.getAcceptMinLevel())
							TaskOp.doTask(id, bean.getId(), 1);
					} else if ("150003".equals(params[1])) {
						int level = userMine.getCrystalMineState() == 0 ? userMine.getCrystalMineLevel() : userMine.getCrystalMineLevel() + 1;
						if (level >= bean.getAcceptMinLevel())
							TaskOp.doTask(id, bean.getId(), 1);
					}
				}
			}
		}
	}
	
	/**
	 * 检查等级是否满足任务要求
	 * @param id
	 * @param taskId
	 * @param level
	 * @return
	 */
	public static boolean checkLevel(long id, int taskId, int level) {
		TaskBean bean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, taskId);
		return bean != null;
	}

	public static void doChallengeTask(long id, int timesID) {
		UserTasks tasks = UserMemory.getTasks(id);
		if (tasks != null) {
			List<UserTask> taskList = tasks.getTasks();
			if (taskList != null) {
				Iterator<UserTask> ite = taskList.iterator();
				while (ite.hasNext()) {
					UserTask task = ite.next();
					if (task.getTaskType() == 5) {
						TaskBean taskBean = (TaskBean) Goods.getById(GoodsCate.TASKBEAN, task.getTaskID());
						if (Integer.valueOf(taskBean.getTargetPara()) == timesID) {
							doTask(id, task.getTaskID(), 1);
						}
					}
				}
			}
		}
	}
	
	public static void protocolDoTask(long id, byte[] information) {
		Cmd req = Cmd.getInstance(information);
		int taskId = req.readInt(10);
		
		doTask(id, taskId, 1);
	}
}
