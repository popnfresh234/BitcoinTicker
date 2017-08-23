package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Alexander on 8/20/2017.
 */

public class CoinCursorAdapter extends CursorAdapter {

    public CoinCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_coin, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.text_view_name);
        TextView symbol = (TextView)view.findViewById(R.id.text_view_symbol);
        TextView priceUSD = (TextView)view.findViewById(R.id.text_view_price_usd);
        TextView priceBTC = (TextView)view.findViewById(R.id.text_view_price_btc);
        TextView twentyFourHourVolumeUSD = (TextView)view.findViewById(R.id.text_view_twenty_four_hour_volume_usd);
        TextView marketCapUSD = (TextView)view.findViewById(R.id.text_view_market_cap_usd);
        TextView availableSupply = (TextView)view.findViewById(R.id.text_view_available_supply);
        TextView totalSupply = (TextView)view.findViewById(R.id.text_view_total_supply);
        TextView percentChangeOneHour = (TextView)view.findViewById(R.id.text_view_percent_change_one_hour);
        TextView percentChangeTwentyFourHour = (TextView)view.findViewById(R.id.text_view_percent_change_twenty_four_hour);
        TextView percentChangeSevenDays = (TextView)view.findViewById(R.id.text_view_percent_change_seven_days);
        TextView lastUpdated = (TextView)view.findViewById(R.id.text_view_last_updated);
        TextView priceCAD = (TextView)view.findViewById(R.id.text_view_price_cad);
        TextView twentyFourHourVolumeCAD = (TextView)view.findViewById(R.id.text_view_twenty_four_hour_volume_cad);
        TextView marketCapCAD = (TextView)view.findViewById(R.id.text_view_market_cap_cad);

        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        symbol.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL)));

        //Format priceUSD
        String priceUsdString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD));
        priceUSD.setText(formatCurrency(priceUsdString));


        priceBTC.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC)));

        //Format 24HVolumeUSD
        String twentyFourHourVolumeUSDString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD));
        twentyFourHourVolumeUSD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeUSDString));

        //Format marketCapUSD
        String marketCapUsdString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
        marketCapUSD.setText(formatCurrencyVolumeOrCap(marketCapUsdString));

        //Format availableSupply
        String availableSupplyString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY));
        availableSupply.setText(formatSupply(availableSupplyString));

        //Format totalSupply
        String totalSupplyString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY));
        totalSupply.setText(formatSupply(totalSupplyString));


        //Format percentChange1H
        String percentChangeOneHourString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H));
        percentChangeOneHour.setText(formatPercentage(percentChangeOneHourString));

        //Format percentChange24H
        String percentChangeTwentyFourHourString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H));
        percentChangeTwentyFourHour.setText(formatPercentage(percentChangeTwentyFourHourString));

        //Format percentChange7D
        String percentChangeSevenDaysString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D));
        percentChangeSevenDays.setText(formatPercentage(percentChangeSevenDaysString));

        //Parse date
        Long date = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED)));
        lastUpdated.setText(getDate(date));

        //Parse priceCAD
        String priceCadString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD));
        priceCAD.setText(formatCurrency(priceCadString));

        //Format 24HVolumeCAD
        String twentyFourHourVolumeCADString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD));
        twentyFourHourVolumeCAD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeCADString));

        //Format marketCapCad
        String marketCapCad = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD));
        marketCapCAD.setText(formatCurrencyVolumeOrCap(marketCapCad));


    }

    private String getDate(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    public String formatCurrency(String input) {
        float floatPrice = Float.parseFloat(input);
        return DecimalFormat.getCurrencyInstance().format(floatPrice);
    }

    public String formatCurrencyVolumeOrCap(String input) {
        float floatPrice = Float.parseFloat(input);
        String formattedPrice = DecimalFormat.getCurrencyInstance().format(floatPrice);
        return formattedPrice.replaceAll("\\.0*$", "");
    }

    private String formatPercentage(String input) {
        float floatPercentage = Float.parseFloat(input);
        DecimalFormat df2;
        if (floatPercentage > 0) {
            df2 = new DecimalFormat("+#,##0.00 '%'");
        } else {
            df2 = new DecimalFormat(" #,##0.00 '%'");
        }

        return df2.format(floatPercentage);
    }

    private String formatSupply(String input) {
        float floatSupply = Float.parseFloat(input);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(floatSupply);
    }
}
