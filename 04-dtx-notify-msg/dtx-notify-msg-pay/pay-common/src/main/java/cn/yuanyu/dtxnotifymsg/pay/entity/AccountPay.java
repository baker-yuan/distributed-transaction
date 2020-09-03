package cn.yuanyu.dtxnotifymsg.pay.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountPay implements Serializable {

    /**
     * 事务号
     */
    private String id;

    /**
     * 账户名
     */
    private String accountName;

    /**
     * 变动金额
     */
    private double payAmount;
    /**
     * 充值结果
     */
    private String result;

    public AccountPay() {
    }

    public AccountPay(String id, String accountNo, double payAmount, String result) {
        this.id = id;
        this.accountName = accountNo;
        this.payAmount = payAmount;
        this.result = result;
    }
}
