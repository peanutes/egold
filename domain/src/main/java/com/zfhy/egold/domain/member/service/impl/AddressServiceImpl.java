package com.zfhy.egold.domain.member.service.impl;

import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dao.AddressMapper;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.domain.member.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;



@Service
@Transactional
public class AddressServiceImpl extends AbstractService<Address> implements AddressService{
    @Autowired
    private AddressMapper addressMapper;

    @Override
    public void deleteById(Integer memberId, Integer id) {
        this.addressMapper.deleteByMemberIdAndId(memberId, id);
    }

    @Override
    public List<AddressOutputDto> list(Integer memberId) {
        return this.addressMapper.list(memberId);

    }

    @Override
    public void setDefault(Integer memberId, Integer id) {
        Address address = this.findById(id);
        if (Objects.isNull(address)) {
            throw new BusException("收货地址不存在");
        }

        if (!Objects.equals(address.getMemberId(), memberId)) {
            throw new BusException("不能操作其他人的收货地址");
        }

        this.addressMapper.clearDefault(memberId);

        address.setDefaultAddress("1");

        this.update(address);

    }

    @Override
    public void deleteByMemberId(Integer memberId) {
        this.addressMapper.deleteByMemberId(memberId);

    }

    @Override
    public Address findDefaultAddress(Integer memberId) {
        return this.addressMapper.findDefaultAddress(memberId);
    }

    @Override
    public Address findAddressById(Integer addressId) {
        return this.findBy("id", addressId);
    }
}
