package com.sendi.signalling.dao;

import com.sendi.signalling.entity.Black;
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
public interface BlackListDao extends JpaRepository<Black, Long> {
    @Query(nativeQuery = true, value = "SELECT count(b_number) FROM black_list  WHERE number=:called  and b_number =:caller")
    Integer queryOneByCalledAndCaller(@Param("called") String called, @Param("caller") String caller);

}
