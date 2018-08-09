package com.zjht.cardsys.orgquery.services;

/**
 * Created by zjhtadmin on 2018/1/10.
 */
public class TestData {

    public static void main(String[] args){
        String temp = "";
        for(int i=1; i<1000;i++){
           temp = "{\"pan\":\"662758211111222"+i+"\",\"issuId\":\"htk-4567\"}\n";
            System.out.print(temp);
        }
    }
}
