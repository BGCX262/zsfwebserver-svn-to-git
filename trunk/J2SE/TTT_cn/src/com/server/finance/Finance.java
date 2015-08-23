package com.server.finance;

import com.database.model.bean.FinanceBean;

public interface Finance {
	/**
	 * <h1>not permit to modify the return value</h1>
	 */
	public FinanceBean getFinance(long id);
	public boolean charge(FinanceBean bean);
	public boolean consume(FinanceBean addend);
	/*public boolean isMaster(long id);*/
	public boolean afford(FinanceBean augend, FinanceBean addend);
	public void offline(long id);
}
