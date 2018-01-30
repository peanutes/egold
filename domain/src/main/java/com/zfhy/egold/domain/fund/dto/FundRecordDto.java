package com.zfhy.egold.domain.fund.dto;

import com.google.common.base.Converter;
import com.zfhy.egold.domain.fund.entity.FundRecord;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FundRecordDto {


    
    private Integer id;


    
    private Integer memberId;


    
    private Double operateMoney;


    
    private Double goldWeight;


    
    private String productName;

    
    private String account;


    
    private Integer operateId;


    
    private String displayLabel;


    
    private String operateType;


    
    private String remarks;




    
    public FundRecord convertTo() {
        return  new FundRecordDtoConvert().convert(this);
    }

    
    public FundRecordDto convertFrom(FundRecord fundRecord) {
        return new FundRecordDtoConvert().reverse().convert(fundRecord);

    }


    private static class FundRecordDtoConvert extends  Converter<FundRecordDto, FundRecord> {


        @Override
        protected FundRecord doForward(FundRecordDto fundRecordDto) {
            FundRecord fundRecord = new FundRecord();
            BeanUtils.copyProperties(fundRecordDto, fundRecord);
            return fundRecord;
        }

        @Override
        protected FundRecordDto doBackward(FundRecord fundRecord) {
            FundRecordDto fundRecordDto = new FundRecordDto();
            BeanUtils.copyProperties(fundRecord, fundRecordDto);
            return fundRecordDto;
        }
    }

    public static void main(String[] args) {
         String md5_32;
         String md5_16;

        System.out.println("fadfas".hashCode());

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update("121".getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();

            md5_16= buf.toString().substring(8, 24);
            System.out.println("16>>>" + md5_16);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
    }

}
