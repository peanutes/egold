package com.zfhy.egold.domain.invest.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.invest.dto.PcHomeNewProductDto;
import com.zfhy.egold.domain.invest.dto.PcTermProductDto;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface FinancialProductMapper extends Mapper<FinancialProduct> {
    @Select("select * from invest_financial_product p where p.product_type=#{productType} and status = '1' and new_user = '0' and del_flag='0' order by sort limit 0,1")
    FinancialProduct findTopProductByProductType(String productType);

    @Select("select * from invest_financial_product p where p.status = '1' and new_user = '1' and del_flag='0' order by sort limit 0,1")
    FinancialProduct findNewUserProduct();

    void batchDelete(List<Integer> idList);

    @Select("select * from invest_financial_product p where p.product_type=#{productType} and status = '1' and new_user = '0' and del_flag='0' order by sort ")
    List<FinancialProduct> findProductsByProductType(String productType);

    @Update("update invest_financial_product set had_invest_amount=#{param3}, invest_status=#{param4}, version=version+1 where id=#{param1} and version=#{param2} ")
    int updateInvestRiseFall(Integer id, Integer version, Double hadInvestAmount, int investStatus);

    @Select("select * from invest_financial_product p where p.product_type=#{productType} and status = '1' and dead_line_date > now() and new_user = '0' and del_flag='0' order by sort limit 0,1")
    FinancialProduct findTopRiseFallProduct(String productType);

    @Select("select * from invest_financial_product p where p.product_type=#{productType} and status = '1' and dead_line_date > now() and new_user = '0' and del_flag='0' order by sort ")
    List<FinancialProduct> findRiseFallProduct(String productType);

    @Select("select p.annual_rate annualRate, p.term, (select sum(i.invest_gold_weight) from invest_invest_record i where i.product_id=p.id) sumInvest from invest_financial_product p where p.product_type=#{productType} and status = '1' and new_user = '0' and del_flag='0' order by sort limit 0,2")
    List<PcTermProductDto> findTop2ProductByProductType(String productType);

    @Select("select p.price, p.term  from invest_financial_product p where p.status = '1' and p.new_user = '1' and p.del_flag='0' order by sort limit 0,1")
    PcHomeNewProductDto findPcHomeNewUserProduct();

}
