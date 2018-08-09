package com.zjht.soft.merchant.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zjht.soft.merchant.dao.PayTypeDao;
import com.zjht.soft.merchant.entity.MerchantTransLog;
import com.zjht.soft.merchant.service.MerchantTransLogService;
import com.zjht.solar.commons.socket.utils.MessageLengthUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class LogisticsController {

    private static final Logger log = LoggerFactory.getLogger(LogisticsController.class);

    @Reference(version = "1.0.0")
    private MerchantTransLogService service;

    @Reference(version = "1.0.0")
    private PayTypeDao payTypeDao;
    // 查看订单物流
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public void view(HttpServletRequest request, HttpServletResponse response) {
        String filename = "query.txt";
        List<MerchantTransLog> all = service.findAll();
        writeFile(response, filename, all, 1);
        log.trace("query.txt");
    }


    @RequestMapping(value = "revoke", method = RequestMethod.GET)
    public void revoke(HttpServletRequest request, HttpServletResponse response) {
        String filename = "revoke.txt";
        List<MerchantTransLog> all = service.findAll();
        writeFile(response, filename, all, 2);
        log.trace("revoke.txt");
    }

    private void writeFile(
        HttpServletResponse response, String filename, List<MerchantTransLog> all, int type) {
        try {
            //设置Content-Disposition
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            OutputStream out = response.getOutputStream();
            for (MerchantTransLog log : all) {
                if (StringUtils.isEmpty(log.getReqData()) ||
                    StringUtils.isEmpty(log.getResData())) {
                    continue;
                }
                byte[] jsonBytes = new byte[0];
                if (type == 1) {
                    jsonBytes = log.getReqData().getBytes("UTF-8");
                } else if (type == 2) {
                    jsonBytes = log.getResData().getBytes("UTF-8");
                }
                byte[] sendDataLen = MessageLengthUtil.toAsicii(jsonBytes.length);
                byte[] payRequest = ArrayUtils.addAll(sendDataLen, jsonBytes);
                String payhex = this.printHexString(payRequest);
                out.write(payhex.getBytes());
            }
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String printHexString(byte[] b) {
        StringBuffer temp = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            temp.append(hex);
        }
//        System.out.println(temp);
        return temp.toString() + "\n";
    }
}
