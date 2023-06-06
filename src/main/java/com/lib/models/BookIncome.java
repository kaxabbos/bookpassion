package com.lib.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BookIncome {

    @Id
    long bookid;
    private long userid;
    private String bookname;
    private float income, price;
    private int count;

    public BookIncome() {
    }

    public BookIncome(String bookname, float price) {
        this.bookname = bookname;
        this.price = price;
        this.income = 0;
        this.count = 0;
    }

    public long getBookid() {
        return bookid;
    }

    public void setBookid(long bookid) {
        this.bookid = bookid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
