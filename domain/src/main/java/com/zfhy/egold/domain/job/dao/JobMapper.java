package com.zfhy.egold.domain.job.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.job.entity.ScheduleJob;
import com.zfhy.egold.domain.job.entity.ScheduleLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface JobMapper {
	/**
	 * 
	 * @param job
	 */
//	@Insert("insert into t_schedulejob (uuid,name,cron,status,auto_start,create_date,creator,edit_date ,editor,is_del)values(#{uuid},#{name},#{cron},0,1,NOW(),'SYS',NOW(),'SYS',0)")
	void addJob(ScheduleJob job);
	/**
	 * 
	 * @param job
	 */
//	@Update("update t_schedulejob set status=#{status} where id=#{id}")
	void updateJobStatus(ScheduleJob job);

	/**
	 * @param uuid
	 * @return
	 */
//	@Select("select id ,uuid ,name ,cron  ,status ,auto_start,create_date,creator,edit_date ,editor,is_del from t_schedulejob  where uuid=#{0} and is_del = 0")
	ScheduleJob queryScheduleJob(String uuid);
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	Long queryLogCount(Map<String, Object> params);
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> queryLogList(Map<String, Object> params);

	/**
	 *
	 * @param log
	 */
	void addJobLog(ScheduleLog log);

	/**
	 *
	 * @param log
	 */
	void updateJobLog(ScheduleLog log);
}
