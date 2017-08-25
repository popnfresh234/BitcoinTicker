package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.data.BitcoinDBContract;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Alexander on 8/25/2017.
 */

public class CoinRecyclerAdapter extends RecyclerView.Adapter<CoinRecyclerAdapter.CoinHolder> {

    private Context context;
    private Cursor cursor;

    public CoinRecyclerAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public CoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item_coin, parent, false);
        CoinHolder viewHolder = new CoinHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CoinHolder holder, int position) {

        //Move cursor to position
        cursor.moveToPosition(position);

        //Set colors
        if (cursor.getPosition() % 2 == 0) {
            holder.listRootView.setBackgroundColor(context.getResources().getColor(R.color.colorListViewAlternate));
        } else holder.listRootView.setBackgroundColor(Color.TRANSPARENT);

        //Handle expansion
        holder.listRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandableLayout.isExpanded()) {
                    holder.expandableLayout.collapse();
                } else holder.expandableLayout.expand();
            }
        });

        holder.coinName.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_NAME)));
        holder.symbol.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_SYMBOL)));

        //Format priceUSD
        String priceUsdString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_USD));
        holder.priceUSD.setText(formatCurrency(priceUsdString));


        holder.priceBTC.setText(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_BTC)));

        //Format 24HVolumeUSD
        String twentyFourHourVolumeUSDString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_USD));
        holder.twentyFourHourVolumeUSD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeUSDString));

        //Format marketCapUSD
        String marketCapUsdString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_USD));
        holder.marketCapUSD.setText(formatCurrencyVolumeOrCap(marketCapUsdString));

        //Format availableSupply
        String availableSupplyString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_AVAILABLE_SUPPLY));
        holder.availableSupply.setText(formatSupply(availableSupplyString));

        //Format totalSupply
        String totalSupplyString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_TOTAL_SUPPLY));
        holder.totalSupply.setText(formatSupply(totalSupplyString));


        //Format percentChange1H
        String percentChangeOneHourString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_1H));
        holder.percentChangeOneHour.setText(formatPercentage(percentChangeOneHourString));

        //Format percentChange24H
        String percentChangeTwentyFourHourString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_24H));
        holder.percentChangeTwentyFourHour.setText(formatPercentage(percentChangeTwentyFourHourString));

        //Format percentChange7D
        String percentChangeSevenDaysString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PERCENT_CHANGE_7D));
        holder.percentChangeSevenDays.setText(formatPercentage(percentChangeSevenDaysString));

        //Parse date
        Long date = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_LAST_UPDATED)));
        holder.lastUpdated.setText(getDate(date));

        //Parse priceCAD
        String priceCadString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_PRICE_CAD));
        holder.priceCAD.setText(formatCurrency(priceCadString));

        //Format 24HVolumeCAD
        String twentyFourHourVolumeCADString = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_24H_VOLUME_CAD));
        holder.twentyFourHourVolumeCAD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeCADString));

        //Format marketCapCad
        String marketCapCad = cursor.getString(cursor.getColumnIndexOrThrow(BitcoinDBContract.BitcoinEntry.COLUMN_MARKET_CAP_CAD));
        holder.marketCapCAD.setText(formatCurrencyVolumeOrCap(marketCapCad));

    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class CoinHolder extends RecyclerView.ViewHolder {

        public LinearLayout listRootView;
        public ExpandableLayout expandableLayout;
        public TextView coinName;
        public TextView symbol;
        public TextView priceUSD;
        public TextView priceBTC;
        public TextView twentyFourHourVolumeUSD;
        public TextView marketCapUSD;
        public TextView availableSupply;
        public TextView totalSupply;
        public TextView percentChangeOneHour;
        public TextView percentChangeTwentyFourHour;
        public TextView percentChangeSevenDays;
        public TextView lastUpdated;
        public TextView priceCAD;
        public TextView twentyFourHourVolumeCAD;
        public TextView marketCapCAD;

        public CoinHolder(View itemView) {
            super(itemView);
            listRootView = (LinearLayout) itemView.findViewById(R.id.coin_list_item_root_view);
            expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
            coinName = (TextView) itemView.findViewById(R.id.text_view_name);
            symbol = (TextView) itemView.findViewById(R.id.text_view_symbol);
            priceUSD = (TextView) itemView.findViewById(R.id.text_view_price_usd);
            priceBTC = (TextView) itemView.findViewById(R.id.text_view_price_btc);
            twentyFourHourVolumeUSD = (TextView) itemView.findViewById(R.id.text_view_twenty_four_hour_volume_usd);
            marketCapUSD = (TextView) itemView.findViewById(R.id.text_view_market_cap_usd);
            availableSupply = (TextView) itemView.findViewById(R.id.text_view_available_supply);
            totalSupply = (TextView) itemView.findViewById(R.id.text_view_total_supply);
            percentChangeOneHour = (TextView) itemView.findViewById(R.id.text_view_percent_change_one_hour);
            percentChangeTwentyFourHour = (TextView) itemView.findViewById(R.id.text_view_percent_change_twenty_four_hour);
            percentChangeSevenDays = (TextView) itemView.findViewById(R.id.text_view_percent_change_seven_days);
            lastUpdated = (TextView) itemView.findViewById(R.id.text_view_last_updated);
            priceCAD = (TextView) itemView.findViewById(R.id.text_view_price_cad);
            twentyFourHourVolumeCAD = (TextView) itemView.findViewById(R.id.text_view_twenty_four_hour_volume_cad);
            marketCapCAD = (TextView) itemView.findViewById(R.id.text_view_market_cap_cad);
        }
    }

    private String getDate(long time) {
        Date date = new Date(time * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm:ss a");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Vancouver"));
        String formattedTime = sdf.format(date);
        return formattedTime;
    }

    public String formatCurrency(String input) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);
        return DecimalFormat.getCurrencyInstance().format(floatPrice);
    }

    public String formatCurrencyVolumeOrCap(String input) {
        if (input == null) {
            return "";
        }
        float floatPrice = Float.parseFloat(input);
        String formattedPrice = DecimalFormat.getCurrencyInstance().format(floatPrice);
        return formattedPrice.replaceAll("\\.0*$", "");
    }

    private String formatPercentage(String input) {
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

    private String formatSupply(String input) {
        if (input == null) {
            return "";
        }
        float floatSupply = Float.parseFloat(input);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        return decimalFormat.format(floatSupply);
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}
