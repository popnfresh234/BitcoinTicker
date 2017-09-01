package com.dmtaiwan.alexander.bitcointicker.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBHelper;
import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;
import com.dmtaiwan.alexander.bitcointicker.model.Price;
import com.dmtaiwan.alexander.bitcointicker.networking.CryptoCompareApiController;
import com.dmtaiwan.alexander.bitcointicker.utility.Utils;
import com.dmtaiwan.alexander.bitcointicker.utility.XAxisValueFormatter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

/**
 * Created by Alexander on 9/1/2017.
 */

public class ChartActivity extends AppCompatActivity implements HistoricalDataCallback, PriceDataCallback{

    public static final String KEY_COIN_ID = "com.dmtaiwan.alexander.bitcointicker.coin_id";

    private String symbol;

    private TextView priceUSD;
    private TextView priceCAD;
    private TextView priceBTC;
    private TextView coinName;
    private TextView percentChange1H;
    private TextView percentChange24H;
    private TextView percentChange7D;
    private LineChart priceChart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        //setup views
        priceUSD = (TextView) findViewById(R.id.text_view_detail_price_usd);
        priceCAD = (TextView) findViewById(R.id.text_view_detail_price_cad);
        priceBTC = (TextView) findViewById(R.id.text_view_detail_price_btc);
        coinName = (TextView) findViewById(R.id.text_view_detail_coin_name);
        percentChange1H = (TextView) findViewById(R.id.text_view_detail_percent_change_one_hour);
        percentChange24H = (TextView) findViewById(R.id.text_view_detail_percent_change_24_hour);
        percentChange7D = (TextView) findViewById(R.id.text_view_detail_percent_change_seven_day);
        priceChart = (LineChart) findViewById(R.id.price_chart);

        //Fetch data
        String coinId = getIntent().getStringExtra(KEY_COIN_ID);
        String[] selectionArgs = new String[]{coinId};
        Cursor cursor = BitcoinDBHelper.readDb(this, null, BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID+ "=?", selectionArgs, null);
        cursor.moveToFirst();

        //bind data from cursor
        coinName.setText(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        percentChange1H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H))));
        percentChange24H.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H))));
        percentChange7D.setText(Utils.formatPercentage(cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D))));

        //Grab symbol from cursor, finished with cursor now;
        symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));
        cursor.close();

        //fetch price data:
        CryptoCompareApiController cryptoCompareApiController = new CryptoCompareApiController();
        String currencies = "BTC,USD,CAD,EUR";
        cryptoCompareApiController.getPriceData(this, symbol, currencies);
        cryptoCompareApiController.getHistoricalData(this, symbol);
    }

    @Override
    public void returnPriceData(Price price) {
        priceUSD.setText(Utils.formatCurrency(price.getUSD()) + " USD");
        priceCAD.setText(Utils.formatCurrency(price.getCAD()) + " CAD");
        priceBTC.setText(price.getBTC()+" BTC");
    }

    @Override
    public void returnHistoricalData(HistoricalData historicalData) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        HistoricalData.Data[] dataList = historicalData.getData();
        for (HistoricalData.Data data : dataList) {
            Entry entry = new Entry(Long.valueOf(data.getTime()), Float.valueOf(data.getClose()));
            values.add(entry);
        }
        LineDataSet dataSet = new LineDataSet(values, symbol);
        dataSet.setColor(getResources().getColor(R.color.colorAccentYellow));
        dataSet.setValueTextSize(getResources().getDimension(R.dimen.text_size_chart_values));
        dataSet.setCircleColor(getResources().getColor(R.color.primaryTextColor));
        dataSet.setValueTextColor(getResources().getColor(R.color.primaryTextColor));
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(getResources().getColor(R.color.colorAccentYellow));
        LineData lineData = new LineData(dataSet);

        priceChart.setData(lineData);
        priceChart.getLegend().setTextColor(getResources().getColor(R.color.primaryTextColor));
        Description description = new Description();
        description.setText("7D Price History for " + symbol);
        description.setTextColor(getResources().getColor(R.color.primaryTextColor));
        priceChart.setDescription(description);

        //Format xAxis
        XAxis xAxis = priceChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter());
        xAxis.setLabelCount(7, true);
        xAxis.setTextColor(getResources().getColor(R.color.primaryTextColor));

        //format yAxis color
        YAxis yAxisLeft = priceChart.getAxisLeft();
        yAxisLeft.setTextColor(getResources().getColor(R.color.primaryTextColor));

        YAxis yAxisRight = priceChart.getAxisRight();
        yAxisRight.setTextColor(getResources().getColor(R.color.primaryTextColor));
        priceChart.invalidate();
    }
}
