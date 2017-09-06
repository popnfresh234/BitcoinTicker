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

/**
 * Created by Alexander on 8/25/2017.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_CURRENCY = "com.dmtaiwan.alexander.bitcointicker.key_pref_currency";
    public static final int USD = 0;
    public static final int CAD = 1;
    public static final int EUR = 2;
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

                SharedPreferences.Editor editor = prefs.edit();
                editor.clear().commit();
                settingsAdapter.notifyDataSetChanged();
            }
        });

        //Setup currency spinner
        Spinner currencySpinner = (Spinner) findViewById(R.id.currency_spinner);
        ArrayAdapter<CharSequence> currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currency_array, android.R.layout.simple_spinner_item);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);
        //get set position
        int setPosition = prefs.getInt(KEY_PREF_CURRENCY, USD);
        currencySpinner.setSelection(setPosition);
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(KEY_PREF_CURRENCY, position).commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
