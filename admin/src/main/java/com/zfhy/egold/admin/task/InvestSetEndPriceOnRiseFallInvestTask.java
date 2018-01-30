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
@ScheduledJob(orderNum = 4,name = "看涨跌填写到申购截止时价格", cron = "0 10/10 15 * * ?", method = "setEndPriceOnRiseFallInvest",autoStartup = true)
@Slf4j
public class InvestSetEndPriceOnRiseFallInvestTask {
    @Autowired
    private InvestRecordService investRecordService;

    /**
     * 看涨跌填写到申购截止时价格
     */
    public void setEndPriceOnRiseFallInvest() {

        PageHelper.startPage(1, 10);
        List<InvestRecord> investRecordList = this.investRecordService.findDeadLineInvestList();
        PageInfo<InvestRecord> pageInfo = new PageInfo<>(investRecordList);

        this.investRecordService.setEndPriceOnRiseFallInvest(investRecordList);


        
        int pages = pageInfo.getPages();

        if (pages > 1) {
            
            for (int j = 2; j <= pages; j++) {
                PageHelper.startPage(j, 10);
                investRecordList = this.investRecordService.findNoPayRiseFallInvestList();
                this.investRecordService.setEndPriceOnRiseFallInvest(investRecordList);
            }

        }


    }


}
