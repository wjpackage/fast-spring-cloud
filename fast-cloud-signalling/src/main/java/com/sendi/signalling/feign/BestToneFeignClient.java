package com.sendi.signalling.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: wj
 * @Date: 2019/7/13 18:57
 * @Description:
 */
@FeignClient(value = "xjld-cloud-besttone")
public interface BestToneFeignClient {

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    GrayNumberInfo query(@RequestBody String number) throws Exception;


}
