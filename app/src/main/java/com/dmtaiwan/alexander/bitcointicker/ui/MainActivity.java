package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.networking.APIController;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CallbackInterface {

    private CoinCursorAdapter adapter;
    private ProgressBar progressBar;
    private APIController apiController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up Views
        ListView coinListView = (ListView) findViewById(R.id.list_view_coin);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        //Set up Adapter
        adapter = new CoinCursorAdapter(this, null, false);
        coinListView.setAdapter(adapter);

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
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                return true;
            case R.id.refresh:
                startLoading();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void returnResults(ArrayList<Coin> coins) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Insert data into DB
        BitcoinDBHelper.bulkInsert(this, coins);

        //Get all preferences and add them to an ArrayList
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
            adapter.changeCursor(cursor);
        }

        //otherwise query for everything
        else{
            Cursor cursor = BitcoinDBHelper.readDb(this, null, null, selectionArgs);
            adapter.changeCursor(cursor);
        }

        //hide progress when done
        progressBar.setVisibility(View.GONE);
    }

    private void startLoading() {
        apiController.start(this);
        progressBar.setVisibility(View.VISIBLE);
    }
}

