package com.zjht.cardsys.orgquery.dao;

import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.exceptions.DaoException;

import java.util.List;

/**
 * Created by zjhtadmin on 2018/1/8.
 */

public interface CardAcceptOrgDao {

    AcceptOrgRelation  queryByPan(String pan) throws DaoException;
}
