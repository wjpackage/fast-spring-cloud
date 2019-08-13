package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/13 17:03
 * @Description:
 */
@Entity
@Table(name = "main_lib_data")
@Data
@ToString
public class MainLibData {
    @Id
    @GeneratedValue
    private Integer id;       //主键id
    private String phoneNumList;    //号码
}
