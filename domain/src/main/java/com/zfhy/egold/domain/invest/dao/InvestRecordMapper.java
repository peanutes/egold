package com.zfhy.egold.domain.invest.dao;

import com.zfhy.egold.common.core.Mapper;
import com.zfhy.egold.domain.fund.dto.MyInvestDto;
import com.zfhy.egold.domain.invest.entity.InvestRecord;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface InvestRecordMapper extends Mapper<InvestRecord> {

    @Select("select ir.* from invest_invest_record ir, invest_financial_product fp where ir.product_id = fp.id and ir.member_id = #{memberId}  and fp.new_user='1'")
    List<InvestRecord> findNewUserInvestRecord(Integer memberId);

    @Select("select count(1) from invest_invest_record i where i.member_id = #{memberId} and del_flag='0'")
    int countInvest(Integer memberId);

    @Select("select count(1) from invest_invest_record i left join invest_financial_product p on p.id = i.product_id  where p.new_user='1' and i.del_flag='0'")
    int countInvestNewUser();


    @Update("update invest_invest_record set version=version+1 where version=#{param2} and id=#{param1}")
    int updateVersion(Integer id, int version);

    @Update("update invest_invest_record set status=#{param2} where id=#{param1}")
    void updateStatus(Integer id, String statusCode);


    @Update("update invest_invest_record set version=version+1,  status=#{param3}  where version=#{param2} and id=#{param1}")
    int repay(Integer id, int version, String status);

    @Select("select count(1) from invest_invest_record i, invest_financial_product p   where i.product_id = p.id and  i.member_id=#{param1} and p.product_type='TERM_DEPOSIT' and p.new_user <> '1' and p.term>=30 and i.del_flag='0' ")
    int investCount(Integer memberId);

    @Select("select sum(i.invest_amount) from invest_invest_record i where i.member_id=#{param1} and status=0 and product_type='RISE_FALL'")
    Double onInvestAmount(Integer memberId);

    @Select("select sum(i.invest_amount) from invest_invest_record i where i.member_id=#{param1} and i.product_type='RISE_FALL'")
    Double sumInvestAmount(Integer memberId);

    @Select("select ir.product_id productId,ir.product_name productName, ir.invest_amount investAmount, (select IFNULL(sum(income), 0)  from invest_income_detail i where i.invest_id=ir.id) income from invest_invest_record ir where ir.member_id=#{param1} and ir.status=#{param2} and ir.product_type='RISE_FALL'")
    List<MyInvestDto> myInvest(Integer memberId, String investStatus);

    List<InvestRecord> findDeadLineInvestList();

    @Update("update invest_invest_record set version=version+1 end_price=#{param3} where version=#{param2} and id=#{param1}")
    void updateEndPrice(Integer id, int version, Double realTimePrice);

    @Select("select sum(i.invest_gold_weight) from invest_invest_record i where i.member_id=#{param1} and i.estimate_fall=1 and i.product_type='CURRENT_DEPOSIT'")
    Double findCurrentGoldBalanceFall(Integer memberId);

    @Select("select sum(price*invest_gold_weight)/sum(invest_gold_weight) from invest_invest_record i where i.member_id=#{param1} and i.estimate_fall=#{param2} and status='0' and i.product_type='CURRENT_DEPOSIT'")
    Double queryCurrentGoldAvgPrice(Integer memberId, int estimateFall);

    @Select("select sum(price*invest_gold_weight)/sum(invest_gold_weight) from invest_invest_record i where i.member_id=#{param1}  and status='0' and i.product_type='TERM_DEPOSIT'")
    Double queryTermGoldAvgPrice(Integer memberId);

    @Select("select sum(price*invest_gold_weight)/sum(invest_gold_weight) from invest_invest_record i where i.member_id=#{param1}  and status='0' and i.product_type='CURRENT_DEPOSIT'")
    Double queryAllCurrentGoldAvgPrice(Integer memberId);
}
