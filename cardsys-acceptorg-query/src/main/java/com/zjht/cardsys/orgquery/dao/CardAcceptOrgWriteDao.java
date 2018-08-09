package com.zjht.cardsys.orgquery.dao;

import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.exceptions.DaoException;


/**
 * Created by zjhtadmin on 2018/1/8.
 */

public interface CardAcceptOrgWriteDao  extends CardAcceptOrgDao{

    AcceptOrgRelation add(AcceptOrgRelation  newObj) throws DaoException;

}
