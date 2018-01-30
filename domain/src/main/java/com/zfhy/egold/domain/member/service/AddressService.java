package com.zfhy.egold.domain.member.service;

import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.common.core.Service;

import java.util.List;



public interface AddressService  extends Service<Address> {

    void deleteById(Integer memberId, Integer id);


    List<AddressOutputDto> list(Integer memberId);

    void setDefault(Integer memberId, Integer id);

    void deleteByMemberId(Integer memberId);

    Address findAddressById(Integer addressId);

    Address findDefaultAddress(Integer memberId);
}
