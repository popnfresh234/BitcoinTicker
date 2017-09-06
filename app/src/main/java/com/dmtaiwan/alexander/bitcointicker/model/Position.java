package com.dmtaiwan.alexander.bitcointicker.model;

/**
 * Created by Alexander on 9/6/2017.
 */

public class Position {
    private int id;
    private String symbol;
    private String position;
    private String price;

    public Position(int id, String symbol, String position, String price) {
        this.id = id;
        this.position = position;
        this.price = price;
        this.symbol = symbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
