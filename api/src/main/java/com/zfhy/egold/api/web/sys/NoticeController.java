package com.zfhy.egold.api.web.sys;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Page;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.dto.MailDto;
import com.zfhy.egold.domain.sys.dto.NoticeDto;
import com.zfhy.egold.domain.sys.entity.Mail;
import com.zfhy.egold.domain.sys.entity.Notice;
import com.zfhy.egold.domain.sys.service.DictService;
import com.zfhy.egold.domain.sys.service.MailService;
import com.zfhy.egold.domain.sys.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/sys/notice")
@Api(value = "NoticeController",tags = "NoticeController", description = "公告")
@Slf4j
@Validated
public class NoticeController {
    @Resource
    private NoticeService noticeService;

    @Autowired
    private DictService dictService;


    @Autowired
    private RedisService redisService;

    @Autowired
    private MailService mailService;

    
    @PostMapping("/list")
    public Result<List<NoticeDto>> list(@ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        List<Notice> noticeList = this.noticeService.listAll();

        String urlPrefix = this.dictService.findStringByType(DictType.URL_PREFIX);

        List<NoticeDto> noticeDtos = noticeList.stream()
                .map(notice -> new NoticeDto().convertFrom(notice, urlPrefix, sysParameterWithoutToken.getTerminalType(), sysParameterWithoutToken.getTerminalId()))
                .collect(Collectors.toList());
        return ResultGenerator.genSuccessResult(noticeDtos);

    }


    
    @PostMapping("/myMail")
    public Result<Page<MailDto>> myMail(
            @ApiParam(name = "page", value = "页数", required = true)
            @RequestParam @NotNull(message = "页数不允许为空") Integer page,
            @ApiParam(name = "size", value = "每页条数", required = true)
            @RequestParam @NotNull(message = "每页条数不允许为空") Integer size,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        PageHelper.startPage(page, size);
        List<Mail> mailList = this.mailService.findMailByMemberId(member.getId());


        PageInfo<Mail> mailPageInfo = new PageInfo<>(mailList);

        Page<MailDto> incomeDailyDtoPage = new Page<>();
        BeanUtils.copyProperties(mailPageInfo, incomeDailyDtoPage);

        List<MailDto> mailDtos = mailList.stream().map(e -> new MailDto().convertFrom(e)).collect(Collectors.toList());
        incomeDailyDtoPage.setList(mailDtos);



        return ResultGenerator.genSuccessResult(incomeDailyDtoPage);

    }



    
    @PostMapping("/settingMailRead")
    public Result<String> settingMailRead(
            @ApiParam(name = "mailId", value = "站内信id", required = true)
            @RequestParam @NotNull(message = "站内信id不允许为空") Integer mailId,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        this.mailService.settingMailRead(member.getId(), mailId);


        return ResultGenerator.genSuccessResult();

    }

}
