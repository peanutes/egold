package com.zfhy.egold.domain.cms.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.cms.dto.PcHomeHotInfoDto;
import com.zfhy.egold.domain.cms.entity.HotInfo;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HotInfoMapper extends Mapper<HotInfo> {
    void batchDelete(List<Integer> ids);


    @Select("select id, title from cms_hot_info where article_type=#{param1} and status=1 and del_flag='0' order by sort asc, create_date desc limit 0,#{param2}")
    List<PcHomeHotInfoDto> findPcHomeHotInfo(int articleType, int limit);
}
