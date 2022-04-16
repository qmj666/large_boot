package com.large.controller;

import com.github.pagehelper.PageInfo;
import com.large.db.JobAndTriggerDto;
import com.large.db.SimpleJobEntity;
import com.large.service.IDynamicJobService;
import com.large.dto.ResultMap;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.soa.quartz.controller
 * @ClassName: JobController
 * @Description: 定时器操作
 * @Author: large
 * @CreateDate: 2018/10/31 17:32
 * @UpdateUser: TODO:修改姓名
 * @UpdateDate: 2018/10/31 17:32
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
@Slf4j
public class JobController {

    @Resource(name = "quartz")
    private IDynamicJobService jobService;

    @PostConstruct
    /**
     * @methodname initialize
     * @see            从数据空中获取初始化定时器
     * @author large
     * @version 1.0
     * @param
     * @return void
     * @exception
     * @date 2018/10/31 17:31
     */
    public void initialize() {
        try {
            jobService.reStartAllJobs();
            log.info("quartz启动加载成功");
        } catch (SchedulerException e) {
            log.error("quartz启动加载失败 : " + e.getMessage());
            e.printStackTrace();
            log.error(e.getMessage());
        }
    }

    @PostMapping("/refresh/{id}")
    /**
     * @methodname 根据ID重启某个Job
     * @see            TODO
     * @author large
     * @version 1.0
     * @param          id
     * @return java.lang.String
     * @exception
     * @date 2018/10/31 17:31
     */
    public String refresh(@PathVariable Long id) {
        return jobService.reStartOneJobs(id);
    }


    @PostMapping("/refresh/all")
    /**
     * @methodname initialize
     * @see            从数据空中获取初始化定时器
     * @author large
     * @version 1.0
     * @param
     * @return void
     * @exception
     * @date 2018/10/31 17:31
     */
    public String refreshAll() {
        String result;
        try {
            jobService.reStartAllJobs();
            result = "SUCCESS";
        } catch (SchedulerException e) {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "refresh all jobs : " + result;
    }


    @PostMapping("/createTimer")
    /**
     * @methodname createSimpleTimer
     * @see            创建内存定时器
     * @author large
     * @version 1.0
     * @param          entity
     * @return void
     * @exception
     * @date 2018/11/2 9:39
     */
    public void createSimpleTimer(@RequestBody SimpleJobEntity entity) {
        log.debug("createTimer--------------->start---->>" + entity.toString());
        log.debug("createTimer--------------->param jobName---->>" + entity.getJobName());
        log.debug("createTimer--------------->param groupName---->>" + entity.getGroupName());
        log.debug("createTimer--------------->param resultUrl---->>" + entity.getControllerPath());
        log.debug("createTimer--------------->param arguments---->>" + entity.getArguments());
        log.debug("createTimer--------------->param minutes---->>" + entity.getMinutes());
        jobService.createSimpleTimer(entity);
        log.debug("createTimer--------------->end---->>");
    }

    /**
     * 暂停任务
     *
     * @param jName  任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/pausejob")
    @ResponseBody
    public ResultMap pausejob(String jName, String jGroup) {
        try {
            jobService.pausejob(jName, jGroup);
            return new ResultMap().success().message("暂停任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultMap().error().message("暂停任务失败");
        }
    }

    /**
     * 恢复任务
     *
     * @param jName  任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/resumejob")
    @ResponseBody
    public ResultMap resumejob(String jName, String jGroup) {
        try {
            jobService.resumejob(jName, jGroup);
            return new ResultMap().success().message("恢复任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultMap().error().message("恢复任务失败");
        }
    }

    /**
     * 删除任务
     *
     * @param jName  任务名称
     * @param jGroup 任务组
     * @return ResultMap
     */
    @PostMapping(path = "/deletejob")
    @ResponseBody
    public ResultMap deletejob(String jName, String jGroup) {
        try {
            jobService.deletejob(jName, jGroup);
            return new ResultMap().success().message("删除任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultMap().error().message("删除任务失败");
        }
    }

    /**
     * 重启任务
     *
     * @param jName  任务名称
     * @param jGroup 任务组
     * @param cron   cron表达式
     * @return ResultMap
     */
    @PostMapping(path = "/reschedulejob")
    @ResponseBody
    public ResultMap rescheduleJob(String jName, String jGroup, String cron) {
        try {
            jobService.rescheduleJob(jName, jGroup, cron);
            return new ResultMap().success().message("重启任务成功");
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ResultMap().error().message("重启任务失败");
        }
    }


    /**
     * 查询任务
     *
     * @param pageNum  页码
     * @param pageSize 每页显示多少条数据
     * @return Map
     */
    @GetMapping(path = "/queryjob")
    @ResponseBody
    public ResultMap queryjob(Integer pageNum, Integer pageSize) {
        PageInfo<JobAndTriggerDto> pageInfo = jobService.getJobAndTriggerDetails(pageNum, pageSize);
        Map<String, Object> map = new HashMap<>();
        if (!ObjectUtils.isEmpty(pageInfo.getTotal())) {
            map.put("JobAndTrigger", pageInfo);
            map.put("number", pageInfo.getTotal());
            return new ResultMap().success().data(map).message("查询任务成功");
        }
        return new ResultMap().fail().message("查询任务成功失败，没有数据");
    }

}
