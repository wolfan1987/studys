package com.anmf.daocommon;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;

import com.anmf.exception.CommonException;
import com.anmf.resource.HibernateSessionFactory;
/**
 * �������ڶ����ݿ�Ĳ������з�װ
 * @author Administrator
 *
 * @param <ANMF>
 */
public class CommonImp<ANMF> implements ICommon<ANMF> {

	private static Logger logger = Logger.getRootLogger();

	/**
	 * ��ѯ����
	 */
	private QuerySelf<ANMF> query = new QuerySelf<ANMF>();

	/**
	 * �����¼�¼
	 * 
	 * @param object
	 *            �����Ķ���
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
	 * �޸ļ�¼
	 * 
	 * @param object
	 *            ���޸ĵĶ���
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
	 * ɾ����¼
	 * 
	 * @param object
	 *            ��ɾ���Ķ���
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
	 * ����ID,�õ���������
	 * 
	 * @param clazz
	 *            ����
	 * @param id
	 *            ����ID
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
	 * �õ����еĶ���
	 * 
	 * @param clazz
	 *            ����
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
	 * ����ҳ����ѯ������ѯ
	 * 
	 * @param clazz
	 *            ����
	 * @param where
	 *            ��ѯ����
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
	 * ����ҳ�����������õ����еļ�¼
	 * 
	 * @param clazz
	 *            ����
	 * @param orderTerm
	 *            ��������
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
	 * ����ҳ����ѯ����������������
	 * 
	 * @param clazz
	 *            ����
	 * @param orderTerm
	 *            ��������
	 * @param where
	 *            ��ѯ����
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
	 * ������ѯ������ҳ��ѯ
	 * 
	 * @param clazz
	 *            ����
	 * @param pag
	 *            ��ҳ����
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
	 * �õ�������ѯ�����ļ�¼��
	 * 
	 * @param clazz
	 *            ����
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
	 * ����ѯ�������з�ҳ
	 * 
	 * @param clazz
	 *            ����
	 * @param pag
	 *            ��ҳ����
	 * @param where
	 *            ��ҳ����
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
	 * �õ�����
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
	 * �õ�����ѯ�����ļ�¼��
	 * 
	 * @param clazz����
	 * @param condition
	 *            ��ѯ����
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
	 * ��ҳ������ѯ���� , ����������
	 * 
	 * @param clazz����
	 * @param orderTerm��������
	 * @param pag
	 *            ��ҳ����
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
	 * ��ҳ����ѯ����������������
	 * 
	 * @param clazz
	 *            ����
	 * @param orderTerm
	 *            ��������
	 * @param where
	 *            ��ѯ����
	 * @param pag
	 *            ��ҳ����
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
		throw new CommonException("com.anmf.daocommon.CommonImp���г�����!!!");
	}
}
