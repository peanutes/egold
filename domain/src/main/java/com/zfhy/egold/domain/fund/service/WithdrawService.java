package com.zfhy.egold.domain.fund.service;

import com.zfhy.egold.domain.fund.entity.Withdraw;
import com.zfhy.egold.common.core.Service;



public interface WithdrawService  extends Service<Withdraw> {



    Withdraw confirmWithdraw(Integer memberId, Double withdrawAmount, String dealPwd);

    void updateAndReleaseAmount(Withdraw withdraw);



    Double findOnWithdraw(Integer memberId);
}
