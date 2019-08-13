package com.sendi.signalling.dao;

import com.sendi.signalling.entity.DNCTolerance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * @Auther: wj
 * @Date: 2019/7/13 16:52
 * @Description:
 */
@Repository
public interface DNCToleranceDao extends JpaRepository<DNCTolerance, String> {

}
