package com.flyingogo.serviceapp.utils;

/**
 * 作者：dfy on 21/8/2017 11:05
 * <p>
 * 邮箱：dengfuyao@163.com
 */

public class URLUtils {
   private static String baseUrl = "http://123.207.25.39:8280/fjbike/";
  // private static String baseUrl = "http://192.168.31.197:8081/fjbike/";  //内网
    /**
     * 获取要充值押金金额
     * http://123.207.25.39:8280/fjbike/queryPayDepositAmount.do
     * @return
     */
    public static String getPayDepositAmountUrl(){
        return
                baseUrl+"queryPayDepositAmount.do";
    }

   // http://123.207.25.39:8280/fjbike/queryCardInfoByCardNo.do?cardCid=8695kdf

    /**
     * 查询卡信息;
     * @param cardId
     * @return
     */

    public static String getCardInfoUrl(String cardId){
        return baseUrl+"queryCardInfoByCardNo.do?cardCid="+cardId;
    }


    //http://123.207.25.39:8280/fjbike/rechargeDepositByCardCid.do?amount=0.01&payType=2&cardCid=4FD0CA8D
  //充值押金;
    public static String getRechargeDeposit(double amount,int payType, String cardCid){
        return baseUrl+"rechargeDepositByCardCid.do?amount="+amount
                +"&payType="+payType
                +"&cardCid="+cardCid;
    }


// http://123.207.25.39:8280/fjbike/queryRechargeRecord.do?outTradeNo=201708211641063513874701333504
    /**
     * 获取充值结果;
     * @param outTradeNo  订单号;

     * @return
     */

    public static String getRechargeResultUrl(String outTradeNo) {
        return  baseUrl +"queryRechargeRecord.do?outTradeNo="+outTradeNo;
    }


   // http://123.207.25.39:8280/fjbike/queryRechargeRecord.do?outTradeNo=201708211641063513874701333504

   // http://123.207.25.39:8280/fjbike/signedCardInfo.do?
    // cardCid=8695kdf&idCard=511602198756232654&mobilePhone=13682597885&realName=zhangsan

    /**
     * 开卡
     * 签约(-10002 参数异常 -1000 并发访问  -1000 卡片已经签约  -1001 客服人员不存在 -1002 客服人员状态异常  0 签约成功)
     http://123.207.25.39:8280/fjbike/signedCardInfo.do?cardCid=8695kdf&idCard=511602198756232654&mobilePhone=13682597885&realName=zhangsan

     */
    /**
     *
     * @param cardId  芯片ID
     * @param userId  身份号
     * @param phone    电话
     * @param userName  名称
     * @return
     */
    public static String getSignedCardInfoUrl(String cardId, String userId ,String  phone,String userName){
        return baseUrl+"signedCardInfo.do?cardCid="+cardId
                +"&idCard="+userId
                +"&mobilePhone="+ phone
                +"&realName="+userName;
    }

    /**
     *
     * http://123.207.25.39:8280/fjbike/queryCardList.do?idCard=511602198756232654
     * 查询卡列表
     *
     * @param userId
     * @return
     */
    public static String getCardListUrl(String userId){
        return baseUrl+"queryCardList.do?idCard="+userId;
    }

    /**
     * 注销卡
     * @param cardId 芯片号
     * @param userId  省份证
     * @return  注销URL
     */
//http://123.207.25.39:8280/fjbike/terminationCardInfo.do?cardCid=8695kdf&idCard=511602198756232654
    public static String getTerminaCardUrl(String cardId ,String userId){
        return baseUrl+"terminationCardInfo.do?cardCid="+cardId
                +"&idCard="+userId;
    }

   // http://123.207.25.39:8280/fjbike/reportedLossCardInfo.do?id=4363&idCard=511602198756232654

    /**
     *
     * @param id  开卡的ID号
     * @param userId  ,身份信息
     * @return   挂失URL;
     */
    public static String getReportedLossCardUrl(int id, String userId){
        return baseUrl+"reportedLossCardInfo.do?id="+id
                +"&idCard="+userId;
    }
    //http://123.207.25.39:8280/fjbike/solutionToHangCardInfo.do?id=4363&idCard=511602198756232654
   // http://123.207.25.39:8280/fjbike/solutionToHangCardInfo.do?id=4363&idCard=511602198756232654

    /**
     *
     * @param id  开卡的ID号
     * @param userId  ,身份信息
     * @return   解挂URL;
     */
    public static String getSolutionToHangCardUrl(int id, String userId){
        return baseUrl+"solutionToHangCardInfo.do?id="+id
                +"&idCard="+userId;
    }
//http://localhost:8280/fjbike/rechargeBalanceByCardId.do?amount=0.01&payType=1&cardCid=4FDCA547

    /**
     * 充值余额
     * @param amount  金额
     * @param payType
     * @param cardId
     * @return
     */
    public static String getRechargeBalanceUrl(double amount, int payType, String cardId) {
        return baseUrl+"rechargeBalanceByCardId.do?amount="+amount
                +"&payType="+payType
                +"&cardCid="+cardId;
    }

   // 解约(-10002 参数异常  -1001 客服人员不存在 -1002 客服人员状态异常
    // -99 卡片不存在 -101 卡片已经解约 -102 身份证号输入错误  -106 解约失败 1 成功)
   // http://123.207.25.39:8280/fjbike/terminationCardInfo.do?cardCid=8695kdf&idCard=511602198756232654

    public static String getTerminationCardUrl(String cardCid ,String userId){
        return baseUrl+"terminationCardInfo.do?cardCid="+cardCid
                +"&idCard="+userId;
    }

}
