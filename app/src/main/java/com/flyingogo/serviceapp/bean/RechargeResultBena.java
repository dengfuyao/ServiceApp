package com.flyingogo.serviceapp.bean;

/**
 * 作者：dfy on 21/8/2017 18:42
 * <p> 充值结果
 * 邮箱：dengfuyao@163.com
 */

public class RechargeResultBena {

    /**
     * id : 319
     * cardNo : 5970100480
     * rechargeType : 3
     * rechargeAmount : 0.01
     * paymentMode : 2
     * rechargeState : 2
     * rechargeTime : 2017-08-21 16:41:06
     * orderTime : 2017-08-21 16:41:06
     * indentId : 201708211641063513874701333504
     * serviceType : 1
     * alipayBuyerId : 2088812245127853
     */

    public DataBean data;
    /**
     * data : {"id":319,"cardNo":"5970100480","rechargeType":"3","rechargeAmount":0.01,"paymentMode":"2","rechargeState":"2","rechargeTime":"2017-08-21 16:41:06","orderTime":"2017-08-21 16:41:06","indentId":"201708211641063513874701333504","serviceType":1,"alipayBuyerId":"2088812245127853"}
     * state : 1000
     */

    public int      state;

    public static class DataBean {
        public int    id;
        public String cardNo;
        public String rechargeType;
        public double rechargeAmount;
        public String paymentMode;
        public String rechargeState;
        public String rechargeTime;
        public String orderTime;
        public String indentId;
        public int    serviceType;
        public String alipayBuyerId;
    }
}
