package com.dmtaiwan.alexander.bitcointicker.networking;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Alexander on 8/20/2017.
 */

public interface CoinMarketCapApi {
    @GET("ticker/")
    Call<ArrayList<Coin>> getCoins(@Query("convert") String currency);
}
