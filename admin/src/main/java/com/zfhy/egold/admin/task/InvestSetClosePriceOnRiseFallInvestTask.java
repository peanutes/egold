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
@ScheduledJob(orderNum = 3,name = "看涨跌填写到结束收盘价格", cron = "0 10/10 15 * * ?", method = "setClosePriceOnRiseFallInvest",autoStartup = true)
@Slf4j
public class InvestSetClosePriceOnRiseFallInvestTask {
    @Autowired
    private InvestRecordService investRecordService;

    /**
     * 看涨跌填写到结束收盘价格
     */
    public void setClosePriceOnRiseFallInvest() {

        PageHelper.startPage(1, 10);
        List<InvestRecord> investRecordList = this.investRecordService.findShouldCloseInvestList();
        PageInfo<InvestRecord> pageInfo = new PageInfo<>(investRecordList);

        this.investRecordService.setClosePriceOnRiseFallInvest(investRecordList);


        int pages = pageInfo.getPages();

        if (pages > 1) {
            
            for (int j = 2; j <= pages; j++) {
                PageHelper.startPage(j, 10);
                investRecordList = this.investRecordService.findShouldCloseInvestList();
                this.investRecordService.setClosePriceOnRiseFallInvest(investRecordList);
            }

        }


    }



}
