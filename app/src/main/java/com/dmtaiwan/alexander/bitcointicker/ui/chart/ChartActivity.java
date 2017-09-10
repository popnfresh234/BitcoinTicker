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

    //Preferred values
    private String secondaryCurrency;
    private String exchange;
    private String timeZone;

    private TextView priceSecondary;
    private TextView coinName;

    private LineChart chartView;
    private SpinKitView loadingViewChart;
    private SpinKitView loadingViewInfo;
    //Chart listeners
    private TextView oneYearChart;
    private TextView sixMonthsChart;
    private TextView threeMonthsChart;
    private TextView oneMonthChart;
    private TextView oneWeekChart;

    //buttons for currency
    private TextView usdCurrencyButton;
    private TextView secondaryCurrencyButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //Get preferred secondary currency
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChartActivity.this);
        secondaryCurrency = prefs.getString(SettingsActivity.KEY_PREF_CURRENCY, SettingsActivity.USD);
        exchange = prefs.getString(SettingsActivity.KEY_PREF_EXCHANGE, SettingsActivity.DEFAULT_EXCHANGE);
        timeZone = prefs.getString(SettingsActivity.KEY_PREF_TIMEZONE, SettingsActivity.DEFAULT_TIMEZONE);


        //setup views
        priceSecondary = (TextView) findViewById(R.id.text_view_detail_secondary);
        coinName = (TextView) findViewById(R.id.text_view_detail_coin_name);


        //setup TextViews for selecting charts
        oneYearChart = (TextView) findViewById(R.id.text_view_detail_1Y);
        sixMonthsChart = (TextView) findViewById(R.id.text_view_detail_6M);
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

        //Setup buttons for USD/Secondary Currency
        usdCurrencyButton = (TextView) findViewById(R.id.text_view_detail_USD_button);
        secondaryCurrencyButton = (TextView) findViewById(R.id.text_view_detail_secondary_currency_button);
        secondaryCurrencyButton.setText(secondaryCurrency);
        usdCurrencyButton.setOnClickListener(this);
        secondaryCurrencyButton.setOnClickListener(this);

        //Fetch data
        String coinId = getIntent().getStringExtra(KEY_COIN_ID);
        String[] selectionArgs = new String[]{coinId};
        Cursor cursor = BitcoinDBHelper.readDbCoins(this, null, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID + "=?", selectionArgs, null);
        cursor.moveToFirst();

        //Grab symbol from cursor, finished with cursor now;
        symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));
        coinName.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));

        cursor.close();

        //fetch price data for default chart:
        cryptoCompareApiController = new CryptoCompareApiController();

        //Get data from API
        loadData(secondaryCurrency);

        oneWeekChart.setTextColor(getResources().getColor(R.color.colorAccentYellow));

        //Setup listeners for chart selection TextViews:
        for (TextView textView : chartTextViews) {
            textView.setOnClickListener(this);
        }


    }

    private void loadData(String currency) {
        cryptoCompareApiController.getPriceData(this, symbol, currency, exchange);
        toggleInfoLoadingOn();
        cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1W, currency, exchange);
        toggleChartLoadingOn();
    }

    @Override
    public void returnPriceData(Price price, String currency) {
        toggleInfoLoadingOff();
        switch (currency) {
            case SettingsActivity.USD:
                priceSecondary.setText(price.getUSD());
                usdCurrencyButton.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                secondaryCurrencyButton.setTextColor(getResources().getColor(R.color.primaryTextColor));
                break;
            case SettingsActivity.CAD:
                priceSecondary.setText(price.getCAD());
                secondaryCurrencyButton.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                usdCurrencyButton.setTextColor(getResources().getColor(R.color.primaryTextColor));
                break;
            case SettingsActivity.EUR:
                priceSecondary.setText(price.getEUR());
                secondaryCurrencyButton.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                usdCurrencyButton.setTextColor(getResources().getColor(R.color.primaryTextColor));
                break;
            default:
                priceSecondary.setText(price.getUSD());
                usdCurrencyButton.setTextColor(getResources().getColor(R.color.colorAccentYellow));
                secondaryCurrencyButton.setTextColor(getResources().getColor(R.color.primaryTextColor));
        }
    }

    @Override
    public void returnHistoricalData(HistoricalData historicalData, int period) {
        toggleChartLoadingOff();

        if (!historicalData.getResponse().equals("Error")) {
            //Create chart data
            LineData lineData = createChartLineData(ChartActivity.this, historicalData, symbol);
            //Set data to chart and format general chart
            setupChart(lineData);
        }else chartView.setNoDataText("No data available for selected currency");


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
            case R.id.text_view_detail_1Y:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1Y, secondaryCurrency, exchange);
                break;
            case R.id.text_view_detail_6M:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_6M, secondaryCurrency, exchange);
                break;
            case R.id.text_view_detail_3M:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_3M, secondaryCurrency, exchange);
                break;
            case R.id.text_view_detail_1M:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1M, secondaryCurrency, exchange);
                break;
            case R.id.text_view_detail_1W:
                cryptoCompareApiController.getHistoricalData(this, symbol, PERIOD_1W, secondaryCurrency, exchange);
                break;

            //handle Currency buttons
            case R.id.text_view_detail_USD_button:
                loadData(SettingsActivity.USD);
                break;
            case R.id.text_view_detail_secondary_currency_button:
                loadData(secondaryCurrency);
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
        xAxis.setValueFormatter(new LineChartXAxisValueFormatter(timeZone));
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
