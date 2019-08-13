package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/13 17:49
 * @Description:
 */
@Entity
@Table(name = "gray_to_white_list")
@Data
@ToString
public class ErrorMarkMobile {
    @Id
    @GeneratedValue
    private Integer id;       //主键id
    private String number;    //号码
    private String region;

}
