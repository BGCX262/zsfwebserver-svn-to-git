/**
 * @author zsf
 * 2011-9-21 下午03:52:19
 */
package com.server.database;

import java.io.Serializable;
import java.util.List;

/**
 * Interface DBHelper
 */
public interface IDBHelper {

	/**
	 * Save Object
	 * @param obj
	 * @return
	 */
	public Serializable save(Object obj);

	/**
	 * Save or Upadte Object
	 * @param obj
	 */
	public void saveOrUpdate(Object obj);

	/**
	 * Update Object
	 * @param obj
	 */
	public void update(Object obj);

	/**
	 * Delete Object
	 * @param obj
	 */
	public void delete(Object obj);

	/**
	 * Batch Delete Object
	 * @param list
	 */
	public void deleteAll(List<?> list);

	/**
	 * Get bean by ClassName and id
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public Object get(Class<?> entityClass, Serializable id);

	/**
	 * Get beans by hql
	 * @param hql
	 * @return
	 */
	public List<?> query(String hql);

	/**
	 * Get beans by hql for page
	 * @param hql
	 * @param offset Page start index
	 * @param length Page size
	 * @return
	 */
	public List<?> pageQuery(String hql, int offset, int length);

}
