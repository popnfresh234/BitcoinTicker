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
import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.networking.APIController;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CallbackInterface {

    private CoinRecyclerAdapter adapter;
    private APIController apiController;
    private SpinKitView spinKitView;
    private Cursor cursor;

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
        adapter = new CoinRecyclerAdapter(this, null);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        coiRecyclerView.setLayoutManager(llm);
        coiRecyclerView.setAdapter(adapter);

        //Set up swipe to delete
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove item from shared prefs
                int position = viewHolder.getAdapterPosition();
                cursor.moveToPosition(position);
                String coinId = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID));
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove(coinId).commit();
                queryDbForCoins();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(coiRecyclerView);

        //Set up API controller
        apiController = new APIController();
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
            cursor = BitcoinDBHelper.rawQuery(this, selectionArgs);
            adapter.swapCursor(cursor);
        }

        //otherwise query for everything
        else{
            cursor = BitcoinDBHelper.readDb(this, null, null, selectionArgs, null);
            adapter.swapCursor(cursor);
        }
    }

    private void startLoading() {
        spinKitView.setVisibility(View.VISIBLE);
        apiController.start(this);
    }
}

