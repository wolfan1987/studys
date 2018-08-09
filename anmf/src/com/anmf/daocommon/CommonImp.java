package com.anmf.daocommon;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.anmf.exception.CommonException;
import com.anmf.resource.HibernateSessionFactory;
/**
 * 此类用于对数据库的操作进行封装
 * @author Administrator
 *
 * @param <ANMF>
 */
public class CommonImp<ANMF> implements ICommon<ANMF> {

	private static Logger logger = Logger.getRootLogger();

	/**
	 * 查询对象
	 */
	private QuerySelf<ANMF> query = new QuerySelf<ANMF>();

	/**
	 * 插入新记录
	 * 
	 * @param object
	 *            需插入的对象
	 * @return
	 */
	public boolean insert(ANMF object) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.save(object);
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 修改记录
	 * 
	 * @param object
	 *            需修改的对象
	 * @return
	 */
	public boolean update(ANMF object) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.update(object);
			return true;
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 删除记录
	 * 
	 * @param object
	 *            需删除的对象
	 * @return
	 */
	public boolean delete(ANMF object) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			session.delete(object);
			return true;
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 根据ID,得到单个对象
	 * 
	 * @param clazz
	 *            类名
	 * @param id
	 *            条件ID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ANMF load(Class<ANMF> clazz, int id) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			return (ANMF) session.load(clazz, id);
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 得到所有的对象
	 * 
	 * @param clazz
	 *            类名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> load(Class<ANMF> clazz) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			return session.createQuery("from " + clazz.getSimpleName()).list();
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 不分页带查询条件查询
	 * 
	 * @param clazz
	 *            类名
	 * @param where
	 *            查询条件
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> loadFromCondition(Class<ANMF> clazz, List<Condition> where)
			throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			String hql = query.getFullHql(clazz, where, null);
			Query qu = session.createQuery(hql);
			qu = query.dynaBindParameter(qu, where);
			return qu.list();
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 不分页带排序条件得到所有的记录
	 * 
	 * @param clazz
	 *            类名
	 * @param orderTerm
	 *            排序条件
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> loadFromOrderTerm(Class<ANMF> clazz, List<OrderTerm> orderTerm)
			throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			String hql = query.getFullHql(clazz, null, orderTerm);
			return session.createQuery(hql).list();
		} catch (Exception e1) {
			logger.warn(e1.getMessage());
			throw new CommonException(e1);
		}
	}

	/**
	 * 不分页带查询条件并带排序条件
	 * 
	 * @param clazz
	 *            类名
	 * @param orderTerm
	 *            排序条件
	 * @param where
	 *            查询条件
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> load(Class<ANMF> clazz, List<OrderTerm> orderTerm,
			List<Condition> where) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			String hql = query.getFullHql(clazz, where, orderTerm);
			Query qu = session.createQuery(hql);
			qu = query.dynaBindParameter(qu, where);
			return qu.list();
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 不带查询条件分页查询
	 * 
	 * @param clazz
	 *            类名
	 * @param pag
	 *            分页对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> load(Class clazz, Pagination pag) throws CommonException {
		try {
			String hql = query.getFullHql(clazz, null, null);
			return this.getData(clazz, hql, null, pag);
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 得到不带查询条件的记录数
	 * 
	 * @param clazz
	 *            类名
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	private int getRows(Class clazz) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			return Integer.parseInt(session.createQuery(
					"select count(*) from " + clazz.getSimpleName())
					.uniqueResult().toString());
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 带查询条件进行分页
	 * 
	 * @param clazz
	 *            类名
	 * @param pag
	 *            分页对象
	 * @param where
	 *            分页条件
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> loadFromCondition(Class<ANMF> clazz, Pagination pag,
			List<Condition> where) throws CommonException {
		try {
			String hql = query.getFullHql(clazz, where, null);
			return getData(clazz, hql, where, pag);
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	@SuppressWarnings("unchecked")
	private List<ANMF> loadFromConditionPageWhere(Class<ANMF> clazz,
			List<OrderTerm> order, List<Condition> where, Pagination pag)
			throws CommonException {
		try {
			String hql = query.getFullHql(clazz, where, order);
			return getData(clazz, hql, where, pag);
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 得到数据
	 * 
	 * @param clazz
	 * @param hql
	 * @param where
	 * @param pag
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> getData(Class<ANMF> clazz, String hql, List<Condition> where,
			Pagination pag) throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			Query qu = session.createQuery(hql);
			if (where != null) {
				qu = query.dynaBindParameter(qu, where);
			}
			List<ANMF> list = qu.setFirstResult(
					(pag.getCurPage() - 1) * pag.getPageRows()).setMaxResults(
					pag.getPageRows()).list();
			int rows = 0;
			if (where != null)
				rows = this.getRowsFromCondition(clazz, where);
			else
				rows = this.getRows(clazz);

			pag.setRowes(rows);

			pag.setPages(rows % pag.getPageRows() == 0 ? rows
					/ pag.getPageRows() : rows / pag.getPageRows() + 1);
			return list;
		} catch (CommonException ce) {
			ce.printStackTrace();
			throw new CommonException(ce);
		}
	}

	/**
	 * 得到带查询条件的记录数
	 * 
	 * @param clazz类名
	 * @param condition
	 *            查询条件
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unused")
	private int getRowsFromCondition(Class<ANMF> clazz, List<Condition> where)
			throws CommonException {
		try {
			Session session = HibernateSessionFactory.getSession();
			StringBuilder w = new StringBuilder();
			w.append("select count(*) ").append(
					query.getFullHql(clazz, where, null));
			Query qu = session.createQuery(w.toString());
			query.dynaBindParameter(qu, where);
			return Integer.parseInt(qu.uniqueResult().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

	/**
	 * 分页不带查询条件 , 带排序条件
	 * 
	 * @param clazz类名
	 * @param orderTerm排序条件
	 * @param pag
	 *            分页对象
	 * @return
	 * @throws CommonException
	 */
	@SuppressWarnings("unchecked")
	private List<ANMF> loadFromOrderTerm(Class<ANMF> clazz, Pagination pag,
			List<OrderTerm> orderTerm) throws CommonException {
		try {
			String hql = query.getFullHql(clazz, null, orderTerm);
			return this.getData(clazz, hql, null, pag);
		} catch (Exception e1) {
			logger.warn(e1);
			throw new CommonException(e1);
		}
	}

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
	public List<ANMF> load(Class<ANMF> clazz, List<OrderTerm> orderTerm,
			List<Condition> where, Pagination pag) throws CommonException {
		if (orderTerm == null && where == null && pag != null) {
			return this.load(clazz, pag);
		}
		if (orderTerm == null && where != null && pag != null) {
			return this.loadFromCondition(clazz, pag, where);
		}
		if (orderTerm == null && where != null && pag == null) {
			return this.loadFromCondition(clazz, where);
		}
		if (orderTerm != null && where == null && pag == null) {
			return this.loadFromOrderTerm(clazz, orderTerm);
		}
		if (orderTerm != null && where != null && pag == null) {
			return this.load(clazz, orderTerm, where);
		}
		if (orderTerm != null && where != null && pag != null) {
			return this
					.loadFromConditionPageWhere(clazz, orderTerm, where, pag);
		}
		if (orderTerm != null && where == null && pag != null) {
			return this.loadFromOrderTerm(clazz, pag, orderTerm);
		}
		if (orderTerm == null && where == null && pag == null) {
			return this.load(clazz);
		}
		throw new CommonException("com.anmf.daocommon.CommonImp类中出错了!!!");
	}
}
