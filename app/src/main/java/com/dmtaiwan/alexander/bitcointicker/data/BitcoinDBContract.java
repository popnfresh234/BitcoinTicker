package com.dmtaiwan.alexander.bitcointicker.data;

import android.provider.BaseColumns;

/**
 * Created by Alexander on 8/20/2017.
 */

public class BitcoinDBContract {
    //private constructor so this class isn't accidentally instantiated
    private BitcoinDBContract() {
    }

    public static final class BitcoinEntry implements BaseColumns{
        public static String TABLE_NAME = "coins";

        public static final String COLUMN_COIN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SYMBOL = "symbol";
        public static final String COLUMN_RANK = "rank";
        public static final String COLUMN_PRICE_USD = "price_usd";
        public static final String COLUMN_PRICE_BTC = "price_btc";
        public static final String COLUMN_24H_VOLUME_USD = "twenty_four_hour_volume_usd";
        public static final String COLUMN_MARKET_CAP_USD = "market_cap_usd";
        public static final String COLUMN_AVAILABLE_SUPPLY = "available_supply";
        public static final String COLUMN_TOTAL_SUPPLY = "total_supply";
        public static final String COLUMN_PERCENT_CHANGE_1H = "percent_change_one_hour";
        public static final String COLUMN_PERCENT_CHANGE_24H = "percent_change_twenty_four_hour";
        public static final String COLUMN_PERCENT_CHANGE_7D = "percent_change_seven_day";
        public static final String COLUMN_LAST_UPDATED = "last_updated";
        public static final String COLUMN_PRICE_CAD = "price_cad";
        public static final String COLUMN_24H_VOLUME_CAD = "twenty_four_hour_volume_cad";
        public static final String COLUMN_MARKET_CAP_CAD = "market_cap_cad";
    }
}


