package com.dmtaiwan.alexander.bitcointicker.ui.settings;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;

import java.util.Map;

/**
 * Created by Alexander on 8/25/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_CURRENCY = "com.dmtaiwan.alexander.bitcointicker.key_pref_currency";
    public static final String KEY_PREF_EXCHANGE = "com.dmtaiwan.alexander.bitcointicker.key_pref_exchange";
    public static final String KEY_PREF_TIMEZONE = "com.dmtaiwan.alexander.bitcointicker.key_time_zone";
    public static final String DEFAULT_TIMEZONE = "America/Vancouver";
    public static final String DEFAULT_EXCHANGE = "CCCAGG";
    public static final String USD = "USD";
    public static final String CAD = "CAD";
    public static final String EUR = "EUR";

    private static final String[] PROJECTION = new String[]{BitcoinDBContract.BitcoinEntry.COLUMN_NAME, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID};
    private SettingsAdapter settingsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Setup RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.pref_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        //Default query to populate:
        Cursor cursor = BitcoinDBHelper.readDbCoins(this, PROJECTION, null, null, null);
        settingsAdapter = new SettingsAdapter(this, cursor);
        recyclerView.setAdapter(settingsAdapter);

        //Setup search
        SearchView searchView = (SearchView) findViewById(R.id.pref_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String coinTerm) {
                Cursor cursor = BitcoinDBHelper.querySimilarCoins(SettingsActivity.this, coinTerm);
                settingsAdapter.swapCursor(cursor);
                return false;
            }
        });
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);

        //Set up clear button
        ImageView clearPrefs = (ImageView) findViewById(R.id.image_view_clear_prefs);
        clearPrefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Remove all coin selections by removing all keys that are not one of the other preferences
                Map<String, ?> keys = prefs.getAll();
                SharedPreferences.Editor editor = prefs.edit();
                for (Map.Entry<String, ?> entry : keys.entrySet()) {
                    if (entry.getKey().equals(KEY_PREF_CURRENCY) || entry.getKey().equals(KEY_PREF_EXCHANGE) || entry.getKey().equals(KEY_PREF_TIMEZONE)) {

                    } else {
                        editor.remove(entry.getKey()).commit();

                    }
                }
                settingsAdapter.notifyDataSetChanged();

            }
        });

        //Setup exchange spinner
        Spinner exchangeSpinner = (Spinner) findViewById(R.id.exchange_spinner);
        final ArrayAdapter<CharSequence> exchangeAdapter = ArrayAdapter.createFromResource(this, R.array.exchange_array, android.R.layout.simple_spinner_item);
        exchangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exchangeSpinner.setAdapter(exchangeAdapter);
        //get set position
        String exchangeString = prefs.getString(KEY_PREF_EXCHANGE, DEFAULT_EXCHANGE);
        int exchangePosition = 0;
        exchangePosition = exchangeAdapter.getPosition(exchangeString);
        exchangeSpinner.setSelection(exchangePosition);
        exchangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_PREF_EXCHANGE, exchangeAdapter.getItem(position).toString()).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Setup timezone spinner
        Spinner timezoneSpinner = (Spinner) findViewById(R.id.timzone_spinner);
        final ArrayAdapter<CharSequence> timezoneAdapter = ArrayAdapter.createFromResource(this, R.array.timezone_array, android.R.layout.simple_spinner_item);
        timezoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timezoneSpinner.setAdapter(timezoneAdapter);
        //get set position
        String timezoneString = prefs.getString(KEY_PREF_TIMEZONE, DEFAULT_EXCHANGE);
        int timezonePosition = 0;
        timezonePosition = timezoneAdapter.getPosition(timezoneString);
        timezoneSpinner.setSelection(timezonePosition);
        timezoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_PREF_TIMEZONE, timezoneAdapter.getItem(position).toString()).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Setup currency spinner
        Spinner currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        final ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        //get set position
        String currencyString = prefs.getString(KEY_PREF_CURRENCY, USD);
        int currencyPosition = 0;
        currencyPosition = currencyAdapter.getPosition(currencyString);
        currencySpinner.setSelection(currencyPosition);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(KEY_PREF_CURRENCY, currencyAdapter.getItem(position).toString()).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
