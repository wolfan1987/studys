package com.zjht.cardsys.orgquery.services;

import com.zjht.cardsys.AorServer;
import com.zjht.cardsys.orgquery.exceptions.ServiceException;
import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Date;

/**
 * Created by zjhtadmin on 2018/1/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AorServer.class)
@WebAppConfiguration
public class AcceptOrgServiceTest {

    private final Logger logger = LoggerFactory.getLogger(AcceptOrgServiceTest.class);
    @Autowired
    CardAcceptOrgService   cardAcceptOrgService;

    @Test
    public  void  testAdd(){
//        String  pan = "6627581111112222222";
//        String  issuId = "htb-1234";
//        AcceptOrgRelation  newObj = new AcceptOrgRelation();
//        newObj.setPan(pan);
//        newObj.setIssuId(issuId);
//        newObj.setCreateTime(new Date());
//        try {
//            AcceptOrgRelation  result=cardAcceptOrgService.add(newObj);
//            Assert.assertTrue("返回Result不为空",result!=null);
//            logger.info("result={}",result.toString());
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void  testQueryPan(){
//        String  pan = "6627581111112222222";
//        try {
//            AcceptOrgRelation  result = cardAcceptOrgService.queryByPan(pan);
//            Assert.assertTrue("返回结果不为空",result!=null);
//            logger.info("查询结果.content={}",result.toString());
//        } catch (ServiceException e) {
//            e.printStackTrace();
//        }
    }

}
