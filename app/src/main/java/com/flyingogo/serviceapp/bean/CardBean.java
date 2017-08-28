package com.flyingogo.serviceapp.bean;

import java.io.Serializable;

/**
 * 作者：dfy on 21/8/2017 17:55
 * <p> 卡信息
 * 邮箱：dengfuyao@163.com
 */

public class CardBean implements Serializable{
    /**
     * id : 633
     * cardNo : 5970100476
     * cardType : 3
     * cardDeposit : 300.0
     * cardBalance : 42.14
     * accountId : 13859533553
     * issuer : admin
     * cardState : 0
     * activeTime : 2017-04-08 17:40:05
     * createDate : 2017-04-08 17:40:05
     * creator : admin
     * projectId : 1
     * lastFlag : 0
     * cardCid : 06F919FB
     */

    public DataBean data;
    /**
     * data : {"id":633,"cardNo":"5970100476","cardType":"3","cardDeposit":300,"cardBalance":42.14,
     * "accountId":"13859533553","issuer":"admin","cardState":"0","activeTime":"2017-04-08 17:40:05",
     * "createDate":"2017-04-08 17:40:05","creator":"admin","projectId":1,"lastFlag":"0","cardCid":"06F919FB"}
     * state : 1
     */

    public int      state;

    public  class DataBean implements Serializable{
        public int    id;
        public String cardNo;
        public String cardType;
        public double cardDeposit;
        public double cardBalance;
        public String accountId;
        public String issuer;
        public int cardState;
        public String activeTime;
        public String createDate;
        public String creator;
        public int    projectId;
        public String lastFlag;
        public String realName;   //用户名
        public String mobilePhone;  //手机号码
        public String cardCid;
        public String idCard;  //身份号码
    }

    /**
     * id : 4363
     * cardNo : 3513912124
     * cardDeposit : 0
     * cardBalance : 0
     * issuer : admin
     * cardState : 0
     * activeTime : 2017-08-21 17:19:12
     * createDate : 2017-08-21 17:19:12
     * creator : 025566999
     * projectId : -1
     * lastFlag : 1
     * cardCid : 8695kdf
     * realName : zhangsan
     * mobilePhone : 13682597885
     * idCard : 511602198756232654
     */

   /* public DataBean data;
    *//**
     * data : {"id":4363,"cardNo":"3513912124","cardDeposit":0,"cardBalance":0,"issuer":"admin","cardState":"0","activeTime":"2017-08-21 17:19:12","createDate":"2017-08-21 17:19:12","creator":"025566999","projectId":-1,"lastFlag":"1","cardCid":"8695kdf","realName":"zhangsan","mobilePhone":"13682597885","idCard":"511602198756232654"}
     * state : 1
     *//*

    public int      state;

    public static class DataBean {
        public int    id;
        public String cardNo;
        public int    cardDeposit;  //押金
        public int    cardBalance;  //余额
        public String issuer;      //用户
        public String cardState;     //状态
        public String activeTime;
        public String createDate;
        public String creator;   //
        public int    projectId;
        public String lastFlag;
        public String cardCid;   //芯片号
        public String realName;   //用户名
        public String mobilePhone;  //手机号码
        public String idCard;  //身份号码
    }*/
}
