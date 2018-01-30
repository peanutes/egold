package com.zfhy.egold.domain.invest.service;

import com.zfhy.egold.common.core.Service;
import com.zfhy.egold.domain.invest.dto.BuyGoldDto;
import com.zfhy.egold.domain.invest.dto.HomeFinancialProductDto;
import com.zfhy.egold.domain.invest.dto.PcHomeDto;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;

import java.util.List;



public interface FinancialProductService  extends Service<FinancialProduct> {

    HomeFinancialProductDto homeProducts();

    List<FinancialProduct> findAllProduct();

    void batchDelete(List<Integer> integers);

    BuyGoldDto productList();

    FinancialProduct findCurrentProduct();

    FinancialProduct findNewUserProduct();

    void updateInvestRiseFall(Integer id, Integer version, Double hadInvestAmount, int investStatus);

    PcHomeDto pcHome();

}
