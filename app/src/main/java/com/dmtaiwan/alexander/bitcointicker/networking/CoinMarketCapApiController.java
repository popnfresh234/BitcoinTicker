package com.dmtaiwan.alexander.bitcointicker.networking;

import android.util.Log;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.model.GlobalData;
import com.dmtaiwan.alexander.bitcointicker.ui.piechart.GlobalDataCallback;
import com.dmtaiwan.alexander.bitcointicker.ui.main.TickerCallback;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexander on 8/20/2017.
 */

public class CoinMarketCapApiController{

    private static final String USD = "usd";
    private static final String CAD = "cad";
    private static final String EUR = "eur";

    private static final String BASE_URL = " https://api.coinmarketcap.com/v1/";
    private TickerCallback tickerListener;
    private GlobalDataCallback globalDataListener;

    public void getTickerData(TickerCallback tickerCallback, String preferredCurrency) {


        this.tickerListener = tickerCallback;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CoinMarketCapApi coinMarketCapApi = retrofit.create(CoinMarketCapApi.class);
        Call<ArrayList<Coin>> call = coinMarketCapApi.getCoins(preferredCurrency);
        call.enqueue(new Callback<ArrayList<Coin>>() {
            @Override
            public void onResponse(Call<ArrayList<Coin>> call, Response<ArrayList<Coin>> response) {
                tickerListener.returnResults(response.body());
            }

            @Override
            public void onFailure(Call<ArrayList<Coin>> call, Throwable t) {
                Log.e("Price failed: ", t.toString());
            }
        });

    }

    public void getGlobalData(GlobalDataCallback globalDataCallback, String preferredCurrency) {
        this.globalDataListener = globalDataCallback;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CoinMarketCapApi coinMarketCapApi = retrofit.create(CoinMarketCapApi.class);
        Call<GlobalData> call = coinMarketCapApi.getGlobalData(preferredCurrency);
        call.enqueue(new Callback<GlobalData>() {
            @Override
            public void onResponse(Call<GlobalData> call, Response<GlobalData> response) {
                globalDataListener.returnGlobalData(response.body());
            }

            @Override
            public void onFailure(Call<GlobalData> call, Throwable t) {
                Log.e("Global Data Failed: ", t.toString());
            }
        });

    }




    public String getPreferredCurrencyString(String preferredCurrency) {
        String currency;
        switch (preferredCurrency) {
            case SettingsActivity.USD:
                currency = USD;
                break;
            case SettingsActivity.CAD:
                currency = CAD;
                break;
            case SettingsActivity.EUR:
                currency = EUR;
                break;
            default:
                currency = USD;
        }
        return currency;
    }
}

//https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=CAD&limit=6