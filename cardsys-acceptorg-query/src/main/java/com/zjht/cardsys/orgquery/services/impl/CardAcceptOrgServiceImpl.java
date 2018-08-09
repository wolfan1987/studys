package com.zjht.cardsys.orgquery.services.impl;


import com.zjht.cardsys.orgquery.dao.CardAcceptOrgWriteDao;
import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.exceptions.DaoException;
import com.zjht.cardsys.orgquery.exceptions.ServiceException;
import com.zjht.cardsys.orgquery.services.CardAcceptOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zjhtadmin on 2018/1/8.
 */
@Service
public class CardAcceptOrgServiceImpl implements CardAcceptOrgService {

    @Autowired
    private CardAcceptOrgWriteDao cardAcceptOrgWriteDao;

    @Override
    public AcceptOrgRelation add(AcceptOrgRelation newObj) throws ServiceException{
        try {
           newObj =  cardAcceptOrgWriteDao.add(newObj);
        } catch (DaoException de) {
            throw new ServiceException(de,de.getMessage());
        }
        return newObj;
    }



    @Override
    public AcceptOrgRelation  queryByPan(String pan)throws ServiceException {
        try {
           return cardAcceptOrgWriteDao.queryByPan(pan);
        } catch (DaoException de) {
            throw new ServiceException(de,de.getMessage());
        }
    }
}
