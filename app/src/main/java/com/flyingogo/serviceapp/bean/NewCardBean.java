package com.flyingogo.serviceapp.bean;

/**
 * 作者：dfy on 21/8/2017 18:32
 * <p> 新开卡
 * 邮箱：dengfuyao@163.com
 */

public class NewCardBean {
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

    public DataBean data;
    /**
     * data : {"id":4363,"cardNo":"3513912124","cardDeposit":0,"cardBalance":0,"issuer":"admin","cardState":"0","activeTime":"2017-08-21 17:19:12","createDate":"2017-08-21 17:19:12","creator":"025566999","projectId":-1,"lastFlag":"1","cardCid":"8695kdf","realName":"zhangsan","mobilePhone":"13682597885","idCard":"511602198756232654"}
     * state : 1
     */

    public int      state;

    public static class DataBean {
        public int    id;
        public String cardNo;
        public int    cardDeposit;
        public int    cardBalance;
        public String issuer;
        public String cardState;
        public String activeTime;
        public String createDate;
        public String creator;
        public int    projectId;
        public String lastFlag;
        public String cardCid;
        public String realName;
        public String mobilePhone;
        public String idCard;
    }
}
