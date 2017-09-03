package com.dmtaiwan.alexander.bitcointicker.networking;

import android.util.Log;

import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;
import com.dmtaiwan.alexander.bitcointicker.model.Price;
import com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity;
import com.dmtaiwan.alexander.bitcointicker.ui.chart.HistoricalDataCallback;
import com.dmtaiwan.alexander.bitcointicker.ui.chart.PriceDataCallback;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alexander on 8/31/2017.
 */

public class CryptoCompareApiController{
    private static final String BASE_URL = "https://min-api.cryptocompare.com/data/";


    public void getHistoricalData(final HistoricalDataCallback historicalDataCallback, String symbol, final int periodInDays, int preferredCurrency) {

        String secondaryCurrencyString;
        switch (preferredCurrency) {
            case SettingsActivity.USD:
                secondaryCurrencyString = ChartActivity.USD_STRING;
                break;
            case SettingsActivity.CAD:
                secondaryCurrencyString = ChartActivity.CAD_STRING;
                break;
            case SettingsActivity.EUR:
                secondaryCurrencyString = ChartActivity.EUR_STRING;
                break;
            default:
                secondaryCurrencyString = ChartActivity.USD_STRING;
        }

        CryptoCompareApi cryptoCompareApi = buildRetrofitClient();
        Map<String, String> params = new HashMap<String, String>();
        params.put("fsym", symbol);
        //TODO implement currency selection
        params.put("tsym", secondaryCurrencyString);

        //Convert int to String
        String periodString = String.valueOf(periodInDays);
        params.put("limit", periodString);
        Call<HistoricalData> call = cryptoCompareApi.getHistoricalData(params);
        call.enqueue(new Callback<HistoricalData>() {
            @Override
            public void onResponse(Call<HistoricalData> call, Response<HistoricalData> response) {
                historicalDataCallback.returnHistoricalData(response.body(), periodInDays);
            }

            @Override
            public void onFailure(Call<HistoricalData> call, Throwable t) {
                Log.e("FAIL", t.toString());
            }

            private CryptoCompareApi buildRetrofitClient() {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                return retrofit.create(CryptoCompareApi.class);
            }
        });
    }


    public void getPriceData(final PriceDataCallback priceDataCallback, String symbol, String currencies) {
        CryptoCompareApi cryptoCompareApi = buildRetrofitClient();
        Map<String, String> params = new HashMap<>();
        params.put("fsym", symbol);
        params.put("tsyms", currencies);
        Call<Price> call = cryptoCompareApi.getPrice(params);
        call.enqueue(new Callback<Price>() {
            @Override
            public void onResponse(Call<Price> call, Response<Price> response) {
                priceDataCallback.returnPriceData(response.body());
            }

            @Override
            public void onFailure(Call<Price> call, Throwable t) {
                Log.i("BOOOO", t.toString());
            }
        });
    }

    private CryptoCompareApi buildRetrofitClient() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        return retrofit.create(CryptoCompareApi.class);
    }
}

//https://min-api.cryptocompare.com/data/histoday?fsym=BTC&tsym=CAD&limit=6
