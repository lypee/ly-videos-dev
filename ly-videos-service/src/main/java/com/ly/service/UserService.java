package com.ly.service;

import com.ly.pojo.Users;
import com.ly.pojo.UsersReport;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    /**
     * 用户是否存在
     */
    public boolean queryUsernameIsExist(String username) ;

    /**
     * 用户注册
     */
    public void saveUser(Users users) ;

    /**
     * 用户登陆 ,根据用户名和密码 查询用户输入的是否正确
     */

    public Users queryUserForLogin(String username , String password);

    /**
     * 修改用户信息
     */
    public void updateUserInfo(Users users) ;

    /**
     * 查询用户信息
     */
    public Users queryUserInfo(String userId) ;
    /**
     * 查询用户是否喜欢该视频
     */
    public boolean isUserLikeVideo(String userId, String videoId);
    /**
     * 增加用户和粉丝的关系
     */
    public void saveUserFanRelation(String userId , String fanId) ;
    /**
     * 删除用户和粉丝的关系
     */
    public void deleteUserFanRelation(String userId, String fanId);
    /**
     * @Description: 查询用户是否关注
     */
    public boolean queryIfFollow(String userId, String fanId);
    /**
     * 举报用户
     */
    public void reportUser(UsersReport usersReport) ;
}
