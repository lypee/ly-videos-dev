package com.ly.mapper;

import com.ly.common.utils.MyMapper;
import com.ly.pojo.Comments;
import com.ly.pojo.vo.CommentsVO;

import java.util.List;

public interface CommentsMapperCustom extends MyMapper<Comments> {

    public List<CommentsVO> queryComments(String videoId);
}