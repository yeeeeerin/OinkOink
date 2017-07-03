package com.example.jihyun.oingoing;

/**
 * Created by samsung on 2017-05-27.
 */

public class accountItem {
    String expenditureItem;
    int account;
    int resid;

    public accountItem(String expenditureItem, int account,int resid) {
        this.expenditureItem = expenditureItem;
        this.account = account;
        this.resid=resid;
    }

    public String getExpenditureItem() {
        return expenditureItem;
    }

    public void setExpenditureItem(String expenditureItem) {
        this.expenditureItem = expenditureItem;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    @Override
    public String toString() {
        return "accountItem{" +
                "expenditureItem='" + expenditureItem + '\'' +
                ", account=" + account +
                '}';
    }
}
