package com.zfhy.egold.api.web.member;

import com.zfhy.egold.common.core.parameter.SysParameter;
import com.zfhy.egold.common.core.parameter.SysParameterWithoutToken;
import com.zfhy.egold.common.core.result.Result;
import com.zfhy.egold.common.core.result.ResultGenerator;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.member.dto.AddressDto;
import com.zfhy.egold.domain.member.dto.AddressOutputDto;
import com.zfhy.egold.domain.member.dto.MemberSession;
import com.zfhy.egold.domain.member.entity.Address;
import com.zfhy.egold.domain.member.service.AddressService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.CityDto;
import com.zfhy.egold.domain.sys.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/member/address")
@Api(value = "AddressController",tags = "AddressController", description = "收货地址")
@Slf4j
public class AddressController {
    @Resource
    private AddressService addressService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RedisService redisService;


    
    @PostMapping("/add")
    public Result<AddressOutputDto> add(@ModelAttribute AddressDto addressDto, @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());



        Address address = addressDto.convertTo();
        address.setCreateDate(new Date());
        address.setUpdateDate(new Date());
        address.setDelFlag("0");
        address.setMemberId(member.getId());



        addressService.save(address);



        return ResultGenerator.genSuccessResult(new AddressOutputDto().convertFrom(address));
    }

    
    @PostMapping("/update")
    public Result<AddressOutputDto> update(
            @ModelAttribute AddressOutputDto addressOutputDto,
            @ModelAttribute @Valid SysParameter sysParameter) {


        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        Address addressEntity = this.addressService.findById(addressOutputDto.getId());
        if (Objects.isNull(addressEntity)) {
            throw new BusException("收货地址不存在");
        }

        if (!Objects.equals(member.getId(), addressEntity.getMemberId())) {
            throw new BusException("不允许修改其他人的收货地址");
        }

        Address address = addressOutputDto.convertTo();
        address.setUpdateDate(new Date());
        address.setMemberId(member.getId());
        addressService.update(address);

        return ResultGenerator.genSuccessResult(new AddressOutputDto().convertFrom(address));
    }


    
    @PostMapping("/delete")
    public Result<String> delete(@ApiParam(name = "id", value = "收货地址id", required = true)
                                 @NotBlank @RequestParam Integer id,
                                 @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());

        addressService.deleteById(member.getId(), id);
        return ResultGenerator.genSuccessResult();
    }

    
    @PostMapping("/list")
    public Result<List<AddressOutputDto>> list(
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        List<AddressOutputDto> addressOutputDtos = addressService.list(member.getId());
        return ResultGenerator.genSuccessResult(addressOutputDtos);
    }


    
    @PostMapping("/setDefault")
    public Result<String> setDefault(
            @ApiParam(name = "id", value = "收货地址id", required = true)
            @NotBlank @RequestParam Integer id,
            @ModelAttribute @Valid SysParameter sysParameter) {

        MemberSession member = this.redisService.checkAndGetMemberToken(sysParameter.getToken());


        addressService.setDefault(member.getId(), id);
        return ResultGenerator.genSuccessResult();
    }

    
    @RequestMapping(value = {"/getDistrictByPid"}, method = RequestMethod.POST)
    public Result<CityDto> getDistrictByPid(
            @ApiParam(name = "id", value = "区域id（100000为顶级）", required = true)
            @RequestParam int id,
            @ModelAttribute @Valid SysParameterWithoutToken sysParameterWithoutToken) {

        CityDto cityDto = this.cityService.findCityAndSubCityById(id);

        return ResultGenerator.genSuccessResult(cityDto);

    }

}
