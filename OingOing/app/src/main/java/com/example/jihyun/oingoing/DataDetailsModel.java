package com.example.jihyun.oingoing;

import android.util.Log;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jihyun on 2017-05-04.
 */

public class DataDetailsModel extends RealmObject {

    @PrimaryKey
    private int id;
    private String name;
    private int price;
    private String date;
    private int money_set = 0; //일일설정액
    private boolean InOrOut = false; //false 수입 true 지출



    public boolean isInOrOut() { return InOrOut; }
    public void setInOrOut(boolean inOrOut) { InOrOut = inOrOut; }

    public int getMoney_set() { return money_set; }
    public void setMoney_set(int money_set) { this.money_set = money_set; }

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) { this.date = date; }


}