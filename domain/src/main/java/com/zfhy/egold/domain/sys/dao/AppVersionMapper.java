package com.zfhy.egold.domain.sys.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.sys.entity.AppVersion;
import org.apache.ibatis.annotations.Select;

public interface AppVersionMapper extends Mapper<AppVersion> {
    @Select("select * from sys_app_version v where v.app_type = #{appType} order by v.upgrade_time desc limit 0,1")
    AppVersion findLatelyVersion(String appType);

}
