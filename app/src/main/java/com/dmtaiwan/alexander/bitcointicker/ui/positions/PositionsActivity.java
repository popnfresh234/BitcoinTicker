package com.dmtaiwan.alexander.bitcointicker.ui.positions;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.model.Position;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;

import java.util.ArrayList;

import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.CAD_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.EUR_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.KEY_COIN_ID;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.USD_STRING;

/**
 * Created by Alexander on 9/6/2017.
 */

public class PositionsActivity extends AppCompatActivity implements PositionRecyclerAdapter.AdapterCallback{

    private PositionRecyclerAdapter positionRecyclerAdapter;
    private String secondaryCurrency;
    private String symbol;
    private float secondaryCurrencyFloat;

    private TextView priceUSD;
    private TextView priceSecondary;
    private TextView priceBTC;
    private TextView coinName;
    private TextView percentChange1H;
    private TextView percentChange24H;
    private TextView percentChange7D;
    private TextView profitTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positions);

        //Get preferred secondary currency
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PositionsActivity.this);
        secondaryCurrency = prefs.getString(SettingsActivity.KEY_PREF_CURRENCY, SettingsActivity.USD);


        //setup views
        priceUSD = (TextView) findViewById(R.id.text_view_pie_usd);
        priceSecondary = (TextView) findViewById(R.id.text_view_pie_secondary);
        priceBTC = (TextView) findViewById(R.id.text_view_pie_btc);
        coinName = (TextView) findViewById(R.id.text_view_ppie_coin_name);
        percentChange1H = (TextView) findViewById(R.id.text_view_pie_percent_change_one_hour);
        percentChange24H = (TextView) findViewById(R.id.text_view_pie_percent_change_24_hour);
        percentChange7D = (TextView) findViewById(R.id.text_view_pie_percent_change_seven_day);
        profitTextView = (TextView) findViewById(R.id.text_view_position_profit);
        RecyclerView positionRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_position);

        //Fetch data for header
        String coinId = getIntent().getStringExtra(KEY_COIN_ID);
        String[] selectionArgs = new String[]{coinId};
        Cursor cursor = BitcoinDBHelper.readDbCoins(this, null, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + "=?", selectionArgs, null);
        cursor.moveToFirst();

        //bind data from cursor
        bindDataFromCursor(cursor);

        //Setup recycler
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        positionRecyclerView.setLayoutManager(llm);

        //setup initial data
        positionRecyclerAdapter = new PositionRecyclerAdapter(this, buildArrayListPosition(), secondaryCurrency, this);
        positionRecyclerView.setAdapter(positionRecyclerAdapter);

        //Setup edit text to get data
        final EditText positionEditText = (EditText) findViewById(R.id.edit_text_position_position);
        final EditText priceEditText = (EditText) findViewById(R.id.edit_text_position_price);
        Button addButton = (Button) findViewById(R.id.button_position_add);

        //setup listener on add button:
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String position = positionEditText.getText().toString();
                String price = priceEditText.getText().toString();
                if (position != null && !position.equals("") &&price!= null && !price.equals("") ){
                    BitcoinDBHelper.insertPositionData(PositionsActivity.this, symbol, position, price);
                    positionRecyclerAdapter.swapData(buildArrayListPosition());
                    profitTextView.setText(getCurrentProfit(secondaryCurrencyFloat, buildArrayListPosition()));
                }
                //show reminder
                if (position == null || position.equals("")) {
                    positionEditText.setError("Enter a valid position");
                }


                if (price == null || price.equals("")) {
                    priceEditText.setError("Enter a valid price");
                }
            }
        });
    }

    private void bindDataFromCursor(Cursor cursor) {
        //get symbol
        symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));

        priceUSD.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)), SettingsActivity.USD));
        priceBTC.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC)));
        coinName.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        percentChange1H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H))));
        percentChange24H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H))));
        percentChange7D.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D))));


        //Set secondary currency
        switch (secondaryCurrency) {
            case SettingsActivity.USD:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)), secondaryCurrency) + " " + USD_STRING);
                secondaryCurrencyFloat = Float.valueOf(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)));
                break;
            case SettingsActivity.CAD:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD)), secondaryCurrency) + " " + CAD_STRING);
                secondaryCurrencyFloat = Float.valueOf(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD)));
                break;
            case SettingsActivity.EUR:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR)), secondaryCurrency) + " " + EUR_STRING);
                secondaryCurrencyFloat = Float.valueOf(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR)));
                break;
            default:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)), SettingsActivity.USD) + " " + USD_STRING);
                secondaryCurrencyFloat = Float.valueOf(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)));

        }
        profitTextView.setText(getCurrentProfit(secondaryCurrencyFloat, buildArrayListPosition()));
        cursor.close();
    }

    private ArrayList<Position> buildArrayListPosition() {
        Cursor cursor = BitcoinDBHelper.readDbPositions(PositionsActivity.this, null, BitcoinDBContract.PositionEntry.COLUMN_SYMBOL+ "=?", new String[]{symbol}, null);
        ArrayList<Position> positions = Utils.extractPositionsFromCursor(cursor);
        cursor.close();
        return positions;
    }

    @Override
    public void onDataUpdated() {
        positionRecyclerAdapter.swapData(buildArrayListPosition());
        profitTextView.setText(getCurrentProfit(secondaryCurrencyFloat, buildArrayListPosition()));
    }

    private String getCurrentProfit(float currentPriceInSecondaryPrice, ArrayList<Position> positions) {
        float profit = 0;
        for (Position position : positions) {
            profit += (currentPriceInSecondaryPrice * Float.valueOf(position.getPosition())) - (Float.valueOf(position.getPosition())* Float.valueOf(position.getPrice()));
        }
        if (profit < 0) {
            this.profitTextView.setTextColor(getResources().getColor(R.color.colorTextLoss));
        }
        return Utils.formatCurrency(String.valueOf(profit), secondaryCurrency);
    }
}
