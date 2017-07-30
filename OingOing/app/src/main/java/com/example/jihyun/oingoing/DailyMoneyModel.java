package com.example.jihyun.oingoing;

import io.realm.RealmObject;
import java.util.Date;

/**
 * Created by Leeyerin on 2017. 7. 23..
 */

public class DailyMoneyModel extends RealmObject{
    private String AimName;
    private int money_set = 0; //일일설정액
    private Date startDate;
    private Date endDate;


    public String getAimName(){return  AimName;}
    public  void setAimName(String AimName){this.AimName = AimName;}

    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getstartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getMoney_set() { return money_set; }
    public void setMoney_set(int money_set) { this.money_set = money_set; }
}
