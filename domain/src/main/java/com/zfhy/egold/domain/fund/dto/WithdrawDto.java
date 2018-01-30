package com.zfhy.egold.domain.fund.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.fund.entity.Withdraw;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawDto {


    
    private Integer id;


    
    private Integer memberId;
    

    
    private Double withdrawAmount;
    

    
    private Double payAmount;
    

    
    private String withdrawAccount;
    

    
    private String mobile;
    

    
    private String realName;
    

    
    private Double withdrawFee;
    

    
    private String status;
    

    
    private Date applyTime;
    

    
    private Integer bankCardId;
    

    
    private Integer adminId;
    

    
    private String operator;
    

    
    private Date auditorTime;
    

    
    private String ip;
    

    
    private Integer version;
    

    
    private String province;
    

    
    private String city;
    

    
    private String bankNo;
    

    
    private String bankName;
    

    
    private String remarks;
    



    
    public Withdraw convertTo() {
        return  new WithdrawDtoConvert().convert(this);
    }

    
    public WithdrawDto convertFrom(Withdraw withdraw) {
        return new WithdrawDtoConvert().reverse().convert(withdraw);

    }


    private static class WithdrawDtoConvert extends  Converter<WithdrawDto, Withdraw> {


        @Override
        protected Withdraw doForward(WithdrawDto withdrawDto) {
            Withdraw withdraw = new Withdraw();
            BeanUtils.copyProperties(withdrawDto, withdraw);
            return withdraw;
        }

        @Override
        protected WithdrawDto doBackward(Withdraw withdraw) {
            WithdrawDto withdrawDto = new WithdrawDto();
            BeanUtils.copyProperties(withdraw, withdrawDto);
            return withdrawDto;
        }
    }
}
