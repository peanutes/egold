package com.zfhy.egold.domain.invest.service.impl;

import com.zfhy.egold.common.config.cache.CacheDuration;
import com.zfhy.egold.common.core.AbstractService;
import com.zfhy.egold.common.exception.BusException;
import com.zfhy.egold.domain.cms.dto.PcHomeHotInfoDto;
import com.zfhy.egold.domain.cms.service.HotInfoService;
import com.zfhy.egold.domain.goods.dto.PcHomeSkuDto;
import com.zfhy.egold.domain.goods.service.SkuService;
import com.zfhy.egold.domain.invest.dao.FinancialProductMapper;
import com.zfhy.egold.domain.invest.dto.*;
import com.zfhy.egold.domain.invest.entity.FinancialProduct;
import com.zfhy.egold.domain.invest.service.FinancialProductService;
import com.zfhy.egold.domain.invest.service.InvestRecordService;
import com.zfhy.egold.domain.redis.service.RedisService;
import com.zfhy.egold.domain.sys.dto.DictType;
import com.zfhy.egold.domain.sys.entity.Dict;
import com.zfhy.egold.domain.sys.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;
import java.util.stream.Collectors;



@Service
@Transactional
@Slf4j
public class FinancialProductServiceImpl extends AbstractService<FinancialProduct> implements FinancialProductService {
    @Autowired
    private FinancialProductMapper financialProductMapper;

    @Autowired
    private DictService dictService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private InvestRecordService investRecordService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private HotInfoService hotInfoService;

    @Cacheable("FinancialProductServiceImpl_homeProducts")
    @CacheDuration(duration = 300L)
    @Transactional(readOnly = true)
    @Override
    public HomeFinancialProductDto homeProducts() {

        FinancialProduct currentDepositProduct = this.financialProductMapper.findTopProductByProductType(FinancialProductType.CURRENT_DEPOSIT.name());
        FinancialProduct termDepositProduct = this.financialProductMapper.findTopProductByProductType(FinancialProductType.TERM_DEPOSIT.name());
        FinancialProduct riseAndFallDepositProduct = this.financialProductMapper.findTopRiseFallProduct(FinancialProductType.RISE_FALL.name());
        FinancialProduct newUserProduct = this.financialProductMapper.findNewUserProduct();

        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        Double realTimePrice = this.redisService.getRealTimePrice();

        HomeFinancialProductDto homeFinancialProductDto = new HomeFinancialProductDto();




        homeFinancialProductDto.setCurrentDepositProduct(new FinancialProductDto().convertFrom(currentDepositProduct, currentYearPriceRisePercent, realTimePrice));
        homeFinancialProductDto.setTermDepositProduct(new FinancialProductDto().convertFrom(termDepositProduct, currentYearPriceRisePercent, realTimePrice));
        homeFinancialProductDto.setRiseAndFallProduct(new FinancialProductDto().convertFrom(riseAndFallDepositProduct, currentYearPriceRisePercent, realTimePrice));
        homeFinancialProductDto.setNewUserProduct(new FinancialProductDto().convertFrom(newUserProduct, currentYearPriceRisePercent, realTimePrice));


        Dict productDesc = this.dictService.findByType(DictType.PRODUCT_DESC);
        Dict accountAgreement = this.dictService.findByType(DictType.ACCOUNT_AGREEMENT);

        ProductDescDto productDescDto = ProductDescDto.builder()
                .title(productDesc.getLabelName())
                .keyPoint(productDesc.getDes())
                .url(productDesc.getValue())
                .build();


        homeFinancialProductDto.setProductDescDto(productDescDto);

        AccountSecurityAgreementDto accountSecurityAgreementDto = AccountSecurityAgreementDto.builder()
                .title(accountAgreement.getDes())
                .url(accountAgreement.getValue())
                .build();

        homeFinancialProductDto.setAccountSecurityAgreementDto(accountSecurityAgreementDto);

        homeFinancialProductDto.setHadInvestNewUserCount(this.investRecordService.countInvestNewUser());


        return homeFinancialProductDto;

    }

    @Override
    public List<FinancialProduct> findAllProduct() {

        Condition condition = new Condition(FinancialProduct.class);
        condition.createCriteria().andEqualTo("delFlag", "0");

        return super.findByCondition(condition);
    }

    @Override
    public void batchDelete(List<Integer> idList) {

        this.financialProductMapper.batchDelete(idList);
    }

    @Cacheable("FinancialProductServiceImpl_productList")
    @CacheDuration(duration = 120)
    @Override
    public BuyGoldDto productList() {
        
        FinancialProduct currentDepositProduct = this.financialProductMapper.findTopProductByProductType(FinancialProductType.CURRENT_DEPOSIT.name());
        
        FinancialProduct newUserProduct = this.financialProductMapper.findNewUserProduct();


        Double currentYearPriceRisePercent = dictService.findDouble(DictType.CURRENT_YEAR_PRICE_RISE_PERCENT);

        Double realTimePrice = this.redisService.getRealTimePrice();


        
        List<FinancialProduct> termDepositProducts = this.financialProductMapper.findProductsByProductType(FinancialProductType.TERM_DEPOSIT.name());
        
        List<FinancialProduct> riseAndFallDepositProducts = this.financialProductMapper.findRiseFallProduct(FinancialProductType.RISE_FALL.name());

        BuyGoldDto buyGoldDto = new BuyGoldDto();
        buyGoldDto.setCurrentDepositProduct(new FinancialProductDto().convertFrom(currentDepositProduct, currentYearPriceRisePercent, realTimePrice));
        buyGoldDto.setNewUserProduct(new FinancialProductDto().convertFrom(newUserProduct, currentYearPriceRisePercent, realTimePrice));


        List<FinancialProductDto> termProductDtoList = termDepositProducts.stream().map(e -> new FinancialProductDto().convertFrom(e, currentYearPriceRisePercent, realTimePrice)).collect(Collectors.toList());
        List<FinancialProductDto> riseAndFallProductDtoList = riseAndFallDepositProducts.stream().map(e -> new FinancialProductDto().convertFrom(e, currentYearPriceRisePercent, realTimePrice)).collect(Collectors.toList());


        Dict termType = this.dictService.findByType(DictType.PRD_TERM_TYPE);
        TermGoldDto termGoldDto = new TermGoldDto();
        termGoldDto.setTitle(termType.getLabelName());
        termGoldDto.setKeyPoint(termType.getValue());
        termGoldDto.setTermDepositProduct(termProductDtoList);
        buyGoldDto.setTermGoldDto(termGoldDto);


        Dict riseFallType = this.dictService.findByType(DictType.PRD_RISE_TYPE);
        RiseFallGoldDto riseFallGoldDto = new RiseFallGoldDto();
        riseFallGoldDto.setKeyPoint(riseFallType.getValue());
        riseFallGoldDto.setTitle(riseFallType.getLabelName());
        riseFallGoldDto.setRiseAndFallProduct(riseAndFallProductDtoList);

        buyGoldDto.setRiseFallGoldDto(riseFallGoldDto);

        
        Dict accountAgreement = this.dictService.findByType(DictType.ACCOUNT_AGREEMENT);
        AccountSecurityAgreementDto accountSecurityAgreementDto = AccountSecurityAgreementDto.builder()
                .title(accountAgreement.getDes())
                .url(accountAgreement.getValue())
                .build();

        buyGoldDto.setAccountSecurityAgreementDto(accountSecurityAgreementDto);
        return buyGoldDto;
    }

    @Override
    public FinancialProduct findCurrentProduct() {
        return this.financialProductMapper.findTopProductByProductType(FinancialProductType.CURRENT_DEPOSIT.name());
    }

    @Override
    public FinancialProduct findNewUserProduct() {
        return this.financialProductMapper.findNewUserProduct();
    }

    @Override
    public void updateInvestRiseFall(Integer id, Integer version, Double hadInvestAmount, int investStatus) {
        int count = this.financialProductMapper.updateInvestRiseFall(id, version, hadInvestAmount, investStatus);
        if (count != 1) {
            throw new BusException("您好，请重新再试");
        }
    }

    @Cacheable("FinancialProductServiceImpl_pchome")
    @CacheDuration(duration = 300L)
    @Override
    public PcHomeDto pcHome() {
        PcHomeNewProductDto pcHomeNewProductDto = this.financialProductMapper.findPcHomeNewUserProduct();

        List<PcTermProductDto> termDepositProducts = this.financialProductMapper.findTop2ProductByProductType(FinancialProductType.TERM_DEPOSIT.name());

        List<PcHomeSkuDto> pcHomeSkuDtos = this.skuService.pcHomeSkuDto();

        List<PcHomeHotInfoDto> pcHomeHotInfoDtos = this.hotInfoService.findPcHomeHotInfo(ArticleType.HOST_INFO);

        List<PcHomeHotInfoDto> pcHomeHelpDesk = this.hotInfoService.findPcHomeHotInfo(ArticleType.HELP_DESK);




        PcHomeDto pcHomeDto = PcHomeDto.builder()
                .pcHomeNewProductDto(pcHomeNewProductDto)
                .pcTermProductDtos(termDepositProducts)
                .pcHomeSkuDtos(pcHomeSkuDtos)
                .pcHomeHotInfoDtos(pcHomeHotInfoDtos)
                .pcHomeHelpDesk(pcHomeHelpDesk)
                .build();


        return pcHomeDto;
    }

}
