package com.zfhy.egold.domain.invest.dto;

import com.google.common.base.Converter;
import com.google.common.collect.Lists;
import com.zfhy.egold.common.util.DateUtil;
import com.zfhy.egold.common.util.DoubleUtil;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FinancialProductDto {

    
    private Integer id;


    
    private String productName;


    
    private Double annualRate;


    
    private Double annualRateMax;


    
    private Double minAmount;


    
    private Double maxAmount;


    
    private Double price;


    
    private Double marketPrice;


    
    private Integer term;


    
    private String productType;


    
    private String newUser;


    
    private String goldType;


    
    private Double buyFee;


    
    private Double saleFee;


    
    private String incomeStruct;


    
    private String interestDesc;


    
    private String interestCal;


    
    private String riskGrade;


    
    private String status;


    
    private String keyPoint;


    
    private Integer sort;


    
    private String remarks;

    
    private List<ProductPropertyDto> propertyDtoList;


    
    private Date beginDate;

    
    private Date endDate;



    
    
    private String minAmountDesc;


    
    
    private String buySaleDesc;


    
    
    private String termDesc;


    
    private Double currentYearPriceRisePercent;


    
    
    private Date deadLineDate;


    
    private int riseOrFall;


    
    private Double aimAmount;

    
    private Double hadInvestAmount;


    
    private Double enableInvestAmount;

    
    private String hadInvestPercent;

    
    private Integer investStatus;

    
    private String deadlinePriceStr;


    
    private String closePriceStr;





    
    public FinancialProduct convertTo() {
        return new FinancialProductDtoConvert().convert(this);
    }

    
    public FinancialProductDto convertFrom(FinancialProduct financialProduct, Double currentYearPriceRisePercent, Double realTimePrice) {
        if (Objects.isNull(financialProduct)) {
            return null;
        }
        FinancialProductDto dto = new FinancialProductDtoConvert().reverse().convert(financialProduct);
        dto.setCurrentYearPriceRisePercent(currentYearPriceRisePercent);

        if (!Objects.equals(dto.getNewUser(), "1")) {
            dto.setPrice(realTimePrice);
        }

        if (Objects.equals(financialProduct.getProductType(), FinancialProductType.RISE_FALL.name())) {
            
            if (!Objects.equals(financialProduct.getInvestStatus(), RiseFallInvestStatus.PROCESSING.getCode())) {
                dto.setHadInvestPercent("100%");
                dto.setEnableInvestAmount(0D);

            } else {
                Double hadInvestAmount = financialProduct.getHadInvestAmount();
                hadInvestAmount = hadInvestAmount == null ? 0D : hadInvestAmount;
                if (DoubleUtil.equal(hadInvestAmount, 0D)) {
                    dto.setHadInvestPercent("0%");
                    dto.setEnableInvestAmount(financialProduct.getAimAmount());
                } else {
                    Double percent = DoubleUtil.doubleDiv(hadInvestAmount, financialProduct.getAimAmount(), 4);

                    dto.setHadInvestPercent(String.join("", DoubleUtil.toString(DoubleUtil.doubleMul(percent, 100D, 2)), "%"));
                    dto.setEnableInvestAmount(DoubleUtil.doubleSub(financialProduct.getAimAmount(), hadInvestAmount));

                }
            }
        }

        return dto;

    }


    private static class FinancialProductDtoConvert extends Converter<FinancialProductDto, FinancialProduct> {


        @Override
        protected FinancialProduct doForward(FinancialProductDto financialProductDto) {
            FinancialProduct financialProduct = new FinancialProduct();
            BeanUtils.copyProperties(financialProductDto, financialProduct);
            return financialProduct;
        }

        @Override
        protected FinancialProductDto doBackward(FinancialProduct financialProduct) {
            FinancialProductDto financialProductDto = new FinancialProductDto();
            BeanUtils.copyProperties(financialProduct, financialProductDto);

            financialProductDto.setBeginDate(DateUtil.tomorrow());
            List<ProductPropertyDto> propertyDtoList = Lists.newArrayList();
            String productType = financialProduct.getProductType();

            if (Objects.equals(productType, FinancialProductType.RISE_FALL.name())) {
                
                
                Date interestBeginDate = financialProduct.getInterestBeginDate();
                
                Date interestEndDate = DateUtil.afterDays(interestBeginDate, (financialProduct.getTerm()-1));
                
                Date giveInterestDate = DateUtil.afterDays(interestBeginDate, (financialProduct.getTerm()));

                Double deadlinePrice = financialProduct.getDeadlinePrice();
                Double closePrice = financialProduct.getClosePrice();


                financialProductDto.setDeadlinePriceStr(deadlinePrice == null ? "待更新" : String.join("", DoubleUtil.toString(deadlinePrice), "元/克"));
                financialProductDto.setClosePriceStr(closePrice == null ? "待更新" : String.join("", DoubleUtil.toString(closePrice), "元/克"));


                financialProductDto.setBeginDate(interestBeginDate);
                financialProductDto.setEndDate(interestEndDate);

                propertyDtoList.add(new ProductPropertyDto("产品说明", financialProduct.getRemarks()));
                propertyDtoList.add(new ProductPropertyDto("年化收益", String.format("%s%%~%s%%", DoubleUtil.toString(financialProduct.getAnnualRate()), "12")));
                propertyDtoList.add(new ProductPropertyDto("起购金额", String.format("%s元", financialProduct.getMinAmount())));
                propertyDtoList.add(new ProductPropertyDto("购买截止", DateUtil.toString(financialProduct.getDeadLineDate(), DateUtil.YYYY_MM_DD_HH_MM)));
                propertyDtoList.add(new ProductPropertyDto("计息起始日", DateUtil.toString(interestBeginDate, DateUtil.YYYY_MM_DD)));
                propertyDtoList.add(new ProductPropertyDto("计息结束日", DateUtil.toString(interestEndDate, DateUtil.YYYY_MM_DD)));
                propertyDtoList.add(new ProductPropertyDto("收益发放日", DateUtil.toString(giveInterestDate, DateUtil.YYYY_MM_DD)));
                propertyDtoList.add(new ProductPropertyDto("收益发放方式", financialProduct.getIncomeStruct()));
                propertyDtoList.add(new ProductPropertyDto("投资期限", String.join("", financialProduct.getTerm().toString(), "天")));
                propertyDtoList.add(new ProductPropertyDto("单次购买上限", String.join("", DoubleUtil.toString(financialProduct.getMaxAmount()), "元")));
                propertyDtoList.add(new ProductPropertyDto("到期处理", financialProduct.getExpiredDealDesc()));

            } else {
                propertyDtoList.add(new ProductPropertyDto("黄金品种", financialProduct.getGoldType()));
                propertyDtoList.add(new ProductPropertyDto("买入手续费", String.format("%s元", DoubleUtil.toString(financialProduct.getBuyFee()))));
                propertyDtoList.add(new ProductPropertyDto("卖出手续费", String.format("每笔%s元/克，不满1克按1克收取", Double.toString(financialProduct.getSaleFee()))));
                propertyDtoList.add(new ProductPropertyDto("起购金额", financialProduct.getMinAmountDesc()));
                propertyDtoList.add(new ProductPropertyDto("买卖规则", financialProduct.getBuySaleDesc()));
                propertyDtoList.add(new ProductPropertyDto("收益购成", financialProduct.getIncomeStruct()));
                propertyDtoList.add(new ProductPropertyDto("利息发放", financialProduct.getInterestDesc()));
                propertyDtoList.add(new ProductPropertyDto("利息计算", financialProduct.getInterestCal()));
                propertyDtoList.add(new ProductPropertyDto("投资期限", financialProduct.getTermDesc()));

                if (Objects.equals(productType, FinancialProductType.TERM_DEPOSIT.name())) {
                    
                    propertyDtoList.add(new ProductPropertyDto("到期处理", "到期后自动转为活期金"));
                    financialProductDto.setEndDate(DateUtil.afterDays(financialProduct.getTerm()));
                }
            }





            financialProductDto.setPropertyDtoList(propertyDtoList);

            return financialProductDto;
        }
    }
}
