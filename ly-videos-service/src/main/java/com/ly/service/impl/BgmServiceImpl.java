package com.ly.service.impl;

import com.ly.common.org.n3r.idworker.Sid;
import com.ly.mapper.BgmMapper;
import com.ly.pojo.Bgm;
import com.ly.service.BgmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BgmServiceImpl implements BgmService {
    @Autowired
    private BgmMapper bgmMapper ;
    @Autowired
    private Sid sid ;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Bgm> queryBgmList() {

        return  bgmMapper.selectAll() ;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Bgm queryBgmById(String bgmId) {
        //根据主键查询bgm
       return  bgmMapper.selectByPrimaryKey(bgmId) ;
    }
}
