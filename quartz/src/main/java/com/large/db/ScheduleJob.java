package com.large.db;

import lombok.Data;

/**
 * @Package:        com.soa.quartz.mapper
 * @ClassName:      JobEntityRepository
 * @Description:    自定义保存定时任务表
 * @Author:          large
 * @CreateDate:     2018/10/31 13:47
 * @UpdateUser:     TODO:修改姓名
 * @UpdateDate:     2018/10/31 13:47
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 */

@Data
public class ScheduleJob {

    private Integer id;

    /**
     * job名称
     */
    private String name;

    /**
     * job组名
     */
    private String group;


    /**
     * cron表达式
     */
    private String cron;


    /**
     * job描述信息
     */
    private String description;


    /**
     * job的执行状态,这里我设置为OPEN/CLOSE且只有该值为OPEN才会执行该Job
     */
    private String status;


    /**
     * seivice 名
     */
    private String targetName;


    /**
     * 函数名
     */
    private String methodName;

}
