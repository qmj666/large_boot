package com.large.dao;

import com.large.db.ScheduleJob;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


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
@Repository(value="scheduleJob")
//@Mapper
public interface ScheduleJobMapper
{
    List<ScheduleJob> findJob(Long id);
}
