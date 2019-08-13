package com.sendi.signalling.dao;

import com.sendi.signalling.entity.ErrorMarkMobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @Auther: wj
 * @Date: 2019/7/13 16:17
 * @Description:
 */

@Repository
public interface ErrorMarkMobileDao extends JpaRepository<ErrorMarkMobile, Long> {

    @Query(nativeQuery = true, value = "SELECT count(id) FROM gray_to_white_list  WHERE number=:caller")
    Integer queryOneByPhone(@Param("caller") String caller);
}
