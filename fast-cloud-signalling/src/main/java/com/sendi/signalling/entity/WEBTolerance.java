package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/5 13:52
 * @Description:
 */
@Entity
@Table(name = "number_mark_tolerance")
@Data
@ToString
public class WEBTolerance {

    @Id
    private String number;       //主键
    private Integer STATUS=1;      //起停状态
    private int high_call;    //高频呼出
    private int phone_scam;    //诈骗电话
    private int crank_call;    //骚扰电话
    private int business_promotion;    //业务推销
    private int real_estate;    //房产中介
    private int insurance_financing;    //保险理财
    private String region;
    private String capturetime;    //入库时间

}
