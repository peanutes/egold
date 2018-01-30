package com.test;

import com.alibaba.fastjson.JSON;
import com.zfhy.egold.AdminApplication;
import com.zfhy.egold.domain.fund.dao.RechargeMapper;
import com.zfhy.egold.domain.job.dao.JobMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by zhongmin on 2018/1/28.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AdminApplication.class)
public class MapperTest {
    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private RechargeMapper rechargeMapper;

    @Test
    public void test1(){
        System.out.println(JSON.toJSONString(jobMapper.queryScheduleJob("123")));
    }
    @Test
    public void test2(){
        System.out.println(JSON.toJSONString(rechargeMapper.list(new HashMap<>())));
    }
}
