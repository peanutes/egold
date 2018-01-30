package com.zfhy.egold.domain.sys.dto;

import java.util.Objects;


public enum CouponUseScene {
    /*1：新用户注册，2：邀请好友绑卡-邀请人,
            3：邀请好友绑卡-被新邀请人，4：邀请好友首投-邀请人,
            5：邀请好友全年返利-邀请人，6首投，7.实物奖励*/

    REGISTER(1, "新用户注册"),
    BIND_BANK_CARD_INVESTOR(2, "邀请好友绑卡"),
    BIND_BANK_CARD_SELF(3, "用户绑卡"),
    FIRST_INVEST_INVESTOR(4, "邀请好友首投"),
    INVESTOR_AWARD(5, "邀请好友全年返利"),
    FIRST_INVEST_SELF(6, "首投"),
    PRESENT_AWARD(7, "礼物奖励"),;



    private int code;
    private String label;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }


    public static String getLabelByCode(Integer code) {

        for (CouponUseScene couponUseScene : CouponUseScene.values()) {
            if (Objects.equals(couponUseScene.getCode(), code)) {
                return couponUseScene.getLabel();
            }
        }
        return "";
    }

    CouponUseScene(int code, String label) {
        this.code = code;
        this.label = label;
    }
}
