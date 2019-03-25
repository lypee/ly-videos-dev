package com.ly.service;

import com.ly.common.utils.PagedResult;
import com.ly.pojo.Comments;
import com.ly.pojo.Videos;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VideoService {

    /**
     * 保存视频
     */
    public String saveVideo(Videos videos) ;

    /**
     *修改视频封面
     */
    public void updateVideo(String videoId , String coverPath);

    /**
     * 查询全部视频
     * @param page 要查询的页数
     * @param pageSize 总共的页数
     * @return
     */
    public PagedResult getAllVideos(Videos videos , Integer isSaveRecoed  , Integer page, Integer pageSize) ;
    /**
     * 查询热搜词
     */
    public List<String> getHotWords();
    /**
     * 用户喜欢的Video
     */
    public void userLikeVideo(String userId , String videoId ,String videoCreaterId) ;

    /**
     * 用户 取消喜欢
     * @param userId
     * @param videoId

     */
    public void userUnLikeVideo(String userId , String videoId ,String videoCreaterId) ;

    /**
     * 查询用户是否喜欢该视频
     */
    public boolean userLikeOrNot(String userId , String videoId) ;
    /**
     * 查询登陆用户关注的人 发布的视频
     */
    public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize);
    /**
     * 查询该用户喜欢的视频的列表
     */
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize);
    /**
     * 用户留言
     */
    public void saveComment(Comments comments);
    /**
     * 留言分页
     */
    public PagedResult getAllComments(String videoId,  Integer page , Integer pageSize) ;
    /**
     * 根据videoId 查询UserID
     */
    public String queryUserIdByVideoId(String videoId) ;
    /**
     * 视频删除
     */
    public boolean delVideoByVideoPath(String videoPath) ;
}
