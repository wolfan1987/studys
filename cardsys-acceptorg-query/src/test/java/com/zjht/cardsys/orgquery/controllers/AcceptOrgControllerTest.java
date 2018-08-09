package com.zjht.cardsys.orgquery.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zjht.cardsys.AorServer;
import com.zjht.cardsys.orgquery.services.AcceptOrgServiceTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by zjhtadmin on 2018/1/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AorServer.class)
@WebAppConfiguration
public class AcceptOrgControllerTest {

    private final Logger logger = LoggerFactory.getLogger(AcceptOrgControllerTest.class);

    MockMvc mvc;

    @Autowired
    WebApplicationContext webApplicationConnect;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
    }



    @Test
    public  void  testAdd(){
//        String uri = "/cardorgctrl/aorbiz/add";
//        MvcResult mvcResult = null;
//        try {
//
//            String  reqJson =   "{\"pan\":\"6627581111112222224\",\"issuId\":\"htk-4567\"}\n";
//
//            mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(reqJson).accept(MediaType.APPLICATION_JSON_UTF8))
//                    .andReturn();
//            int status = mvcResult.getResponse().getStatus();
//            Assert.assertTrue("200",status==200);
//            String content = mvcResult.getResponse().getContentAsString();
//            logger.info("content={}",content);
//            Assert.assertTrue("00",content.indexOf("00")>=0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    @Test
    public void testQueryByPan(){
        String uri = "/cardorgctrl/aorbiz/query";
        MvcResult mvcResult = null;
        try {
            String  reqJson =   "{\"pan\":\"6627581111112222224\"}\n";
            mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.APPLICATION_JSON_UTF8).content(reqJson).accept(MediaType.APPLICATION_JSON_UTF8))
                    .andReturn();
            int status = mvcResult.getResponse().getStatus();
            String content = mvcResult.getResponse().getContentAsString();
            logger.info("content={}",content);
            Assert.assertTrue("200",status==200);

            logger.info("queryPan.content={}",content);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
