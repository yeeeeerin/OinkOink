package com.example.jihyun.oingoing;

import io.realm.RealmObject;

/**
 * Created by Leeyerin on 2017. 7. 23..
 */

public class DailyMoneyModel extends RealmObject{
    private String AimName;
    private int money_set = 0; //일일설정액
    private String startDate;
    private String endDate;


    public String getAimName(){return  AimName;}
    public  void setAimName(String AimName){this.AimName = AimName;}

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getstartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public int getMoney_set() { return money_set; }
    public void setMoney_set(int money_set) { this.money_set = money_set; }
}
