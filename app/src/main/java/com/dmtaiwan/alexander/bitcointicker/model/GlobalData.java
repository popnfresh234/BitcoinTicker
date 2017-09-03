package com.dmtaiwan.alexander.bitcointicker.model;

/**
 * Created by Alexander on 9/2/2017.
 */

public class GlobalData {
    private String active_assets;

    private String total_market_cap_usd;

    private String total_24h_volume_cad;

    private String total_market_cap_cad;

    private String bitcoin_percentage_of_market_cap;

    private String total_market_cap_eur;

    private String active_currencies;

    private String total_24h_volume_usd;

    private String total_24h_volume_eur;

    private String active_markets;

    public String getActive_assets ()
    {
        return active_assets;
    }

    public void setActive_assets (String active_assets)
    {
        this.active_assets = active_assets;
    }

    public String getTotal_market_cap_usd ()
    {
        return total_market_cap_usd;
    }

    public void setTotal_market_cap_usd (String total_market_cap_usd)
    {
        this.total_market_cap_usd = total_market_cap_usd;
    }

    public String getTotal_24h_volume_cad ()
    {
        return total_24h_volume_cad;
    }

    public void setTotal_24h_volume_cad (String total_24h_volume_cad)
    {
        this.total_24h_volume_cad = total_24h_volume_cad;
    }

    public String getTotal_market_cap_cad ()
    {
        return total_market_cap_cad;
    }

    public void setTotal_market_cap_cad (String total_market_cap_cad)
    {
        this.total_market_cap_cad = total_market_cap_cad;
    }

    public String getBitcoin_percentage_of_market_cap ()
    {
        return bitcoin_percentage_of_market_cap;
    }

    public void setBitcoin_percentage_of_market_cap (String bitcoin_percentage_of_market_cap)
    {
        this.bitcoin_percentage_of_market_cap = bitcoin_percentage_of_market_cap;
    }

    public String getTotal_market_cap_eur ()
    {
        return total_market_cap_eur;
    }

    public void setTotal_market_cap_eur (String total_market_cap_eur)
    {
        this.total_market_cap_eur = total_market_cap_eur;
    }

    public String getActive_currencies ()
    {
        return active_currencies;
    }

    public void setActive_currencies (String active_currencies)
    {
        this.active_currencies = active_currencies;
    }

    public String getTotal_24h_volume_usd ()
    {
        return total_24h_volume_usd;
    }

    public void setTotal_24h_volume_usd (String total_24h_volume_usd)
    {
        this.total_24h_volume_usd = total_24h_volume_usd;
    }

    public String getTotal_24h_volume_eur ()
    {
        return total_24h_volume_eur;
    }

    public void setTotal_24h_volume_eur (String total_24h_volume_eur)
    {
        this.total_24h_volume_eur = total_24h_volume_eur;
    }

    public String getActive_markets ()
    {
        return active_markets;
    }

    public void setActive_markets (String active_markets)
    {
        this.active_markets = active_markets;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [active_assets = "+active_assets+", total_market_cap_usd = "+total_market_cap_usd+", total_24h_volume_cad = "+total_24h_volume_cad+", total_market_cap_cad = "+total_market_cap_cad+", bitcoin_percentage_of_market_cap = "+bitcoin_percentage_of_market_cap+", total_market_cap_eur = "+total_market_cap_eur+", active_currencies = "+active_currencies+", total_24h_volume_usd = "+total_24h_volume_usd+", total_24h_volume_eur = "+total_24h_volume_eur+", active_markets = "+active_markets+"]";
    }
}


