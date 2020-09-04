package cn.yuanyu.dtxmsgrocketmq.construction.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountChangeEvent implements Serializable {
    /**
     * 转账人
     */
    private String accountFromName;

    /**
     * 收款人
     */
    private String accountToName;

    /**
     * 变动金额
     */
    private double amount;
    /**
     * 事务号
     */
    private String txNo;

    public AccountChangeEvent() {
    }

    public AccountChangeEvent(String accountFromName, String accountToName, double amount, String txNo) {
        this.accountFromName = accountFromName;
        this.accountToName = accountToName;
        this.amount = amount;
        this.txNo = txNo;
    }
}
