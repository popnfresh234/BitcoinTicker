package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.model.Coin;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Alexander on 8/25/2017.
 */

public class CoinRecyclerAdapter extends RecyclerView.Adapter<CoinRecyclerAdapter.CoinHolder> {

    private Context context;
    private ArrayList<Coin> coins;

    public CoinRecyclerAdapter(Context context, ArrayList<Coin> coins) {
        this.context = context;
        this.coins = coins;
    }

    @Override
    public CoinHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.list_item_coin, parent, false);
        CoinHolder viewHolder = new CoinHolder(rootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CoinHolder holder, final int position) {

        //Get coin
        final Coin coin = coins.get(position);

        //handle expansion state
        if (coin.getIsExpanded()) {
            holder.expandableLayout.expand();
        }else holder.expandableLayout.collapse();

        //Set colors
        if (position % 2 == 0) {
            holder.listRootView.setBackgroundColor(context.getResources().getColor(R.color.colorListViewAlternate));
        } else holder.listRootView.setBackgroundColor(Color.TRANSPARENT);

        //Handle expansion
        holder.listRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandableLayout.isExpanded()) {
                    holder.expandableLayout.collapse();
                    coin.setIsExpanded(false);
                } else{
                    holder.expandableLayout.expand();
                    coin.setIsExpanded(true);
                }
            }
        });

        holder.coinName.setText(coin.getName());
        holder.symbol.setText(coin.getSymbol());

        //Format priceUSD
        String priceUsdString = coin.getPrice_usd();
        holder.priceUSD.setText(formatCurrency(priceUsdString));


        holder.priceBTC.setText(coin.getPrice_btc());

        //Format 24HVolumeUSD
        String twentyFourHourVolumeUSDString = coin.getTwenty_four_hour_volume_usd();
        holder.twentyFourHourVolumeUSD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeUSDString));

        //Format marketCapUSD
        String marketCapUsdString = coin.getMarket_cap_usd();
        holder.marketCapUSD.setText(formatCurrencyVolumeOrCap(marketCapUsdString));

        //Format availableSupply
        String availableSupplyString = coin.getAvailable_supply();
        holder.availableSupply.setText(formatSupply(availableSupplyString));

        //Format totalSupply
        String totalSupplyString = coin.getTotal_supply();
        holder.totalSupply.setText(formatSupply(totalSupplyString));


        //Format percentChange1H
        String percentChangeOneHourString = coin.getPercent_change_one_hour();
        holder.percentChangeOneHour.setText(formatPercentage(percentChangeOneHourString));

        //Format percentChange24H
        String percentChangeTwentyFourHourString = coin.getPercent_change_twenty_four_hour();
        holder.percentChangeTwentyFourHour.setText(formatPercentage(percentChangeTwentyFourHourString));

        //Format percentChange7D
        String percentChangeSevenDaysString = coin.getPercent_change_seven_days();
        holder.percentChangeSevenDays.setText(formatPercentage(percentChangeSevenDaysString));

        //Parse date
        Long date = Long.parseLong(coin.getLast_updated());
        holder.lastUpdated.setText(getDate(date));

        //Parse priceCAD
        String priceCadString = coin.getPrice_cad();
        holder.priceCAD.setText(formatCurrency(priceCadString));

        //Format 24HVolumeCAD
        String twentyFourHourVolumeCADString =coin.getTwenty_four_hour_volume_cad();
        holder.twentyFourHourVolumeCAD.setText(formatCurrencyVolumeOrCap(twentyFourHourVolumeCADString));

        //Format marketCapCad
        String marketCapCad = coin.getMarket_cap_cad();
        holder.marketCapCAD.setText(formatCurrencyVolumeOrCap(marketCapCad));

    }

    @Override
    public int getItemCount() {
        if (coins == null) {
            return 0;
        }
        return coins.size();
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

    public void swapData(ArrayList<Coin> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }
}
