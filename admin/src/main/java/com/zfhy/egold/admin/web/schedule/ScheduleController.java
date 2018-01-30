/*
package com.zfhy.egold.admin.web.schedule;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zfhy.egold.common.constant.AppEnvConst;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.util.PageUtils;
import com.zfhy.egold.domain.schedule.entity.Job;
import com.zfhy.egold.schedule.service.JobService;
import com.zfhy.egold.schedule.util.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

*/
/**
 * Created by LAI on 2017/9/23.
 *//*

@RequestMapping("/schedule/job")
@Controller
@Validated
public class ScheduleController {
    String prefix = "schedule/job";

    @Autowired
    JobService jobService;


    @Autowired
    private Scheduler scheduler;

    @Value("${app.env}")
    private String appEnv;



    */
/**
     * 项目启动时，初始化定时器
     *//*

    @PostConstruct
    public void init(){
        if (Objects.equals(AppEnvConst.dev.name(), appEnv)) {
            return;

        }

        List<Job> jobList = this.jobService.findAll();
        for(Job job : jobList){
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, job.getId());
            
            if(cronTrigger == null) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }else {
                ScheduleUtils.updateScheduleJob(scheduler, job);
            }
        }
    }



    @RequiresPermissions("sys:schedule:schedule")
    @GetMapping()
    String job() {
        return prefix + "/job";
    }


    @RequiresPermissions("sys:schedule:schedule")
    @GetMapping("/list")
    @ResponseBody
    PageUtils list(@RequestParam Map<String, String> params) {

        PageHelper.offsetPage(Integer.parseInt(params.get("page")), Integer.parseInt(params.get("size")));
        Condition condition = new Condition(Job.class);
        Example.Criteria criteria = condition.createCriteria();
        List<Map.Entry<String, String>> parameters = params.entrySet().stream()
                .filter(entry -> StringUtils.isNotBlank(entry.getValue()))
                .filter(entry -> !"page".equalsIgnoreCase(entry.getKey()) && !"size".equalsIgnoreCase(entry.getKey()))
                .collect(Collectors.toList());

        for (Map.Entry<String, String> entry : parameters) {
            criteria.andEqualTo(entry.getKey(), entry.getValue());
        }

        List<Job> jobList = this.jobService.findByCondition(condition);
        PageInfo<Job> pageInfo = new PageInfo<>(jobList);

        return new PageUtils(jobList, (int)pageInfo.getTotal());
    }

    @RequiresPermissions("sys:schedule:add")
    @GetMapping("/add")
    String add() {
        return prefix + "/add";
    }

    @RequiresPermissions("sys:schedule:edit")
    @GetMapping("/edit/{id}")
    String edit(@PathVariable("id") Integer id, Model model) {
        Job job = jobService.findById(id);
        model.addAttribute("job", job);
        return prefix + "/edit";
    }

    @RequiresPermissions("sys:schedule:add")
    @PostMapping("/save")
    @ResponseBody()
    Result<String> save(Job job) {

        job.setStatus(0);
        this.jobService.save(job);

        ScheduleUtils.createScheduleJob(scheduler, job);
        return ResultGenerator.genSuccessResult();
    }

    @RequiresPermissions("sys:schedule:edit")
    @PostMapping("/update")
    @ResponseBody()
    Result<String> update(Job job) {
        this.jobService.update(job);

        ScheduleUtils.updateScheduleJob(scheduler, job);
        return ResultGenerator.genSuccessResult();
    }

    @RequiresPermissions("sys:schedule:remove")
    @PostMapping("/remove")
    @ResponseBody()
    Result<String> remove(Integer id) {
        

        this.jobService.deleteById(id);

        return ResultGenerator.genSuccessResult();
    }


    @RequiresPermissions("sys:schedule:batchRemove")
    @PostMapping("/batchRemove")
    @ResponseBody
    Result<String> batchRemove(@RequestParam("ids[]") Integer[] jobIds) {
        if (jobIds == null || jobIds.length == 0) {
            return ResultGenerator.genSuccessResult();
        }

        String userIdStr = Stream.of(jobIds).map(Object::toString).collect(Collectors.joining(","));
        this.jobService.deleteByIds(userIdStr);

        return ResultGenerator.genSuccessResult();
    }


    */
/**
     * 立即执行任务
     *//*

    @RequestMapping("/run")
    @RequiresPermissions("sys:schedule:run")
    @ResponseBody()
    public Result<String> run(Integer id){
        this.jobService.run(id);

        return ResultGenerator.genSuccessResult();
    }

    */
/**
     * 暂停定时任务
     *//*

    @RequestMapping("/pause")
    @RequiresPermissions("sys:schedule:pause")
    @ResponseBody()
    public Result<String> pause(Integer id){
        jobService.pause(id);

        return ResultGenerator.genSuccessResult();
    }

    */
/**
     * 恢复定时任务
     *//*

    @RequestMapping("/resume")
    @RequiresPermissions("sys:schedule:resume")
    @ResponseBody()
    public Result<String> resume(Integer id){
        jobService.resume(id);

        return ResultGenerator.genSuccessResult();
    }




}
*/
