package com.dmtaiwan.alexander.bitcointicker.ui.main;

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
import com.dmtaiwan.alexander.bitcointicker.utility.helper.SimpleItemTouchHelperCallback;
import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.networking.CoinMarketCapApiController;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;

import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.getStrings;
import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.stripCursor;

public class MainActivity extends AppCompatActivity implements TickerCallback, CoinRecyclerAdapter.AdapterCallback {

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

        //Insert data into DB
        BitcoinDBHelper.bulkInsert(this, coins);
        queryDbForCoins();


        //Hide loading
        spinKitView.setVisibility(View.INVISIBLE);
    }


    private void queryDbForCoins() {
        //Get all preferences and add them to an ArrayList
        ArrayList<String> prefStrings = getStrings(MainActivity.this);

        //Create selectionAgs from ArrayList
        String[] selectionArgs = null;
        if (prefStrings.size() > 0) {
            selectionArgs = prefStrings.toArray(new String[prefStrings.size()]);
        }

        //If our selectionArgs aren't null, query only for the user's preferred coins
        if (selectionArgs != null) {
            Cursor cursor = BitcoinDBHelper.rawQuery(this, selectionArgs);
            //extract list of coins from cursor
            coins = stripCursor(cursor);
            cursor.close();
            adapter.swapData(coins);
        }

        //otherwise query for everything
        else{
            Cursor cursor = BitcoinDBHelper.readDb(this, null, null, selectionArgs, null);
            //extract list of coins from cursor
            coins = stripCursor(cursor);
            cursor.close();
            adapter.swapData(coins);
        }
    }



    private void startLoading() {
        //Get preferred secondary currency
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        int secondaryCurrency = prefs.getInt(SettingsActivity.KEY_PREF_CURRENCY, SettingsActivity.USD);
        spinKitView.setVisibility(View.VISIBLE);
        coinMarketCapApiController.getTickerData(this, secondaryCurrency);
    }

    @Override
    public void queryData() {
        queryDbForCoins();
    }


}

