package com.ly.mapper;

import com.ly.common.utils.MyMapper;
import com.ly.pojo.SearchRecords;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {

    public List<String> getHotwords();

}
