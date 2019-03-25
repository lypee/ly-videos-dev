package com.ly.service;

import com.ly.pojo.Bgm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BgmService {
    /**
     * 查询背景音乐的列表
     */
    public List<Bgm> queryBgmList() ;
    /**
     * 根据id查询bgm的信息
     */
    public Bgm queryBgmById(String bgmId) ;

}
