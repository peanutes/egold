package com.zfhy.egold.domain.member.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.entity.Address;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AddressMapper extends Mapper<Address> {
    @Delete("delete from member_address where id=#{param2} and member_id=#{param1} ")
    void deleteByMemberIdAndId(Integer memberId, Integer id);

    @Select("select * from member_address where member_id=#{param1}")
    List<AddressOutputDto> list(Integer memberId);

    @Update("update member_address set default_address='0' where member_id=#{param1}")
    void clearDefault(Integer memberId);

    @Delete("delete from member_address where  member_id=#{param1} ")
    void deleteByMemberId(Integer memberId);

    @Select("select * from member_address where member_id=#{param1} and default_address='1' ")
    Address findDefaultAddress(Integer memberId);
}
