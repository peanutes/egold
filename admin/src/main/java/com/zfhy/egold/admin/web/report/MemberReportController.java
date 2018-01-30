package com.zfhy.egold.admin.web.report;

import com.google.common.collect.Lists;
import com.zfhy.egold.admin.log.Log;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.member.service.BankcardService;
import com.zfhy.egold.domain.member.service.IdcardService;
import com.zfhy.egold.domain.member.service.MemberService;
import com.zfhy.egold.domain.report.dto.CountDto;
import com.zfhy.egold.domain.report.dto.MemberReportDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by LAI on 2017/12/20.
 */
@Controller
@RequestMapping("/report/member")
@Slf4j
@Validated
public class MemberReportController {
    private String prefix = "report/member";


    @Autowired
    private MemberService memberService;

    @Autowired
    private IdcardService idcardService;

    @Autowired
    private BankcardService bankcardService;


    @RequiresPermissions("report:member:registerReport")
    @GetMapping()
    String registerReport() {
        return prefix + "/registerReport";
    }








    
    @RequiresPermissions("report:member:registerReport")
    @PostMapping("/registerReport")
    @ResponseBody
    Result<MemberReportDto> registerReport(@RequestParam Map<String, String> params) {

        String beginDateStr = params.get("beginDate");
        String endDateStr = params.get("endDate");


        if (StringUtils.isBlank(endDateStr)) {
            endDateStr = DateUtil.toString(new Date());
        }

        if (StringUtils.isBlank(beginDateStr)) {
            beginDateStr = DateUtil.toString(DateUtil.afterDays(-30));
        }


        params.clear();
        params.put("beginDate", beginDateStr);
        params.put("endDate", endDateStr);

        Date beginDate = DateUtil.convertStringToDate(DateUtil.YYYY_MM_DD_HH_MM_SS, beginDateStr);
        Date endDate = DateUtil.convertStringToDate(DateUtil.YYYY_MM_DD_HH_MM_SS, endDateStr);

        TreeMap<String, Integer> registerMap = initMap(beginDate, endDate);
        Map<String, Integer> realNameMap = initMap(beginDate, endDate);
        Map<String, Integer> bindCardMap = initMap(beginDate, endDate);




        List<CountDto> registerReportList = this.memberService.statisticRegister(params);

        registerReportList.stream().forEach(e->{
            if (registerMap.containsKey(e.getCountDate())) {
                registerMap.put(e.getCountDate(), e.getCount());
            }
        });

        List<CountDto> realNameList = this.idcardService.statisticBindIdcard(params);
        realNameList.stream().forEach(e->{
            if (realNameMap.containsKey(e.getCountDate())) {
                realNameMap.put(e.getCountDate(), e.getCount());
            }
        });

        List<CountDto> bindBankCardList = this.bankcardService.statisticBindCard(params);
        bindBankCardList.stream().forEach(e->{
            if (bindCardMap.containsKey(e.getCountDate())) {
                bindCardMap.put(e.getCountDate(), e.getCount());
            }
        });









        List<String> keyList = Lists.newArrayList(registerMap.keySet());
        keyList.sort(String::compareTo);

        List<Integer> registerCountList = keyList.stream().map(registerMap::get).collect(Collectors.toList());
        List<Integer> realNameCountList = keyList.stream().map(realNameMap::get).collect(Collectors.toList());
        List<Integer> bindCardCountList = keyList.stream().map(bindCardMap::get).collect(Collectors.toList());

        MemberReportDto memberReportDto = MemberReportDto.builder()
                .xaxis(keyList)
                .registerCountList(registerCountList)
                .realNameCountList(realNameCountList)
                .bindCardCountList(bindCardCountList)
                .build();

        return ResultGenerator.genSuccessResult(memberReportDto);
    }

    private TreeMap<String, Integer> initMap(Date beginDate, Date endDate) {
        TreeMap<String, Integer> countMap = new TreeMap<>();

        while (endDate.after(beginDate)) {
            countMap.put(DateUtil.toString(beginDate, DateUtil.YYYY_MM_DD), 0);
            beginDate = DateUtil.afterDays(beginDate, 1);
        }
        return countMap;
    }

}
