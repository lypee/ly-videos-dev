package com.ly.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ly.common.org.n3r.idworker.Sid;
import com.ly.common.utils.PagedResult;
import com.ly.common.utils.TimeAgoUtils;
import com.ly.mapper.*;
import com.ly.pojo.Comments;
import com.ly.pojo.SearchRecords;
import com.ly.pojo.UsersLikeVideos;
import com.ly.pojo.Videos;
import com.ly.pojo.vo.CommentsVO;
import com.ly.pojo.vo.VideosVO;
import com.ly.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.Date;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    public VideoMapper videoMapper ;
    //注入自定义mapper
    @Autowired
    public VideosMapperCustom videosMapperCustom ;
    @Autowired
    public Sid sid ;
    @Autowired
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper ;
    @Autowired
    private UsersMapper usersMapper ;
    @Autowired
    private CommentsMapper commentsMapper ;
    @Autowired
    private CommentsMapperCustom commentsMapperCustom ;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos videos) {
//        Example videoExample = new Example(Videos.class);
        String id = sid.nextShort() ;
        videos.setId(id);
        videoMapper.insertSelective(videos) ;
        return id ;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateVideo(String videoId, String coverPath) {
        Videos videos = new Videos() ;
        videos.setId(videoId);
        videos.setCoverPath(coverPath);
        videoMapper.updateByPrimaryKeySelective(videos) ;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public PagedResult getAllVideos(Videos videos , Integer isSaveRecord  , Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize) ;


        //保存热搜词的记录
        String desc = videos.getVideoDesc() ;
        String userId = videos.getUserId() ;
        if(isSaveRecord != null && isSaveRecord == 1){
            SearchRecords records = new SearchRecords() ;
            String recordsId = sid.nextShort() ;
            records.setId(recordsId);
            records.setContent(desc);
            //保存热搜词
            searchRecordsMapper.insert(records) ;
        }

        List<VideosVO>  list = videosMapperCustom.queryAllVideos(desc) ;
        PageInfo<VideosVO> pageList = new PageInfo<>(list) ;
        PagedResult pagedResult = new PagedResult() ;
        //设置当前页
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        //设置查询后分页的记录数
        pagedResult.setRows(list);
        //查询列表的数据量
        pagedResult.setRecords(pageList.getTotal());
        return pagedResult ;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<String> getHotWords() {

        //自己写的 可以测试 返回
//    List<SearchRecords> searchRecordsList = searchRecordsMapper.selectAll();
//    List<String> ansString = new ArrayList<>() ;
//    //得到热搜词
//    for(SearchRecords searchRecords:searchRecordsList){
//        ansString.add(searchRecords.getContent()) ;
//    }
//    return ansString ;
//}
        return searchRecordsMapper.getHotwords();
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userLikeVideo(String userId, String videoId, String videoCreaterId) {
        //保存用户喜欢的点赞的关联关系表
        String likeId = sid.nextShort();
        UsersLikeVideos  usersLikeVideos = new UsersLikeVideos() ;
        usersLikeVideos.setId(likeId);
        usersLikeVideos.setVideoId(videoId);
        usersLikeVideos.setUserId(userId);
        //调用 使videoCreateId 加1 .
        usersLikeVideosMapper.insert(usersLikeVideos) ;
        //视频喜欢数量累加
        videosMapperCustom.addVideoLikeCount(videoId);
        //用户受 喜欢的数量累加
        usersMapper.addReceiveLikeCount(userId);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void userUnLikeVideo(String userId, String videoId, String videoCreaterId) {
        Example example = new Example(UsersLikeVideos.class) ;
        Criteria criteria = example.createCriteria() ;

        criteria.andEqualTo("userId",userId) ;
        criteria.andEqualTo("videoId" , videoId) ;
        //视频喜欢数量 -1 ;
        usersLikeVideosMapper.deleteByExample(example) ;

       //video受喜欢的数量 -1
        videosMapperCustom.reduceVideoLikeCount(videoId);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean userLikeOrNot(String userId , String videoId){

       return (videoId.equals(videosMapperCustom.userLikeOrNot(userId))) ;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyFollowVideos(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page , pageSize) ;
        //获取该用户关注的视频列表
        List<VideosVO> list = videosMapperCustom.queryMyFollowVideos(userId) ;

        PageInfo<VideosVO> pageList = new PageInfo<>(list) ;

        PagedResult pagedResult = new PagedResult() ;
        pagedResult.setRecords(pageList.getTotal());
        pagedResult.setRows(list);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setPage(page);
        return pagedResult ;

    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult queryMyLikeVideos(String userId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<VideosVO> list = videosMapperCustom.queryMyLikeVideos(userId);

        PageInfo<VideosVO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setPage(page);
        pagedResult.setRecords(pageList.getTotal());

        return pagedResult;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveComment(Comments comments) {
    String id =sid.nextShort() ;
    comments.setId(id);
    comments.setCreateTime(new Date());
    commentsMapper.insert(comments);

    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedResult getAllComments(String videoId, Integer page, Integer pageSize) {
        //分页
        PageHelper.startPage(page, pageSize) ;
        List<CommentsVO> list = commentsMapperCustom.queryComments(videoId) ;
        for(CommentsVO c :list){
            String timeAgo = TimeAgoUtils.format(c.getCreateTime());
            c.setTimeAgoStr(timeAgo);
        }
        PageInfo<CommentsVO> pageList = new PageInfo<>();

        PagedResult grid = new PagedResult();

        grid.setTotal(pageList.getPages());
        grid.setRows(list) ;
        grid.setRows(list) ;
        grid.setPage(page) ;
        grid.setRecords(pageList.getTotal()) ;
        return grid ;
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String queryUserIdByVideoId(String videoId){
      return   videosMapperCustom.queryUserIdByVideoId(videoId);
    }

    @Override
    public boolean delVideoByVideoPath(String videoPath) {
        Videos videos = new Videos() ;
        videos.setVideoPath(videoPath);
        return ( videoMapper.delete(videos) > 0 ) ? true : false ;
    }
}
