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
public class SimpleJobEntity {

    /**
     * job名称
     */
    private String jobName;

    /**
     * job组名
     */
    private String groupName;

    /**
     * 接口地址
     */
    private String controllerPath;

    /**
     * 定时器参数
     */
    private String arguments;

    /**
     * 分钟
     */
    private Integer minutes;

}
