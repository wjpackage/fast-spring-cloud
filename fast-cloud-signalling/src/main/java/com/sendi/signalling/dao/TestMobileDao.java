package com.sendi.signalling.dao;

import com.sendi.signalling.entity.TestMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @Auther: wj
 * @Date: 2019/7/13 16:17
 * @Description:
 */

@Repository
public interface TestMobileDao extends JpaRepository<TestMobile, String> {

}
