package com.zfhy.egold.domain.order.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.domain.order.entity.Myorder;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyorderDto {


    
    private Integer id;

    
    private Integer memberId;
    

    
    private String relateOrderId;
    

    
    private Integer myorderType;
    

    
    private Integer myorderStatus;
    

    
    private String productName;
    

    
    private Double annualRate;
    

    
    private Double orderTotalAmount;
    

    
    private Double currentPrice;
    

    
    private Double goldWeight;
    

    
    private String remarks;

    
    private String creteDate;

    



    
    public Myorder convertTo() {
        return  new MyorderDtoConvert().convert(this);
    }

    
    public MyorderDto convertFrom(Myorder myorder) {
        return new MyorderDtoConvert().reverse().convert(myorder);

    }


    private static class MyorderDtoConvert extends  Converter<MyorderDto, Myorder> {


        @Override
        protected Myorder doForward(MyorderDto myorderDto) {
            Myorder myorder = new Myorder();
            BeanUtils.copyProperties(myorderDto, myorder);
            return myorder;
        }

        @Override
        protected MyorderDto doBackward(Myorder myorder) {
            MyorderDto myorderDto = new MyorderDto();
            BeanUtils.copyProperties(myorder, myorderDto);
            if (Objects.nonNull(myorder.getCreateDate())) {
                myorderDto.setCreteDate(DateUtil.toString(myorder.getCreateDate(), DateUtil.YYYY_MM_DD));
            }
            return myorderDto;
        }
    }
}
