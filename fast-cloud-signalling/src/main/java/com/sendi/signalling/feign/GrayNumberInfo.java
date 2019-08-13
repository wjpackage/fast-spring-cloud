package com.sendi.signalling.feign;

import lombok.Data;
import lombok.ToString;

/**
 * 灰名单信息,未经过号百白名单过滤
 */
@Data
@ToString
public class GrayNumberInfo {

    /** 号码 */
    private String tel;
    /** 标记类型名称 */
    private String markNameDesc;
    /** 标记次数 */
    private String markType;

}
