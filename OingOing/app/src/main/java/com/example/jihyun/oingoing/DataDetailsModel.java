package com.example.jihyun.oingoing;

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
}
