package com.large.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.large.dao.JobDetailMapper;
import com.large.dao.ScheduleJobMapper;
import com.large.db.JobAndTriggerDto;
import com.large.db.ScheduleJob;
import com.large.db.SimpleJobEntity;
import com.large.service.IDynamicJobService;
import com.large.job.DynamicJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.DateBuilder.futureDate;

/**
 * @Package:        com.soa.quartz.service
 * @ClassName:      IDynamicJobService
 * @Description:    定时器sevice
 * @Author:          large
 * @CreateDate:     2018/10/31 14:09
 * @UpdateUser:     TODO:修改姓名
 * @UpdateDate:     2018/10/31 14:09
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 */
@Service("quartz")
@Slf4j
public class DynamicJobService implements IDynamicJobService
{

    @Resource(name = "scheduleJob")
    private ScheduleJobMapper repository;

    @Autowired
    private JobDetailMapper jobDetailMapper;

    @Autowired
    private Scheduler scheduler;

    @Override
     /**
      * @methodname    getJobDataMap
      * @see            获取JobDataMap(Job参数对象)
      * @author         large
      * @version        1.0
      * @param          job
      * @return         org.quartz.JobDataMap
      * @exception
      * @date           2018/10/31 14:15
      */
    public JobDataMap getJobDataMap(ScheduleJob job)
    {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("group", job.getGroup());
        map.put("cronExpression", job.getCron());
        map.put("JobDescription", job.getDescription());
        map.put("status", job.getStatus());
        map.put("targetBeanName", job.getTargetName());
        map.put("methodName", job.getMethodName());
        return map;
    }


    @Override
    /**
     * @methodname    geJobDetail
     * @see            获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     * @author         large
     * @version        1.0
     * @param          jobKey
     * @param          description
     * @param          map
     * @return         org.quartz.JobDetail
     * @exception
     * @date           2018/10/31 14:14
     */
    public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map, ScheduleJob job) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }


    @Override
    /**
     * @methodname    getTrigger
     * @see            获取Trigger (Job的触发器,执行规则)
     * @author         large
     * @version        1.0
     * @param          job
     * @return         org.quartz.Trigger
     * @exception
     * @date           2018/10/31 14:14
     */
    public Trigger getTrigger(ScheduleJob job)
    {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(job.getName(), job.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .build();
    }

    @Override
     /**
      * @methodname    geSimpleJobDetail
      * @see            获取临时创建JobDetail,
      * @author         large
      * @version        1.0
      * @param          jobName         job名
      * @param          groupName       组名
      * @param          description     job备注
      * @return         org.quartz.JobDetail
      * @exception
      * @date           2018/10/31 17:17
      */
    public JobDetail geSimpleJobDetail(String jobName, String groupName, String controllerPath, String arguments)
    {
        JobDataMap map = new JobDataMap();
        map.put("jobName",jobName);
        map.put("groupName",groupName);
        map.put("controllerPath",controllerPath);
        map.put("arguments",arguments);
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobName, groupName)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    @Override
     /**
      * @methodname    getSimpleTrigger
      * @see            获取Trigger (Job的触发器,执行规则) 仅执行一次
      * @author         large
      * @version        1.0
      * @param          jobName
      * @param          groupName
      * @return         org.quartz.Trigger
      * @exception
      * @date           2018/10/31 17:17
      */
    public Trigger getSimpleTrigger(String jobName, String groupName, int minutes)
    {
        return TriggerBuilder
                .newTrigger()
                .withIdentity(jobName+"Trg",groupName+"Trg")
                .startAt(futureDate(minutes, DateBuilder.IntervalUnit.MINUTE))
                .forJob(jobName,groupName)
                .build();
    }


    @Override
     /**
      * @methodname    createSimpleTimer
      * @see            创建临时定时器执行一次
      * @author         large
      * @version        1.0
      * @param          jobName
      * @param          groupName
      * @param          description
      * @param          arguments
      * @return         boolean
      * @exception
      * @date           2018/10/31 17:19
      */
    public boolean createSimpleTimer(SimpleJobEntity entity)
    {
        try
        {
            JobDetail jobDetail = geSimpleJobDetail(entity.getJobName(),entity.getGroupName(),entity.getControllerPath(),entity.getArguments());
            Trigger trigger = getSimpleTrigger(entity.getJobName(),entity.getGroupName(),entity.getMinutes());
            if (jobDetail != null && trigger != null)
            {

                scheduler.deleteJob(JobKey.jobKey(entity.getJobName(), entity.getGroupName()));
                scheduler.scheduleJob(jobDetail, trigger);
                log.debug("createSimpleTimer------------>>>success");
                return true;
            }
        } catch (SchedulerException e)
        {
            e.printStackTrace();
            log.error("createSimpleTimer------------>>>" + e.getMessage());
        }
        return false;
    }



    @Override
     /**
      * @methodname    reStartAllJobs
      * @see            重新启动所有的job
      * @author         large
      * @version        1.0
      * @param
      * @return         void
      * @exception
      * @date           2018/10/31 17:19
      */
    public void reStartAllJobs() throws SchedulerException
    {

        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            scheduler.deleteJob(jobKey);
        }
        // 从数据库中加载获取到所有Job
        List<ScheduleJob> list = new ArrayList<>();
        repository.findJob(null).forEach(list::add);
        for (ScheduleJob job : list) {
            log.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getGroup(), job.getCron());
            JobDataMap map = getJobDataMap(job);
            JobKey jobKey = JobKey.jobKey(job.getName(), job.getGroup());
            JobDetail jobDetail = geJobDetail(jobKey, job.getDescription(), map ,job);
            if (job.getStatus().equals("OPEN"))
            {
                scheduler.scheduleJob(jobDetail, getTrigger(job));
                scheduler.start();
            }
            else {
                log.info("Job jump name : {} , Because {} status is {}", job.getName(), job.getName(), job.getStatus());
            }
        }
    }

    public void reStartAllJob()
    {
        System.out.println("启动完毕1");
        System.out.println("启动完毕2");
        System.out.println("启动完毕3");
    }

    @Override
     /**
      * @methodname    reStartOneJobs
      * @see            重启指定job
      * @author         large
      * @version        1.0
      * @param          id
      * @return         java.lang.String
      * @exception
      * @date           2018/10/31 17:19
      */
    public String reStartOneJobs(Long id)
    {
        String result;
        ScheduleJob entity;
        entity = repository.findJob(id).get(0);
        if (entity == null) {
            return "error: id is not exist ";
        }
        TriggerKey triggerKey = new TriggerKey(entity.getName(), entity.getGroup());
        JobKey jobKey = JobKey.jobKey(entity.getName(), entity.getGroup());
        try {
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            JobDataMap map = getJobDataMap(entity);
            ScheduleJob job = new ScheduleJob();
            JobDetail jobDetail = geJobDetail(jobKey, entity.getDescription(), map, job);
            if (entity.getStatus().equals("OPEN")) {
                scheduler.scheduleJob(jobDetail, getTrigger(entity));
                result = "Refresh Job : " + entity.getName()  + " success !";
            } else {
                result = "Refresh Job : " + entity.getName()  + " failed ! , " +
                        "Because the Job status is " + entity.getStatus();
            }
        } catch (SchedulerException e) {
            result = "Error while Refresh " + e.getMessage();
        }
        return result;
    }


    @Override
    public void pausejob(String jName, String jGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jName, jGroup));
    }

    @Override
    public void resumejob(String jName, String jGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jName, jGroup));
    }

    @Override
    public void deletejob(String jName, String jGroup) throws SchedulerException {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jName, jGroup));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jName, jGroup));
        scheduler.deleteJob(JobKey.jobKey(jName, jGroup));
    }

    @Override
    public void rescheduleJob(String jName, String jGroup, String cron) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jName, jGroup);
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        // 按新的cronExpression表达式重新构建trigger
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        // 按新的trigger重新设置job执行，重启触发器
        scheduler.rescheduleJob(triggerKey, trigger);
    }

    @Override
    public PageInfo<JobAndTriggerDto> getJobAndTriggerDetails(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<JobAndTriggerDto> list = jobDetailMapper.getJobAndTriggerDetails();
        PageInfo<JobAndTriggerDto> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}
