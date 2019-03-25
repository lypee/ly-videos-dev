package com.ly.mapper;

import com.ly.common.utils.MyMapper;
import com.ly.pojo.Users;

public interface UsersMapper extends MyMapper<Users> {
    /**
     *
     */
    public Users queryPasswordByUsername(String username) ;

    /**
     * @Description:
     */
    public void addReceiveLikeCount(String userId);

    /**
     * @Description:
     */
    public void reduceReceiveLikeCount(String userId);

    /**
     * @Description:
     */
    public void addFansCount(String userId);

    /**
     * @Description:
     */
    public void addFollersCount(String userId);

    /**
     * @Description:
     */
    public void reduceFansCount(String userId);

    /**
     * @Description:
     */
    public void reduceFollersCount(String userId);
    /**
     *
     */
}