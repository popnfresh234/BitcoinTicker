package com.dmtaiwan.alexander.bitcointicker.ui.chart;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;
import com.dmtaiwan.alexander.bitcointicker.model.Price;
import com.dmtaiwan.alexander.bitcointicker.networking.CryptoCompareApiController;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;
import com.dmtaiwan.alexander.bitcointicker.utility.LineChartXAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;

import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.createChartLineData;

/**
 * Created by Alexander on 9/1/2017.
 */

public class ChartActivity extends AppCompatActivity implements View.OnClickListener, HistoricalDataCallback, PriceDataCallback {

    public static final String KEY_COIN_ID = "com.dmtaiwan.alexander.bitcointicker.coin_id";

    //values for chart periods
    private static final int PERIOD_1Y = 364;
    private static final int PERIOD_6M = 181;
    private static final int PERIOD_3M = 89;
    private static final int PERIOD_1M = 29;
    private static final int PERIOD_1W = 6;

    public static final String BTC_STRING = "BTC";
    public static final String USD_STRING = "USD";
    public static final String CAD_STRING = "CAD";
    public static final String EUR_STRING = "EUR";

    private String symbol;
    private CryptoCompareApiController cryptoCompareApiController;
    private ArrayList<TextView> chartTextViews = new ArrayList<>();
    private int secondaryCurrency;

    private TextView priceUSD;
    private TextView priceSecondary;
    private TextView priceBTC;
    private TextView coinName;
    private TextView percentChange1H;
    private TextView percentChange24H;
    private TextView percentChange7D;
    private LineChart chartView;
    private SpinKitView loadingViewChart;
    private SpinKitView loadingViewInfo;
    //Chart listeners
    private TextView oneYearChart;
    private TextView sixMonthsChart;
    private TextView threeMonthsChart;
    private TextView oneMonthChart;
    private TextView oneWeekChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //Get preferred secondary currency
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChartActivity.this);
        secondaryCurrency = prefs.getInt(SettingsActivity.KEY_PREF_CURRENCY, SettingsActivity.USD);


        //setup views
        priceUSD = (TextView) findViewById(R.id.text_view_detail_price_usd);
        priceSecondary = (TextView) findViewById(R.id.text_view_detail_price_secondary);
        priceBTC = (TextView) findViewById(R.id.text_view_detail_price_btc);
        coinName = (TextView) findViewById(R.id.text_view_detail_coin_name);
        percentChange1H = (TextView) findViewById(R.id.text_view_detail_percent_change_one_hour);
        percentChange24H = (TextView) findViewById(R.id.text_view_detail_percent_change_24_hour);
        percentChange7D = (TextView) findViewById(R.id.text_view_detail_percent_change_seven_day);

        //setup TextViews for selecting charts
        oneYearChart = (TextView) findViewById(R.id.text_view_detail_show_small_cap);
        sixMonthsChart = (TextView) findViewById(R.id.text_view_detail_hide_small_cap);
        threeMonthsChart = (TextView) findViewById(R.id.text_view_detail_3M);
        oneMonthChart = (TextView) findViewById(R.id.text_view_detail_1M);
        oneWeekChart = (TextView) findViewById(R.id.text_view_detail_1W);
        chartView = (LineChart) findViewById(R.id.price_chart);
        loadingViewChart = (SpinKitView) findViewById(R.id.detail_spin_kit_chart);
        loadingViewInfo = (SpinKitView) findViewById(R.id.detail_spin_kit_info);

        chartTextViews.add(oneYearChart);
        chartTextViews.add(sixMonthsChart);
        chartTextViews.add(threeMonthsChart);
        chartTextViews.add(oneMonthChart);
        chartTextViews.add(oneWeekChart);


        //Fetch data
        String coinId = getIntent().getStringExtra(KEY_COIN_ID);
        String[] selectionArgs = new String[]{coinId};
        Cursor cursor = BitcoinDBHelper.readDbCoins(this, null, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + "=?", selectionArgs, null);
        cursor.moveToFirst();

        //bind data from cursor
        coinName.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        percentChange1H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H))));
        percentChange24H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H))));
        percentChange7D.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D))));

        //Grab symbol from cursor, finished with cursor now;
        symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));
        cursor.close();

        //fetch price data for default chart:
        cryptoCompareApiController = new CryptoCompareApiController();
        String currencies = BTC_STRING + "," + USD_STRING + "," + CAD_STRING + "," + EUR_STRING;
        cryptoCompareApiController.getPriceData(this, symbol, currencies);
        toggleInfoLoadingOn();
        cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1W, secondaryCurrency);
        toggleChartLoadingOn();
        oneWeekChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));

        //Setup listeners for chart selection TextViews:
        for (TextView textView : chartTextViews) {
            textView.setOnClickListener(this);
        }


    }

    @Override
    public void returnPriceData(Price price) {
        toggleInfoLoadingOff();
        //Pass USD_STRING constant here for preferred currency since this will always display USD_STRING
        priceUSD.setText(Utils.formatCurrency(price.getUSD(), SettingsActivity.USD) +  " " + USD_STRING);
        switch (secondaryCurrency) {
            case SettingsActivity.USD:
                priceSecondary.setText(Utils.formatCurrency(price.getUSD(), secondaryCurrency) + " " + USD_STRING);
                break;
            case SettingsActivity.CAD:
                priceSecondary.setText(Utils.formatCurrency(price.getCAD(), secondaryCurrency) + " " + CAD_STRING);
                break;
            case SettingsActivity.EUR:
                priceSecondary.setText(Utils.formatCurrency(price.getEUR(), secondaryCurrency) + " " + EUR_STRING);
                break;
            default:
                priceSecondary.setText(Utils.formatCurrency(price.getUSD(), SettingsActivity.USD) + " " + USD_STRING);

        }
        priceBTC.setText(price.getBTC() + " BTC");
    }

    @Override
    public void returnHistoricalData(HistoricalData historicalData, int period) {
        toggleChartLoadingOff();

        //Create chart data
        LineData lineData = createChartLineData(ChartActivity.this, historicalData, symbol);

        //Set data to chart and format general chart
        setupChart(lineData);

        //set chart selector TextView colors based on period user selected:
        resetTextColors();
        switch (period) {
            case PERIOD_1Y:
                oneYearChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                break;
            case PERIOD_6M:
                sixMonthsChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));

                break;
            case PERIOD_3M:
                threeMonthsChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                break;
            case PERIOD_1M:
                oneMonthChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                break;
            case PERIOD_1W:
                oneWeekChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                break;
        }
    }


    @Override
    public void onClick(View v) {
        toggleChartLoadingOn();

        switch (v.getId()) {
            case R.id.text_view_detail_show_small_cap:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1Y, secondaryCurrency);
                break;
            case R.id.text_view_detail_hide_small_cap:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_6M, secondaryCurrency);
                break;
            case R.id.text_view_detail_3M:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_3M, secondaryCurrency);
                break;
            case R.id.text_view_detail_1M:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1M, secondaryCurrency);
                break;
            case R.id.text_view_detail_1W:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1W, secondaryCurrency);
                break;
        }
    }

    private void resetTextColors() {
        for (TextView textView : chartTextViews) {
            textView.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
    }

    private void toggleChartLoadingOn() {
        loadingViewChart.setVisibility(View.VISIBLE);
        chartView.setVisibility(View.INVISIBLE);
    }

    private void toggleChartLoadingOff() {
        loadingViewChart.setVisibility(View.INVISIBLE);
        chartView.setVisibility(View.VISIBLE);
    }

    private void toggleInfoLoadingOn() {
        loadingViewInfo.setVisibility(View.VISIBLE);
    }

    private void toggleInfoLoadingOff() {
        loadingViewInfo.setVisibility(View.GONE);
    }

    private void setupChart(LineData lineData) {
        chartView.setData(lineData);
        chartView.getLegend().setTextColor(getResources().getColor(R.color.primaryTextColor));
        Description description = new Description();
        description.setText("7D Price History for " + symbol);
        description.setTextColor(getResources().getColor(R.color.primaryTextColor));
        chartView.setDescription(description);

        //Format xAxis
        XAxis xAxis = chartView.getXAxis();
        xAxis.setValueFormatter(new LineChartXAxisValueFormatter());
        xAxis.setLabelCount(7, true);
        xAxis.setTextColor(getResources().getColor(R.color.primaryTextColor));

        //format yAxis color
        YAxis yAxisLeft = chartView.getAxisLeft();
        yAxisLeft.setTextColor(getResources().getColor(R.color.primaryTextColor));

        YAxis yAxisRight = chartView.getAxisRight();
        yAxisRight.setTextColor(getResources().getColor(R.color.primaryTextColor));
        chartView.invalidate();
    }
}
