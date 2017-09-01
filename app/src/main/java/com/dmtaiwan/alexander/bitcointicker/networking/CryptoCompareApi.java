package com.dmtaiwan.alexander.bitcointicker.networking;

import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;
import com.dmtaiwan.alexander.bitcointicker.model.Price;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Alexander on 8/31/2017.
 */

public interface CryptoCompareApi {
    @GET("histoday")
    Call<HistoricalData> getHistoricalData(@QueryMap Map<String, String> params);

    @GET("price")
    Call<Price> getPrice(@QueryMap Map<String, String> params);
}
