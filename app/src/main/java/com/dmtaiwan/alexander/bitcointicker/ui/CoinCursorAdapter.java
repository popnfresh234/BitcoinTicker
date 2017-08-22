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
        TextView id = view.findViewById(R.id.text_view_coin_id);
        TextView name = view.findViewById(R.id.text_view_name);
        TextView symbol = view.findViewById(R.id.text_view_symbol);
        TextView rank = view.findViewById(R.id.text_view_rank);
        TextView priceUSD = view.findViewById(R.id.text_view_price_usd);
        TextView priceBTC = view.findViewById(R.id.text_view_price_btc);
        TextView twentyFourHourVolumeUSD = view.findViewById(R.id.text_view_twenty_four_hour_volume_usd);
        TextView marketCapUSD = view.findViewById(R.id.text_view_market_cap_usd);
        TextView availableSupply = view.findViewById(R.id.text_view_available_supply);
        TextView totalSupply = view.findViewById(R.id.text_view_total_supply);
        TextView percentChangeOneHour = view.findViewById(R.id.text_view_percent_change_one_hour);
        TextView percentChangeTwentyFourHour = view.findViewById(R.id.text_view_percent_change_twenty_four_hour);
        TextView percentChangeSevenDays = view.findViewById(R.id.text_view_percent_change_seven_days);
        TextView lastUpdated = view.findViewById(R.id.text_view_last_updated);
        TextView priceCAD = view.findViewById(R.id.text_view_price_cad);
        TextView twentyFourHourVolumeCAD = view.findViewById(R.id.text_view_twenty_four_hour_volume_cad);
        TextView marketCapCAD = view.findViewById(R.id.text_view_market_cap_cad);

        id.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_COIN_ID)));
        name.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        symbol.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL)));
        rank.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_RANK)));
        priceUSD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD)));
        priceBTC.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC)));
        twentyFourHourVolumeUSD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD)));
        marketCapUSD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD)));
        availableSupply.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY)));
        totalSupply.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY)));
        percentChangeOneHour.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H)));
        percentChangeTwentyFourHour.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H)));
        percentChangeSevenDays.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D)));

        //Parse date
        Long date = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED)));
        lastUpdated.setText(getDate(date));
        priceCAD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD)));
        twentyFourHourVolumeCAD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD)));
        marketCapCAD.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD)));


    }

    private String getDate(long time) {
        Date date = new Date(time*1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }
}
