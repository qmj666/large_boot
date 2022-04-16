package com.large.service;

import com.github.pagehelper.PageInfo;
import com.large.db.JobAndTriggerDto;
import com.large.db.ScheduleJob;
import com.large.db.SimpleJobEntity;
import org.quartz.*;

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
public interface IDynamicJobService
{

    /**
     * 获取JobDataMap.(Job参数对象)
     * @param job
     * @return
     */
    JobDataMap getJobDataMap(ScheduleJob job);


    /**
     * 获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     * @param jobKey
     * @param description
     * @param map
     * @param job
     * @return
     * @throws ClassNotFoundException
     */
    JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map, ScheduleJob job);


    /**
     * 获取Trigger (Job的触发器,执行规则)
     * @param job
     * @return
     */
    Trigger getTrigger(ScheduleJob job);


    void reStartAllJobs() throws SchedulerException;
    void reStartAllJob();

    String reStartOneJobs(Long id);

    JobDetail geSimpleJobDetail(String jobName, String groupName, String controllerPath, String arguments) throws ClassNotFoundException;

    Trigger getSimpleTrigger(String jobName, String groupName, int minutes);

    boolean createSimpleTimer(SimpleJobEntity entity);

    void pausejob(String jName, String jGroupe) throws SchedulerException;

    void resumejob(String jName, String jGroup) throws SchedulerException;

    void rescheduleJob(String jName, String jGroup, String cron) throws SchedulerException;

    void deletejob(String jName, String jGroup) throws SchedulerException;

    PageInfo<JobAndTriggerDto> getJobAndTriggerDetails(Integer pageNum, Integer pageSize);

}
