package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/3 09:15
 * @Description:
 */
@Entity
@Table(name = "black_list")
@Data
@ToString
public class Black {
    @Id
    @GeneratedValue
    private Integer id;       //主键id
    private String number;    //号码
    private String b_number;    //黑名单号码
    private String region;
}
