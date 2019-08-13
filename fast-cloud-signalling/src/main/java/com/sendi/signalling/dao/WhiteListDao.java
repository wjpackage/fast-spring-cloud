package com.sendi.signalling.dao;

import com.sendi.signalling.entity.White;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Auther: wj
 * @Date: 2019/7/13 16:17
 * @Description:
 */

@Repository
public interface WhiteListDao extends JpaRepository<White, Long> {

    @Query(nativeQuery = true, value = "SELECT count(w_number) FROM white_list WHERE number=:number  and  w_number =:w_number")
    Integer queryOneByCalledAndCaller(@Param("number") String number, @Param("w_number") String w_number);
}
