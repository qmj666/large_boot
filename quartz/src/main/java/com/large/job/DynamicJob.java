package com.large.job;

import com.large.service.impl.DynamicJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.MethodInvoker;

import java.lang.reflect.InvocationTargetException;


/**
 * @Package:        com.soa.quartz.job
 * @ClassName:      DynamicJob
 * @Description:    数据库job
 * @Author:          large
 * @CreateDate:     2018/10/31 9:55
 * @UpdateUser:     TODO:修改姓名
 * @UpdateDate:     2018/10/31 9:55
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * :@DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * :注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * :假如Job的设置时间间隔为3秒,但Job执行时间是5秒,设置 @DisallowConcurrentExecution 以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 */
@Slf4j
public class DynamicJob implements Job
{

    @Autowired
    private ApplicationContext appCtx;


    /**
     * 核心方法,Quartz Job真正的执行逻辑.
     * @param executorContext executorContext JobExecutionContext中封装有Quartz运行所需要的所有信息
     * @throws JobExecutionException execute()方法只允许抛出JobExecutionException异常
     */
    @Override
    public void execute(JobExecutionContext executorContext)
    {
        long startTime = System.currentTimeMillis();
        try
        {
            // JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
            JobDataMap map = executorContext.getMergedJobDataMap();
            String targetBeanName = map.getString("targetBeanName");
            String methodName = map.getString("methodName");
            log.info("Running Job targetBeanName : {} , methodName : {}", targetBeanName,methodName);
            Object bean = appCtx.getBean(targetBeanName);
            MethodInvoker beanMethod = new MethodInvoker();
            beanMethod.setTargetObject(bean);
            beanMethod.setTargetMethod(methodName);
            // TODO 可添加定时器函数入参
            Object[] arguments = null;
            beanMethod.setArguments(arguments);
            beanMethod.prepare();
            beanMethod.invoke();
        }
        catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
        {
            e.printStackTrace();
            log.error("quartz -- error " + e.getMessage());
        }
        long endTime = System.currentTimeMillis();
        log.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
    }

}
