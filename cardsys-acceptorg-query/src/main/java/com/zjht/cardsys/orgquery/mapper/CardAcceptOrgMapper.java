package com.zjht.cardsys.orgquery.mapper;

import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by zjhtadmin on 2018/1/8.
 */
public interface CardAcceptOrgMapper {


     AcceptOrgRelation queryByPan(@Param("pan")  String pan) ;


     void add(AcceptOrgRelation newObj);

}
