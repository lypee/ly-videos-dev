package com.ly.mapper;

import java.util.List ;
import org.apache.ibatis.annotations.Param ;
import com.ly.pojo.Videos ;
import com.ly.pojo.vo.VideosVO ;
import com.ly.common.utils.MyMapper ;

public interface VideosMapperCustom extends MyMapper<Videos>{
    /**
     *
     */
    public List<VideosVO> queryAllVideos(@Param("videoDesc")String videoDesc);
    /**
     *
     */
    public List<VideosVO> queryMyFollowVideos(String userId) ;
    /**

     */
    public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId) ;
    /**

     */
    public void addVideoLikeCount(String videoId) ;
    /**

     */
    public void reduceVideoLikeCount(String videoId) ;

    /**
     * select this user like this video or not
     */
    public boolean userLikeOrNot(String userId) ;
    /**
     * 根据videoId 查询 UserID
     */
    public String queryUserIdByVideoId(String videoId) ;
    /**
     *
     */
}
