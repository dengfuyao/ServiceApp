package com.flyingogo.serviceapp.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 作者：dfy on 21/8/2017 18:47
 * <p> 通过身份查询的卡列表
 * 邮箱：dengfuyao@163.com
 */

public class ListCardBean  implements Serializable{

    /**
     * data : [{"id":4363,"cardNo":"3513912124","cardDeposit":0,"cardBalance":0,"issuer":"admin","cardState":"0","activeTime":"2017-08-21 17:19:12","createDate":"2017-08-21 17:19:12","creator":"025566999","projectId":-1,"lastFlag":"1","cardCid":"8695kdf","realName":"zhangsan","mobilePhone":"13682597885","idCard":"511602198756232654"},{"id":4364,"cardNo":"3513918994","cardDeposit":0,"cardBalance":0,"issuer":"admin","cardState":"0","activeTime":"2017-08-21 17:26:10","createDate":"2017-08-21 17:26:10","creator":"025566999","projectId":-1,"lastFlag":"1","cardCid":"8695kcf","realName":"zhangsan","mobilePhone":"13682597885","idCard":"511602198756232654"},{"id":4365,"cardNo":"3513983593","cardDeposit":0,"cardBalance":0,"issuer":"admin","cardState":"0","activeTime":"2017-08-21 18:31:52","createDate":"2017-08-21 18:31:52","creator":"025566999","projectId":-1,"lastFlag":"1","cardCid":"8694kdf","realName":"zhangsan","mobilePhone":"13682597885","idCard":"511602198756232654"}]
     * state : 1
     */

    public int            state;
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

    public List<DataBean> data;

    public class DataBean extends CardBean implements Serializable {
        public int        id;
        public String     cardNo;
        public BigDecimal cardDeposit;
        public BigDecimal cardBalance;
        public String     issuer;
        public int     cardState;
        public String     activeTime;
        public String     createDate;
        public String     creator;
        public int        projectId;
        public String     lastFlag;
        public String     cardCid;
        public String     realName;
        public String     mobilePhone;
        public String     idCard;
    }
}
