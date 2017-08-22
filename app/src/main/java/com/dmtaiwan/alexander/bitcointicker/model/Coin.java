package com.dmtaiwan.alexander.bitcointicker.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexander on 8/20/2017.
 */

public class Coin
{

    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String price_usd;
    private String price_btc;

    @SerializedName("24h_volume_usd")
    private String twenty_four_hour_volume_usd;
    private String market_cap_usd;
    private String available_supply;
    private String total_supply;

    @SerializedName("percent_change_1h")
    private String percent_change_one_hour;

    @SerializedName("percent_change_24h")
    private String percent_change_twenty_four_hour;

    @SerializedName("percent_change_7d")
    private String percent_change_seven_days;

    private String last_updated;
    private String price_cad;

    @SerializedName("24h_volume_cad")
    private String twenty_four_hour_volume_cad;
    private String market_cap_cad;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public String getPrice_btc() {
        return price_btc;
    }

    public void setPrice_btc(String price_btc) {
        this.price_btc = price_btc;
    }

    public String getTwenty_four_hour_volume_usd() {
        return twenty_four_hour_volume_usd;
    }

    public void setTwenty_four_hour_volume_usd(String twenty_four_hour_volume_usd) {
        this.twenty_four_hour_volume_usd = twenty_four_hour_volume_usd;
    }

    public String getMarket_cap_usd() {
        return market_cap_usd;
    }

    public void setMarket_cap_usd(String market_cap_usd) {
        this.market_cap_usd = market_cap_usd;
    }

    public String getAvailable_supply() {
        return available_supply;
    }

    public void setAvailable_supply(String available_supply) {
        this.available_supply = available_supply;
    }

    public String getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(String total_supply) {
        this.total_supply = total_supply;
    }

    public String getPercent_change_one_hour() {
        return percent_change_one_hour;
    }

    public void setPercent_change_one_hour(String percent_change_one_hour) {
        this.percent_change_one_hour = percent_change_one_hour;
    }

    public String getPercent_change_twenty_four_hour() {
        return percent_change_twenty_four_hour;
    }

    public void setPercent_change_twenty_four_hour(String percent_change_twenty_four_hour) {
        this.percent_change_twenty_four_hour = percent_change_twenty_four_hour;
    }

    public String getPercent_change_seven_days() {
        return percent_change_seven_days;
    }

    public void setPercent_change_seven_days(String percent_change_seven_days) {
        this.percent_change_seven_days = percent_change_seven_days;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getPrice_cad() {
        return price_cad;
    }

    public void setPrice_cad(String price_cad) {
        this.price_cad = price_cad;
    }

    public String getTwenty_four_hour_volume_cad() {
        return twenty_four_hour_volume_cad;
    }

    public void setTwenty_four_hour_volume_cad(String twenty_four_hour_volume_cad) {
        this.twenty_four_hour_volume_cad = twenty_four_hour_volume_cad;
    }

    public String getMarket_cap_cad() {
        return market_cap_cad;
    }

    public void setMarket_cap_cad(String market_cap_cad) {
        this.market_cap_cad = market_cap_cad;
    }

    @Override
    public String toString() {
        return "Coin{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", rank='" + rank + '\'' +
                ", price_usd='" + price_usd + '\'' +
                ", price_btc='" + price_btc + '\'' +
                ", twenty_four_hour_volume_usd='" + twenty_four_hour_volume_usd + '\'' +
                ", market_cap_usd='" + market_cap_usd + '\'' +
                ", available_supply='" + available_supply + '\'' +
                ", total_supply='" + total_supply + '\'' +
                ", percent_change_one_hour='" + percent_change_one_hour + '\'' +
                ", percent_change_twenty_four_hour='" + percent_change_twenty_four_hour + '\'' +
                ", percent_change_seven_days='" + percent_change_seven_days + '\'' +
                ", last_updated='" + last_updated + '\'' +
                ", price_cad='" + price_cad + '\'' +
                ", twenty_four_hour_volume_cad='" + twenty_four_hour_volume_cad + '\'' +
                ", market_cap_cad='" + market_cap_cad + '\'' +
                '}';
    }
}
