package com.zjht.cardsys.orgquery.dao.impl;

import com.zjht.cardsys.orgquery.dao.CardAcceptOrgWriteDao;
import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.exceptions.DaoException;
import com.zjht.cardsys.orgquery.mapper.CardAcceptOrgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjhtadmin on 2018/1/8.
 */
@Repository
public class CardAcceptOrgDaoImpl implements CardAcceptOrgWriteDao {

    @Autowired
    private CardAcceptOrgMapper  cardAcceptOrgMapper;

    @Override
    public AcceptOrgRelation add(AcceptOrgRelation newObj) throws DaoException{
        try{
            cardAcceptOrgMapper.add(newObj);
            newObj = cardAcceptOrgMapper.queryByPan(newObj.getPan().trim());
        }catch(Exception ex){
           throw new DaoException(ex,ex.getMessage());
        }
        return newObj;
    }

    @Override
    public AcceptOrgRelation queryByPan(String pan) throws DaoException {
        try{
            return cardAcceptOrgMapper.queryByPan(pan);
        }catch(Exception ex){
            throw new DaoException(ex,ex.getMessage());
        }

    }
}
