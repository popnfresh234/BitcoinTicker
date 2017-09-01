package com.dmtaiwan.alexander.bitcointicker.model;

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
        return EUR;
    }

    public void setEUR (String EUR)
    {
        this.EUR = EUR;
    }

    public String getUSD ()
    {
        return USD;
    }

    public void setUSD (String USD)
    {
        this.USD = USD;
    }

    public String getBTC ()
    {
        return BTC;
    }

    public void setBTC (String BTC)
    {
        this.BTC = BTC;
    }

    public String getCAD ()
    {
        return CAD;
    }

    public void setCAD (String CAD)
    {
        this.CAD = CAD;
    }
}
