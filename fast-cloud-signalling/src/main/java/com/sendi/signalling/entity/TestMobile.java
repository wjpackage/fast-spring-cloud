package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/3 09:15
 * @Description:
 */
@Entity
@Table(name = "test_list")
@Data
@ToString
public class TestMobile {
    @Id
    private String number;    //号码
    private String m_number;    //模拟号码
}
