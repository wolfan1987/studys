package com.zjht.cardsys.orgquery.controllers;

import com.zjht.cardsys.orgquery.dao.CardAcceptOrgDao;
import com.zjht.cardsys.orgquery.dto.SimpleResponse;
import com.zjht.cardsys.orgquery.exceptions.DaoException;
import com.zjht.cardsys.orgquery.exceptions.ServiceException;
import com.zjht.cardsys.orgquery.model.AcceptOrgRelation;
import com.zjht.cardsys.orgquery.services.CardAcceptOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by AndrewLiu on 2018/1/8.
 * 卡系统拆分： 卡号与发卡机构关系路由服务接口
 * addAcceptOrgRel： 添加卡号与发卡机构关系
 * queryAcceptOrgRel： 查询卡号的发卡机构
 */
@RestController
@RequestMapping("/cardorgctrl")
public class AcceptOrgRelationController {

    private final Logger logger = LoggerFactory.getLogger(AcceptOrgRelationController.class);

    /**
     * 添加卡号与发卡机构关系服务
     */
    @Autowired
    private CardAcceptOrgService cardAcceptOrgService;

    /**
     * 查询卡号的发卡机构Dao,次处为只读
     */
    @Autowired
    private CardAcceptOrgDao cardAcceptOrgDao;

    /**
     *添加新的卡号与发卡机构关系
     * @param newPanIssuId  新的卡号与发卡机构关系对象
     * @return
     * 00=添加成功，并返回Id,pan,issuId
     * 01=添加失败，并返回具体失败描述
     */
    @PostMapping("/aorbiz/add")
    public SimpleResponse addAcceptOrgRel(@RequestBody AcceptOrgRelation newPanIssuId) {
        SimpleResponse simpleResponse = new SimpleResponse("00", "添加卡号发卡机构关系成功");
        simpleResponse = verifyPanIssuId(newPanIssuId, simpleResponse);
        if (simpleResponse.getRespCode().trim().equalsIgnoreCase("01")) {
            return simpleResponse;
        }
        String   toStr =  newPanIssuId.toString();
        logger.info("--新的卡号与受理机构关系对象={}",toStr);
        try {
            newPanIssuId.setCreateTime(new Date());
            newPanIssuId = cardAcceptOrgService.add(newPanIssuId);
            if (newPanIssuId != null) {
                simpleResponse = getQueryResponse(simpleResponse, newPanIssuId);
                logger.info("新增结果：{} ,{},{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg(),toStr);
                return simpleResponse;
            } else {
                throw new Exception("卡号与发卡机构查询服务器异常。");
            }
        } catch (ServiceException e) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("添加pan=" + newPanIssuId.getPan() + ",issuId=" + newPanIssuId.getIssuId() + "失败," + e.getMessage());
            logger.error("service错误码：{},service错误描述：{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg());
            e.printStackTrace();
        } catch (Exception ex) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("添加pan=" + newPanIssuId.getPan() + ",issuId=" + newPanIssuId.getIssuId() + "失败," + ex.getMessage());
            logger.error("未知异常错误码：{},{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg());
            ex.printStackTrace();
        }
        logger.info("新增结果：{} ,{} ,{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg(),toStr);
        return simpleResponse;
    }

    /**
     * @param queryPanIssuId
     * @return
     *
     * 00=查询成功，并返回具体结果
     * 01=查询失败，返回错误说明
     * 02=查询成功，但无匹配结果
     *
     */
    @PostMapping(value = "/aorbiz/query")
    public SimpleResponse queryAcceptOrgRel(@RequestBody AcceptOrgRelation queryPanIssuId) {
        SimpleResponse simpleResponse = new SimpleResponse("00", "查询成功");
        String pan = queryPanIssuId.getPan();
        logger.info("-查询卡号={}", queryPanIssuId.getPan());
        if (StringUtils.isEmpty(pan) || (!StringUtils.isEmpty(pan) && pan.trim().length() > 19)) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("查询失败,请检查pan值是否为空及长度是否为小于19位，pan=" + pan);
            return simpleResponse;
        }
        try {
            AcceptOrgRelation result = cardAcceptOrgDao.queryByPan(pan);
            if (result != null) {
                simpleResponse = getQueryResponse(simpleResponse, result);
            } else {
                simpleResponse.setRespCode("02");
                simpleResponse.setRespMsg("查询成功，但无匹配结果.");
            }
        } catch (DaoException e) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("查询失败" + e.getMessage());
            logger.error("Dao错误码：{},Dao错误描述：{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg());
            e.printStackTrace();
        } catch (Exception ex) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("查询失败" + ex.getMessage());
            logger.error("未知异常错误码：{},{}",simpleResponse.getRespCode(),simpleResponse.getRespMsg());
            ex.printStackTrace();
        }
        logger.info("查询结果：{} ,{},pan={}",simpleResponse.getRespCode(),simpleResponse.getRespMsg(),pan);
        return simpleResponse;
    }

    /**
     * 查询结果与响应进行转换
     * @param simpleResponse
     * @param result
     * @return
     */
    private SimpleResponse getQueryResponse(SimpleResponse simpleResponse, AcceptOrgRelation result) {
        simpleResponse.setIssuId(result.getIssuId());
        simpleResponse.setPan(result.getPan());
        simpleResponse.setId(result.getId());
        return simpleResponse;
    }

    private SimpleResponse verifyPanIssuId(AcceptOrgRelation newPanIssuId, SimpleResponse simpleResponse) {
        if (newPanIssuId == null) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("添加新的卡号与发卡机构关系失败，AcceptOrgRelation 对象为Null");
            return simpleResponse;
        }
        if (StringUtils.isEmpty(newPanIssuId.getPan()) || StringUtils.isEmpty(newPanIssuId.getIssuId())) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("添加新的卡号与发卡机构关系失败，PAN 或 ISSUID 不合法，请检查是否为空。");
        }
        if (newPanIssuId.getPan().trim().length() > 19 || newPanIssuId.getIssuId().trim().length() > 8) {
            simpleResponse.setRespCode("01");
            simpleResponse.setRespMsg("添加新的卡号与发卡机构关系失败，PAN 或 ISSUId 不合法，请检查PAN的长度是否大于19位，ISSUID是否的长度是否小于8位");
        }
        return simpleResponse;
    }


}
