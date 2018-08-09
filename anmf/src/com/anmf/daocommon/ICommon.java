package com.anmf.daocommon;

import java.util.List;

import com.anmf.exception.CommonException;

/**
 * ANMF框架 操作数据库类
 * 
 * @author Administrator
 * 
 * @param <CRM>
 */
public interface ICommon<ANMF> {
	/**
	 * 插入新记录
	 * 
	 * @param object
	 *            需插入的对象
	 * @return
	 */
	boolean insert(ANMF object) throws CommonException;

	/**
	 * 修改记录
	 * 
	 * @param object
	 *            需修改的对象
	 * @return
	 */
	boolean update(ANMF object) throws CommonException;

	/**
	 * 删除记录
	 * 
	 * @param object
	 *            需删除的对象
	 * @return
	 */
	boolean delete(ANMF object) throws CommonException;

	/**
	 * 根据ID,得到单个对象
	 * 
	 * @param clazz
	 *            类名
	 * @param id
	 *            条件ID
	 * @return
	 */
	ANMF load(Class<ANMF> clazz, int id) throws CommonException;

	/**
	 * 分页带查询条件并带排序条件
	 * 
	 * @param clazz
	 *            类名
	 * @param orderTerm
	 *            排序条件
	 * @param where
	 *            查询条件
	 * @param pag
	 *            分页对象
	 * @return
	 * @throws CommonException
	 */
	List<ANMF> load(Class<ANMF> clazz, List<OrderTerm> orderTerm,
			List<Condition> where, Pagination pag) throws CommonException;
}
