package com.anmf.dao;

import java.util.List;

import com.anmf.exception.DaoException;

/**
 * ���ݷ��ʲ�ӿ�
 * 
 * @author Administrator
 * 
 * @param <VO>
 */
public interface IDao<AbstractVo> {
	public boolean save(AbstractVo vo) throws DaoException;

	public boolean update(AbstractVo vo) throws DaoException;

	public boolean delete(AbstractVo vo) throws DaoException;

	public List<AbstractVo> getAll() throws DaoException;

	public AbstractVo findByPk(AbstractVo vo) throws DaoException;

}
