package com.sendi.signalling.dao;

import com.sendi.signalling.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @Auther: wj
 * @Date: 2019/7/13 15:37
 * @Description:
 */
@Repository
public interface UserInfoDao extends JpaRepository<UserInfo, String> {

}
