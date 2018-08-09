package com.zjht.cardsys.orgquery.services;

import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.exceptions.ServiceException;

import java.util.List;

/**
 * Created by zjhtadmin on 2018/1/8.
 */
public interface CardAcceptOrgService {

    AcceptOrgRelation   add(AcceptOrgRelation  newObj) throws ServiceException;


    AcceptOrgRelation  queryByPan(String pan)throws ServiceException;

}
