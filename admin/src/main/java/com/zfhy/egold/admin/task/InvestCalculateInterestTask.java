package com.zfhy.egold.admin.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.schedule.config.ScheduledJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by LAI on 2017/10/20.
 */
@Component
@ScheduledJob(orderNum = 2,name = "活期金，定期金计息（每晚12点30分开始计息）", cron = "0 30 0 * * ?", method = "calculateInterest",autoStartup = true)
@Slf4j
public class InvestCalculateInterestTask {
    @Autowired
    private InvestRecordService investRecordService;


    public void calculateInterest() {

        PageHelper.startPage(1, 10);
        List<InvestRecord> investRecordList = this.investRecordService.findNoPayInvestList();
        PageInfo<InvestRecord> pageInfo = new PageInfo<>(investRecordList);

        this.investRecordService.calculateInterest(investRecordList);


        
        int pages = pageInfo.getPages();

        if (pages > 1) {
            
            for (int j = 2; j <= pages; j++) {
                PageHelper.startPage(j, 10);
                investRecordList = this.investRecordService.findNoPayInvestList();
                this.investRecordService.calculateInterest(investRecordList);
            }

        }


    }






}
