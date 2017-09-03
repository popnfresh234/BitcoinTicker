package com.dmtaiwan.alexander.bitcointicker.ui.piechart;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.dmtaiwan.alexander.bitcointicker.utility.ColorTemplate;
import com.dmtaiwan.alexander.bitcointicker.utility.PieChartValueFormatter;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.CAD_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.EUR_STRING;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.KEY_COIN_ID;
import static com.dmtaiwan.alexander.bitcointicker.ui.chart.ChartActivity.USD_STRING;

/**
 * Created by Alexander on 9/2/2017.
 */

public class PieChartActivity extends AppCompatActivity {
    private static final int HIDE_SMALL_CAP = 0;
    private static final int SHOW_SMALL_CAP = 1;

    private TextView priceUSD;
    private TextView priceSecondary;
    private TextView priceBTC;
    private TextView coinName;
    private TextView percentChange1H;
    private TextView percentChange24H;
    private TextView percentChange7D;
    private PieChart pieChart;
    private TextView showSmallCap;
    private TextView hideSmallCap;

    private int secondaryCurrency;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        //Get preferred secondary currency
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(PieChartActivity.this);
        secondaryCurrency = prefs.getInt(SettingsActivity.KEY_PREF_CURRENCY, SettingsActivity.USD);




        //setup views
        priceUSD = (TextView) findViewById(R.id.text_view_detail_price_usd);
        priceSecondary = (TextView) findViewById(R.id.text_view_detail_price_secondary);
        priceBTC = (TextView) findViewById(R.id.text_view_detail_price_btc);
        coinName = (TextView) findViewById(R.id.text_view_detail_coin_name);
        percentChange1H = (TextView) findViewById(R.id.text_view_detail_percent_change_one_hour);
        percentChange24H = (TextView) findViewById(R.id.text_view_detail_percent_change_24_hour);
        percentChange7D = (TextView) findViewById(R.id.text_view_detail_percent_change_seven_day);
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        showSmallCap = (TextView) findViewById(R.id.text_view_detail_show_small_cap);
        hideSmallCap = (TextView) findViewById(R.id.text_view_detail_hide_small_cap);

        //Setup listeners for show/hide small cap
        showSmallCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChart(SHOW_SMALL_CAP);
            }
        });

        hideSmallCap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateChart(HIDE_SMALL_CAP);
            }
        });

        //Fetch data
        String coinId = getIntent().getStringExtra(KEY_COIN_ID);
        String[] selectionArgs = new String[]{coinId};
        Cursor cursor = BitcoinDBHelper.readDb(this, null, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + "=?", selectionArgs, null);
        cursor.moveToFirst();

        //bind data from cursor
        bindDataFromCursor(cursor);

        //Setup chart
        setupChart();

        //Get total market cap for chart
        generateChart(HIDE_SMALL_CAP);

    }

    private void generateChart(int chartType) {
        float totalMarketCap = getTotalMarketCap();

        ArrayList<PieEntry> entries = new ArrayList<>();
        //generate entries for chart
        if (chartType == HIDE_SMALL_CAP) {
            entries = getPieEntriesHideSmallCap(totalMarketCap);
            hideSmallCap.setTextColor(getResources().getColor(R.color.colorAccentYellow));
            showSmallCap.setTextColor(getResources().getColor(R.color.primaryTextColor));

        } else if (chartType == SHOW_SMALL_CAP) {
            entries = getPieEntries(totalMarketCap);
            showSmallCap.setTextColor(getResources().getColor(R.color.colorAccentYellow));
            hideSmallCap.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }

        //Create pie data set
        PieDataSet dataSet = getPieDataSet(entries);
        PieData data = getPieData(dataSet);
        data.setValueFormatter(new PieChartValueFormatter());
        pieChart.setData(data);
        pieChart.invalidate();
    }

    @NonNull
    private PieData getPieData(PieDataSet dataSet) {
        PieData data = new PieData(dataSet);
        data.setValueTextSize(getResources().getDimension(R.dimen.text_size_pie_chart_values));
        return data;
    }

    private void bindDataFromCursor(Cursor cursor) {
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
                break;
            case SettingsActivity.CAD:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD)), secondaryCurrency) + " " + CAD_STRING);
                break;
            case SettingsActivity.EUR:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR)), secondaryCurrency) + " " + EUR_STRING);
                break;
            default:
                priceSecondary.setText(Utils.formatCurrency(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)), SettingsActivity.USD) + " " + USD_STRING);

        }
        cursor.close();
    }

    @NonNull
    private PieDataSet getPieDataSet(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        //Create colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        dataSet.setColors(colors);
        return dataSet;
    }

    @NonNull
    private ArrayList<PieEntry> getPieEntriesHideSmallCap(float totalMarketCap) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor allCoins = BitcoinDBHelper.readDb(this, null, null, null, null);
        float smallCapCoins = 0f;
        while (allCoins.moveToNext()) {
            float marketCapFloat = 0f;
            String marketCap = allCoins.getString(allCoins.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
            if (marketCap != null) {
                marketCapFloat = Float.parseFloat(marketCap);
            }
            float percentage = marketCapFloat / totalMarketCap * 100;

            //aggregate market cap < 2%
            if (percentage >= 2f) {
                String symbol = allCoins.getString(allCoins.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME));
                PieEntry pieEntry = new PieEntry(percentage, symbol);
                entries.add(pieEntry);
            } else {
                smallCapCoins += percentage;
            }
        }
        entries.add(new PieEntry(smallCapCoins, getResources().getString(R.string.small_cap_label)));
        allCoins.close();
        return entries;
    }

    @NonNull
    private ArrayList<PieEntry> getPieEntries(float totalMarketCap) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor cursor = BitcoinDBHelper.readDb(this, null, null, null, null);
        while (cursor.moveToNext()) {
            float marketCapFloat = 0f;
            String marketCap = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
            if (marketCap != null) {
                marketCapFloat = Float.parseFloat(marketCap);
            }
            float percentage = marketCapFloat / totalMarketCap * 100;
            String symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME));
            //Cut out very small values, not useful for chart
            if (percentage > 0.1) {
                PieEntry pieEntry;
                if (percentage >= 2f) {
                    pieEntry = new PieEntry(percentage, symbol);
                }else {
                    pieEntry = new PieEntry(percentage, "");
                }
                entries.add(pieEntry);
            }


        }
        cursor.close();
        return entries;
    }

    private float getTotalMarketCap() {
        Cursor cursor = BitcoinDBHelper.readDb(this, null, null, null, null);
        float totalMarketCap = 0f;
        while (cursor.moveToNext()) {
            String marketCapString = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
            if (marketCapString != null) {
                totalMarketCap += Float.valueOf(marketCapString);
            }
        }
        cursor.close();
        return totalMarketCap;
    }

    private void setupChart() {
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        pieChart.setDrawHoleEnabled(false);
        Description description = new Description();
        description.setText("Market Capitlization for All Cryptocurrencies");
        description.setTextColor(getResources().getColor(R.color.primaryTextColor));
        pieChart.setDescription(description);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorPrimary));
        pieChart.getLegend().setEnabled(false);
    }
}
