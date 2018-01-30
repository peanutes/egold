package com.zfhy.egold.domain.fund.dto;

import java.util.Objects;


public enum FundOperateType {
    RECHARGE("1", "充值"),
    WITHDRAW("2", "提现"),
    BUYGOLD("3", "买黄金"),
    SELLGOLD("4", "卖黄金"),
    ORDER_EXPIRED("5", "订单过期"),
    TERM_EXPIRED("6", "定期金到期"),
    TERM_SWITCH_INTO("7", "定期金转入"),
    WITHDRAW_GOLD("8", "提金"),
    INVITER_CASH_AWARD("9", "邀请人现金奖励"),
    FIRST_INVEST_CASH_AWARD("10", "首次投邀请人现金奖励资现金奖励"),
    INTEREST_INCOME("11", "利息收入"),
    RISE_FALL_EXPIRED("12", "看涨跌到期"),
    RISE_FALL_INVEST("13", "投资看涨跌产品"),
    ;



    private String code;
    private String label;

    FundOperateType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static String getLabelByCode(String code) {

        for (FundOperateType operateType : FundOperateType.values()) {
            if (Objects.equals(operateType.getCode(), code)) {
                return operateType.getLabel();
            }
        }
        return "";
    }
    }
