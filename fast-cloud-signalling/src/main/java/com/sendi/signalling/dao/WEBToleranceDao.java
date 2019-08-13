package com.sendi.signalling.dao;

import com.sendi.signalling.entity.WEBTolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @Auther: wj
 * @Date: 2019/7/13 16:54
 * @Description:
 */
@Repository
public interface WEBToleranceDao extends JpaRepository<WEBTolerance, String> {

}
