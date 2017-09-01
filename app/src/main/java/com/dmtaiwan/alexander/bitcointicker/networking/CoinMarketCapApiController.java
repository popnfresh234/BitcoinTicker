package com.dmtaiwan.alexander.bitcointicker.networking;

import android.util.Log;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.ui.CoinMarketCapCallbackInterface;
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

public class CoinMarketCapApiController implements Callback<ArrayList<Coin>> {

    private static final String BASE_URL = " https://api.coinmarketcap.com/v1/";
    private CoinMarketCapCallbackInterface coinMarketCapCallbackInterface;

    public void start(CoinMarketCapCallbackInterface coinMarketCapCallbackInterface) {
        this.coinMarketCapCallbackInterface = coinMarketCapCallbackInterface;
        Gson gson = new GsonBuilder()
                .setLenient().
                        create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        CoinMarketCapApi coinMarketCapApi = retrofit.create(CoinMarketCapApi.class);
        Call<ArrayList<Coin>> call = coinMarketCapApi.getCoins("cad");
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<ArrayList<Coin>> call, Response<ArrayList<Coin>> response) {
        coinMarketCapCallbackInterface.returnResults(response.body());
    }

    @Override
    public void onFailure(Call<ArrayList<Coin>> call, Throwable t) {
        Log.i("FAIL", t.toString());
    }
}

//https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=CAD&limit=6