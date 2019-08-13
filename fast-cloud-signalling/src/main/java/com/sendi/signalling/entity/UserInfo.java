package com.sendi.signalling.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Auther: wj
 * @Date: 2019/7/9 14:16
 * @Description:
 */
@Entity
@Table(name = "user_info")
@Data
@ToString
public class UserInfo {
    @Id
    private String number;       //主键 号码
    private Integer status=1;      //谢电业务订阅状态 1订阅 0未订阅
    private Integer sign_flag;    //语音功能开通状态 0未开通 1开通
    private String region;

}
