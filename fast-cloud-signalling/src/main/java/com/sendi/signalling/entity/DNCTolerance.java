package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/5 13:51
 * @Description:
 */
@Entity
@Table(name = "number_dnc_tolerance")
@Data
@ToString
public class DNCTolerance {

    @Id
    private String number;       //主键
    private Integer main_lib;    //主体库
    private String code_range; //号段模糊拦截
    private String region;
    private String capturetime;    //入库时间

}
