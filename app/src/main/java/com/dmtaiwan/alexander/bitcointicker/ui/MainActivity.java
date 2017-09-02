package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.helper.SimpleItemTouchHelperCallback;
import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.networking.CoinMarketCapApiController;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CoinMarketCapCallbackInterface, CoinRecyclerAdapter.AdapterCallback {

    private CoinRecyclerAdapter adapter;
    private CoinMarketCapApiController coinMarketCapApiController;
    private SpinKitView spinKitView;
    private ArrayList<Coin> coins;

    private static final String SORT_ORDER = BitcoinDBContract.BitcoinEntry.COLUMN_NAME + " ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up Views
        RecyclerView coiRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_coin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        spinKitView = (SpinKitView) toolbar.findViewById(R.id.spin_kit);


        //Set action bar
        setSupportActionBar(toolbar);

        //Set up Adapter
        adapter = new CoinRecyclerAdapter(this, new ArrayList<Coin>(), this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        coiRecyclerView.setLayoutManager(llm);
        coiRecyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(coiRecyclerView);

        //Set up API controller
        coinMarketCapApiController = new CoinMarketCapApiController();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLoading();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;

            case R.id.refresh:
                startLoading();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void returnResults(ArrayList<Coin> coins) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Insert data into DB
        BitcoinDBHelper.bulkInsert(this, coins);
        queryDbForCoins();


        //Hide loading
        spinKitView.setVisibility(View.INVISIBLE);
    }


    private void queryDbForCoins() {
        //Get all preferences and add them to an ArrayList
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        ArrayList<String> prefStrings = new ArrayList<>();
        if (prefs.getAll() != null) {
            Map<String, ?> keys = prefs.getAll();
            for (Map.Entry<String, ?> entry : keys.entrySet()) {
                boolean value = Boolean.valueOf(entry.getValue().toString());
                if (value) {
                    prefStrings.add(entry.getKey());
                }
            }
        }

        //Create selectionAgs from ArrayList
        String[] selectionArgs = null;
        if (prefStrings.size() > 0) {
            selectionArgs = prefStrings.toArray(new String[prefStrings.size()]);
        }

        //If our selectionArgs aren't null, query only for the user's preferred coins
        if (selectionArgs != null) {
            Cursor cursor = BitcoinDBHelper.rawQuery(this, selectionArgs);
            coins = stripCurosr(cursor);
            cursor.close();
            adapter.swapData(coins);
        }

        //otherwise query for everything
        else{
            Cursor cursor = BitcoinDBHelper.readDb(this, null, null, selectionArgs, null);
            coins = stripCurosr(cursor);
            cursor.close();
            adapter.swapData(coins);
        }
    }

    private ArrayList<Coin> stripCurosr(Cursor cursor) {
        ArrayList<Coin> coinList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID));
            String name = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME));
            String symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));
            String rank = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_RANK));
            String priceUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD));
            String priceBtc = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC));
            String twentyFourHourVolumeUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD));
            String marketCapUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
            String availableSupply = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY));
            String totalSupply = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY));
            String percentChangeOneHour = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H));
            String percentChangeTwentyFourHour = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H));
            String percentChangeSevenDays = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D));
            String lastUpdated = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED));
            String priceCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD));
            String twentyFourHourVolumeCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD));
            String marketCapCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD));

            Coin coin = new Coin(id, name, symbol, rank, priceUsd, priceBtc, twentyFourHourVolumeUsd,
                    marketCapUsd, availableSupply, totalSupply, percentChangeOneHour, percentChangeTwentyFourHour,
                    percentChangeSevenDays, lastUpdated, priceCad, twentyFourHourVolumeCad, marketCapCad, false);

            coinList.add(coin);
        }
        return coinList;
    }

    private void startLoading() {
        spinKitView.setVisibility(View.VISIBLE);
        coinMarketCapApiController.start(this);
    }

    @Override
    public void queryData() {
        queryDbForCoins();
    }


}

