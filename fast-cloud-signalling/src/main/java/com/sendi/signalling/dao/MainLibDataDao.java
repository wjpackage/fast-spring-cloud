package com.sendi.signalling.dao;

import com.sendi.signalling.entity.MainLibData;
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
public interface MainLibDataDao extends JpaRepository<MainLibData, Long> {

    @Query(nativeQuery = true, value = "SELECT count(phoneNumList) FROM main_lib_data WHERE phoneNumList=:caller")
    Integer queryOneByPhone(@Param("caller") String caller);
}
