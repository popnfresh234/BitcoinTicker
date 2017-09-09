package com.dmtaiwan.alexander.bitcointicker.model;

import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;

import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.BTC_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.CAD_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.EUR_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.USD_STRING;

/**
 * Created by Alexander on 9/1/2017.
 */

public class Price {

    private String EUR;
    private String USD;
    private String BTC;
    private String CAD;

    public String getEUR ()
    {
        if (!Utils.isEmpty(EUR)) {
            return Utils.formatCurrency(EUR, SettingsActivity.EUR) + " " + EUR_STRING;
        } else return "No EUR Price";
    }

    public void setEUR (String EUR)
    {
        this.EUR = EUR;
    }

    public String getUSD ()
    {
        if (!Utils.isEmpty(USD)) {
            return Utils.formatCurrency(USD, SettingsActivity.USD) + " " + USD_STRING;
        } else return "No USD Price";

    }

    public void setUSD (String USD)
    {
        this.USD = USD;
    }

    public String getBTC ()
    {
        if (!Utils.isEmpty(BTC)) {
            return BTC + " " + BTC_STRING;
        } else return "No BTC Price";
    }

    public void setBTC (String BTC)
    {
        this.BTC = BTC;
    }

    public String getCAD ()
    {
        if (!Utils.isEmpty(CAD)) {
            return Utils.formatCurrency(CAD, SettingsActivity.CAD) + " " + CAD_STRING;
        } else return "No CAD Price";
    }

    public void setCAD (String CAD)
    {
        this.CAD = CAD;
    }
}
