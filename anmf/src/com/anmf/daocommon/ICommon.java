package com.anmf.daocommon;

import java.util.List;

import com.anmf.exception.CommonException;

/**
 * ANMF��� �������ݿ���
 * 
 * @author Administrator
 * 
 * @param <CRM>
 */
public interface ICommon<ANMF> {
	/**
	 * �����¼�¼
	 * 
	 * @param object
	 *            �����Ķ���
	 * @return
	 */
	boolean insert(ANMF object) throws CommonException;

	/**
	 * �޸ļ�¼
	 * 
	 * @param object
	 *            ���޸ĵĶ���
	 * @return
	 */
	boolean update(ANMF object) throws CommonException;

	/**
	 * ɾ����¼
	 * 
	 * @param object
	 *            ��ɾ���Ķ���
	 * @return
	 */
	boolean delete(ANMF object) throws CommonException;

	/**
	 * ����ID,�õ���������
	 * 
	 * @param clazz
	 *            ����
	 * @param id
	 *            ����ID
	 * @return
	 */
	ANMF load(Class<ANMF> clazz, int id) throws CommonException;

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
	List<ANMF> load(Class<ANMF> clazz, List<OrderTerm> orderTerm,
			List<Condition> where, Pagination pag) throws CommonException;
}
