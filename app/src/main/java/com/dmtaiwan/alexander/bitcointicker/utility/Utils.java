package com.dmtaiwan.alexander.bitcointicker.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;
import com.dmtaiwan.alexander.bitcointicker.model.Coin;
import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;
import com.dmtaiwan.alexander.bitcointicker.model.Position;
import com.dmtaiwan.alexander.bitcointicker.ui.settings.SettingsActivity;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Alexander on 8/26/2017.
 */

public class Utils {

    public static String getDate(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    public static String getDateForChart(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }


    public static String formatCurrency(String input, int preferredCurrency) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);
        NumberFormat formatter;
        //Get proper currency formatter
        switch (preferredCurrency) {
            case SettingsActivity.EUR:
                formatter = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
                break;
            default:
                formatter = DecimalFormat.getCurrencyInstance();
        }
        return formatter.format(floatPrice);
    }

    public static String formatCurrencyVolumeOrCap(String input, int preferredCurrency) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);

        NumberFormat formatter;
        //Get proper currency formatter
        switch (preferredCurrency) {
            case SettingsActivity.EUR:
                formatter = DecimalFormat.getCurrencyInstance(Locale.GERMANY);
                break;
            default:
                formatter = DecimalFormat.getCurrencyInstance();
        }
        String formattedPrice =  formatter.format(floatPrice);
        return formattedPrice.replaceAll("\\.0*$", "");
    }

    public static String formatPercentage(String input) {
        if (input == null) {
            return "";
        }
        float floatPercentage = Float.parseFloat(input);
        DecimalFormat df2;
        if (floatPercentage > 0) {
            df2 = new DecimalFormat("+#,##0.00 '%'");
        } else {
            df2 = new DecimalFormat(" #,##0.00 '%'");
        }


        return df2.format(floatPercentage);
    }

    public static String formatSupply(String input) {
        if (input == null) {
            return "";
        }
        float floatSupply = Float.parseFloat(input);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(floatSupply);
    }

    //Extract an ArrayList<Coins> from a Cursor
    public static ArrayList<Coin> stripCursor(Cursor cursor) {
        ArrayList<Coin> coinList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID));
            String name = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_NAME));
            String symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL));
            String rank = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_RANK));
            String priceUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD));
            String priceBtc = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC));
            String twentyFourHourVolumeUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD));
            String marketCapUsd = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
            String availableSupply = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY));
            String totalSupply = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY));
            String percentChangeOneHour = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H));
            String percentChangeTwentyFourHour = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H));
            String percentChangeSevenDays = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D));
            String lastUpdated = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED));
            String priceCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD));
            String twentyFourHourVolumeCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD));
            String marketCapCad = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD));
            String priceEur = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_EUR));
            String twentyFourHourVolumeEur = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_EUR));
            String marketCapEur = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_EUR));

            Coin coin = new Coin(id, name, symbol, rank, priceUsd, priceBtc, twentyFourHourVolumeUsd,
                    marketCapUsd, availableSupply, totalSupply, percentChangeOneHour, percentChangeTwentyFourHour,
                    percentChangeSevenDays, lastUpdated, priceCad, twentyFourHourVolumeCad, marketCapCad, priceEur, twentyFourHourVolumeEur, marketCapEur, false);

            coinList.add(coin);
        }
        return coinList;
    }

    public static ArrayList<Position> extractPositionsFromCursor(Cursor cursor) {
        ArrayList<Position> positions = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
            String symbol = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.PositionEntry.COLUMN_SYMBOL));
            String positionFloat = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.PositionEntry.COLUMN_POSITION));
            String priceFloat = cursor.getString(cursor.getColumnIndex(BitcoinDBContract.PositionEntry.COLUMN_PRICE));
            Position position = new Position(id, symbol, positionFloat, priceFloat);
            positions.add(position);
        }
        return positions;
    }

    //Get array list of coin names user has selected
    public static ArrayList<String> getStrings(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
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
        return prefStrings;
    }

    //Create data for chart
    public static LineData createChartLineData(Context context, HistoricalData historicalData, String symbol) {
        ArrayList<Entry> values = new ArrayList<Entry>();
        HistoricalData.Data[] dataList = historicalData.getData();

        //Create entries for chart from historical data
        for (HistoricalData.Data data : dataList) {
            Entry entry = new Entry(Long.valueOf(data.getTime()), Float.valueOf(data.getClose()));
            values.add(entry);
        }

        //Create a data set and format data
        LineDataSet dataSet = new LineDataSet(values, symbol);
        dataSet.setColor(context.getResources().getColor(R.color.colorAccentYellow));
        dataSet.setValueTextSize(context.getResources().getDimension(R.dimen.text_size_line_chart_values));
        dataSet.setDrawCircles(false);
        dataSet.setValueTextColor(context.getResources().getColor(R.color.primaryTextColor));
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(context.getResources().getColor(R.color.colorAccentYellow));
        LineData lineData = new LineData(dataSet);
        return lineData;
    }
}
