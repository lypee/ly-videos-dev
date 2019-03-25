package com.ly.service.impl;

import com.ly.common.org.n3r.idworker.Sid;
import com.ly.mapper.UsersFansMapper;
import com.ly.mapper.UsersLikeVideosMapper;
import com.ly.mapper.UsersMapper;
import com.ly.mapper.UsersReportMapper;
import com.ly.pojo.Users;
import com.ly.pojo.UsersFans;
import com.ly.pojo.UsersLikeVideos;
import com.ly.pojo.UsersReport;
import com.ly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UsersMapper usersMapper ;
    @Autowired
    private Sid sid ;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper ;
    @Autowired
    private UsersFansMapper usersFansMapper ;
    @Autowired
    private UsersReportMapper usersReportMapper ;


    //定义SUPPORTS级别的事物
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users() ;
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        return result == null ? false : true ;
    }
    //新增 修改 删除 定义REQUIRED级别事务
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users users) {
        String userId = sid.nextShort() ;
        users.setId(userId);
        usersMapper.insert(users) ;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users queryUserForLogin(String username, String password) {
//        Example userExample = new Example(Users.class);
//        Criteria criteria = userExample.createCriteria();
//        criteria.andEqualTo("id", userId);
//        Users user = userMapper.selectOneByExample(userExample);
//        return user;
        Users users = new Users() ;
        users.setPassword(password);
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        if(result == null){
            return null ;
        }else {
            return result ;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users users) {
        Example userExample = new Example(Users.class) ;
        Criteria criteria = userExample.createCriteria() ;
        criteria.andEqualTo("id",users.getId());
        //更新用户信息
        usersMapper.updateByExampleSelective(users,userExample);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Example userExample=  new Example(Users.class) ;
        Criteria criteria = userExample.createCriteria() ;
        criteria.andEqualTo("id",userId) ;
//        Users users = usersMapper.selectOneByExample(userExample) ;
//        Users users = new Users() ;
//        users.setUsername(userId);
        List<Users> resultList = usersMapper.selectByExample(userExample) ;
        Users users = resultList.get(0) ;
        return users ;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId, String videoId) {
        Example example = new Example(UsersLikeVideos.class) ;
        Criteria criteria = example.createCriteria() ;
        criteria.andEqualTo("userId",userId) ;
        criteria.andEqualTo("videoId",videoId) ;
        //返回List
        List<UsersLikeVideos> usersLikeVideosList = usersLikeVideosMapper.selectByExample(example) ;
        return (!usersLikeVideosList.isEmpty() && usersLikeVideosList.size()>0)? true : false   ;
    }

    /**
     * 用户和粉丝关系的方法
     */
    public void saveUserFanRelation(String userId , String fanId){
        //关系索引键
        String relId = sid.nextShort() ;
        UsersFans usersFans = new UsersFans() ;
        usersFans.setId(relId);
        usersFans.setUserId(userId);
        usersFans.setFanId(fanId);

        usersFansMapper.insert(usersFans) ;
        //增加用户粉丝数和 粉丝的关注数;
        usersMapper.addFansCount(userId);
        usersMapper.addFollersCount(fanId);
    }

    @Override
    public void deleteUserFanRelation(String userId, String fanId) {
        Example example = new Example(UsersFans.class) ;
        Criteria criteria = example.createCriteria() ;

        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId) ;


        usersFansMapper.deleteByExample(example) ;

        usersMapper.reduceFansCount(userId);
        usersMapper.reduceFollersCount(userId);

    }

    /**
     * 查询是否有粉丝关系
     * @param userId
     * @param fanId
     * @return
     */
    @Override
    public boolean queryIfFollow(String userId, String fanId) {
        Example example = new Example(UsersFans.class) ;
        Criteria criteria = example.createCriteria() ;

        criteria.andEqualTo("fanId",fanId) ;
        criteria.andEqualTo("userId",userId) ;

        List<UsersFans> usersFansList = usersFansMapper.selectByExample(example);
        //如果数据中没有这个数据 --> 他们不存在关系 . -->return false ;
        return (usersFansList.size() == 0 || usersFansList == null )? false : true  ;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public void reportUser(UsersReport usersReport) {
        String urid = sid.nextShort() ;
        usersReport.setId(urid);
        usersReport.setCreateDate(new Date());
        usersReportMapper.insert(usersReport);
    }
}
