package com.sendi.signalling.handler.impl;

import com.sendi.signalling.annotation.SignallingMessage;
import com.sendi.signalling.contant.SignallingConstant;
import com.sendi.signalling.dao.*;
import com.sendi.signalling.entity.*;
import com.sendi.signalling.feign.BestToneFeignClient;
import com.sendi.signalling.feign.GrayNumberInfo;
import com.sendi.signalling.handler.IHandler;
import com.sendi.signalling.service.EMailService;
import com.sendi.signalling.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: wj
 * @Date: 2019/7/13 16:27
 * @Description:
 */
@Slf4j
@SignallingMessage("200")
@Component
public class BusinessHandler implements IHandler {

    /**
     * 返回的响应码
     */
    private final static String RESPONSE_CODE = "201";

    private static Map<Integer, Integer> map = new HashMap<>();

    private BusinessHandler() {
        map.put(1, 20);
        map.put(2, 15);
        map.put(3, 10);
    }

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WhiteListDao whiteListDao;

    @Resource
    private BlackListDao blackListDao;

    @Resource
    private DNCToleranceDao dncToleranceDao;

    @Resource
    private MainLibDataDao mainLibDataDao;

    @Resource
    private ErrorMarkMobileDao errorMarkMobileDao;

    @Resource
    private WEBToleranceDao webToleranceDao;

    @Resource
    private BestToneFeignClient bestToneFeignClient;

    @Resource
    private TestMobileDao testMobileDao;

    @Resource
    private UserInfoDao userInfoDao;

    @Resource
    private EMailService eMailService;

    @Override
    public String run(String msg) {
        String[] strings = msg.split(",");
        String callId = strings[1];
        String caller = strings[2];
        String called = strings[3];

        //返回信息结构 201,callId,ret(放通0，拦截1，语音2),reason(原因),oper(后续操作上传dnc),region
        String region = redisUtil.hget("h_code", called.substring(0, 7));

        //未知用户 系统库不存在的被叫用户(放通)
        UserInfo userInfo = userInfoDao.findOne(called);
        if (userInfo == null || StringUtils.isBlank(userInfo.getNumber())) {
            return getQueryResult(callId,"0","26","0",region);
        }

        //白名单判断 主叫存在于被叫用户白名单(放通)
        String result = doWhiteDeal(callId, called, caller, region);
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }

        //400/950/951/952号码判断 主叫属于被叫用户设置的DNC意愿库-400/950等号段(拦截)
        DNCTolerance dncTolerance = dncToleranceDao.findOne(called);
        if (caller.equals("18681555176") || caller.equals("18101377652")) {
            TestMobile testMobile = testMobileDao.findOne(caller);
            if (testMobile != null && StringUtils.isNotEmpty(testMobile.getM_number()))
                caller = testMobile.getM_number();
        }
        if ("400,950,951,952".contains(caller.substring(0, 3))) {
            result = doCodeRangeDeal(callId, caller, dncTolerance);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        }

        //黑名单判断 主叫属于被叫用户黑名单(拦截)
        result = doBlackDeal(callId, called, caller, region);
        if (StringUtils.isNotEmpty(result)) {
            return result;
        }

        //主体库，营销电话判断  被叫用户意愿库-营销电话是否拦截且主叫存在于DNC主体库(拦截)
        if (dncTolerance != null && dncTolerance.getMain_lib() == 1) {
            result = doMainLibDeal(callId, caller, dncTolerance);
            if (StringUtils.isNotEmpty(result)) {
                return result;
            }
        }

        //误标记号码 主叫是否存在于误标记号码(放通)
        Integer errorMarkMobile = errorMarkMobileDao.queryOneByPhone(caller);
        if (errorMarkMobile >= 1) {
            return getQueryResult(callId,"0","7","0",region);
        }

        //通过号百接口查询主叫号码信息，与意愿库进行判断  WEB意愿库被标记且大于被叫用户设置的阈值 (拦截)
        result = doBestToneDeal(called, caller, callId, region);
        return result;
    }

    private String doWhiteDeal(String callId, String called, String caller, String region) {
        Integer i = whiteListDao.queryOneByCalledAndCaller(called, caller);
        if (i >= 1) {
            return getQueryResult(callId,"0","0","0",region);
        }
        return "";
    }

    private String doBestToneDeal(String called, String caller, String callId, String region) {
        WEBTolerance webTolerance = webToleranceDao.findOne(called);
        try {
            GrayNumberInfo grayNumberInfo = bestToneFeignClient.query(caller);

            if (caller.equals("15322075691")) {
                grayNumberInfo = new GrayNumberInfo();
                grayNumberInfo.setMarkNameDesc("业务推销");
                grayNumberInfo.setMarkType("15");
                grayNumberInfo.setTel("15322075691");
            } else if (caller.equals("17338146192")) {
                grayNumberInfo = new GrayNumberInfo();
                grayNumberInfo.setMarkNameDesc("保险理财");
                grayNumberInfo.setMarkType("14");
                grayNumberInfo.setTel("17338146192");
            } else if (caller.equals("15313736712")) {
                grayNumberInfo = new GrayNumberInfo();
                grayNumberInfo.setMarkNameDesc("诈骗电话");
                grayNumberInfo.setMarkType("19");
                grayNumberInfo.setTel("15313736712");
            }else if (caller.equals("15322075329")) {
                grayNumberInfo = new GrayNumberInfo();
                grayNumberInfo.setMarkNameDesc("高频呼出");
                grayNumberInfo.setMarkType(null);
                grayNumberInfo.setTel("15322075329");
            }
            log.info("号百的号码信息 {}", grayNumberInfo);
            if (grayNumberInfo == null || StringUtils.isBlank(grayNumberInfo.getTel())) {
                return getQueryResult(callId,"0","8","0",region);
            } else {
                switch (grayNumberInfo.getMarkNameDesc()) {
                    case "高频呼出":
                        if (webTolerance.getHigh_call() >= 0) {
                            return getQueryResult(callId,"1","9","0",region);
                        } else {
                            return getQueryResult(callId,"0","10","0",region);
                        }
                    case "诈骗电话":
                        if (webTolerance.getPhone_scam() > 0) {
                            if (map.get(webTolerance.getPhone_scam()) <= Integer.parseInt(grayNumberInfo.getMarkType())) {
                                return getQueryResult(callId,"1","11","0",region);
                            } else {
                                return getQueryResult(callId,"0","12","0",region);
                            }
                        } else {
                            return getQueryResult(callId,"0","13","0",region);
                        }
                    case "骚扰电话":
                        if (webTolerance.getCrank_call() > 0) {
                            if (map.get(webTolerance.getCrank_call()) <= Integer.parseInt(grayNumberInfo.getMarkType())) {
                                return getQueryResult(callId,"1","14","0",region);
                            } else {
                                return getQueryResult(callId,"0","15","0",region);
                            }
                        } else {
                            return getQueryResult(callId,"0","16","0",region);
                        }
                    case "业务推销":
                        if (webTolerance.getBusiness_promotion() > 0) {
                            if (map.get(webTolerance.getBusiness_promotion()) <= Integer.parseInt(grayNumberInfo.getMarkType())) {
                                return getQueryResult(callId,"1","17","0",region);
                            } else {
                                return getQueryResult(callId,"0","18","0",region);
                            }
                        } else {
                            return getQueryResult(callId,"0","19","0",region);
                        }
                    case "房产中介":
                        if (webTolerance.getReal_estate() > 0) {
                            if (map.get(webTolerance.getReal_estate()) <= Integer.parseInt(grayNumberInfo.getMarkType())) {
                                return getQueryResult(callId,"1","20","0",region);
                            } else {
                                return getQueryResult(callId,"0","21","0",region);
                            }
                        } else {
                            return getQueryResult(callId,"0","22","0",region);
                        }
                    case "保险理财":
                        if (webTolerance.getInsurance_financing() > 0) {
                            if (map.get(webTolerance.getInsurance_financing()) <= Integer.parseInt(grayNumberInfo.getMarkType())) {
                                return getQueryResult(callId,"1","23","0",region);
                            } else {
                                return getQueryResult(callId,"0","24","0",region);
                            }
                        } else {
                            return getQueryResult(callId,"0","25","0",region);
                        }
                }
            }
        } catch (Exception e) {
            eMailService.send("besttoneQuery","信令Socket通道号百查询异常",e.getMessage());
            log.warn("", e);
            return getQueryResult(callId,"0","8","0",region);
        }
        return getQueryResult(callId,"0","8","0",region);
    }

    private String doMainLibDeal(String callId, String caller, DNCTolerance dncTolerance) {
        Integer count = mainLibDataDao.queryOneByPhone(caller);
        if (count >= 1) {
            return getQueryResult(callId,"1","6","1",dncTolerance.getRegion());
        }
        return "";
    }

    private String doBlackDeal(String callId, String called, String caller, String region) {
        Integer black = blackListDao.queryOneByCalledAndCaller(called, caller);
        Integer count = mainLibDataDao.queryOneByPhone(caller);
        if (black >= 1) {
            if (count >= 1) {
                return getQueryResult(callId,"1","5","1",region);
            }
            return getQueryResult(callId,"1","5","0",region);
        }
        return "";
    }

    /**
     * 400/950/951/952号码判断 主叫属于被叫用户设置的DNC意愿库-400/950等号段(拦截)
     */
    private String doCodeRangeDeal(String callId, String caller, DNCTolerance dncTolerance) {
        Integer count = mainLibDataDao.queryOneByPhone(caller);
        String segment = caller.substring(0, 3);
        String region = dncTolerance.getRegion();
        if (segment.equals("400") && dncTolerance.getCode_range().contains("400")) {
            if (count >= 1)
                return getQueryResult(callId,"1","1","1",region);
            return getQueryResult(callId,"1","1","0",region);
        }
        if (segment.equals("950") && dncTolerance.getCode_range().contains("950")) {
            if (count >= 1)
                return getQueryResult(callId,"1","2","1",region);

            return getQueryResult(callId,"1","2","0",region);
        }
        if (segment.equals("951") && dncTolerance.getCode_range().contains("951")) {
            if (count >= 1)
                return getQueryResult(callId,"1","3","1",region);
            return getQueryResult(callId,"1","3","0",region);
        }
        if (segment.equals("952") && dncTolerance.getCode_range().contains("952")) {
            if (count >= 1)
                return getQueryResult(callId,"1","4","1",region);
            return getQueryResult(callId,"1","4","0",region);
        }
        return "";
    }

    /**
     * 返回给信令处理侧的消息
     * @param callId 消息唯一标识符
     * @param state 查询结果 0为放通 1为拦截 2为语音验证
     * @param reason 查询结果原因
     * 白名单     0   	放通
     * 400	      1	    拦截
     * 950 	      2	    拦截
     * 951	      3	    拦截
     * 952	      4	    拦截
     * 黑名单  	  5     拦截
     * 主体库     6	    拦截
     * 误标记  	  7	    放通
     * 非标记号码  8	放通
     * 高频呼出   9  	拦截
     * 高频呼出  10 	未设容忍度放通
     * 诈骗电话  11 	拦截
     * 诈骗电话  12 	低于设容忍度放通
     * 诈骗电话  13 	未设容忍度放通
     * 骚扰电话  14 	拦截
     * 骚扰电话  15 	低于设容忍度
     * 骚扰电话  16 	未设容忍度
     * 业务推销  17 	拦截
     * 业务推销  18 	低于设容忍度
     * 业务推销  19 	未设容忍度
     * 房产中介  20 	拦截
     * 房产中介  21 	低于设容忍度
     * 房产中介  22 	未设容忍度
     * 保险理财  23 	拦截
     * 保险理财  24 	低于设容忍度
     * 保险理财  25 	未设容忍度
     * 被叫号码为空  26 放通
     * @param operate 后续操作指示
     * @param region 号码的区域字段，后续做权限控制使用
     * @return 按照约定拼接的最终结果字符串
     */
    private String getQueryResult(String callId,String state,String reason,String operate,String region){
        return RESPONSE_CODE + SignallingConstant.FIELDS_SEPARATOR +
                callId + SignallingConstant.FIELDS_SEPARATOR +
                state + SignallingConstant.FIELDS_SEPARATOR +
                reason + SignallingConstant.FIELDS_SEPARATOR +
                operate + SignallingConstant.FIELDS_SEPARATOR +
                region + SignallingConstant.MSG_END;
    }

}
