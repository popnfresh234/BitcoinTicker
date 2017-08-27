package com.dmtaiwan.alexander.bitcointicker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.dmtaiwan.alexander.bitcointicker.helper.ItemTouchHelperAdapter;
import com.dmtaiwan.alexander.bitcointicker.model.Coin;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Collections;

import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.formatCurrency;
import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.formatCurrencyVolumeOrCap;
import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.formatPercentage;
import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.formatSupply;
import static com.dmtaiwan.alexander.bitcointicker.utility.Utils.getDate;

/**
 * Created by Alexander on 8/25/2017.
 */

public class CoinRecyclerAdapter extends RecyclerView.Adapter<CoinRecyclerAdapter.CoinHolder> implements ItemTouchHelperAdapter {

    private Context context;
    private ArrayList<Coin> coins;
    private AdapterCallback listener;

    public CoinRecyclerAdapter(Context context, ArrayList<Coin> coins, AdapterCallback listener) {

        this.context = context;
        this.coins = coins;
        this.listener = listener;
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
            holder.listRootView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ripple_dark));
        } else holder.listRootView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ripple_light));

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

        //Share
        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildShareIntent(position);
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(coins, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(coins, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        coins.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onSwipe(int position) {

        Coin coin = coins.get(position);
        String coinId = coin.getId();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(coinId).commit();
        //requery to update adapter
        listener.queryData();
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
        public ImageView shareIcon;

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
            shareIcon = (ImageView) itemView.findViewById(R.id.share_icon);
        }
    }



    private void buildShareIntent(int position) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, coins.get(position).toString());
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, "TEST");
        if (sendIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(chooser);
        }
    }

    public void swapData(ArrayList<Coin> coins) {
        this.coins = coins;
        notifyDataSetChanged();
    }

    public interface AdapterCallback {
        void queryData();
    }

}
