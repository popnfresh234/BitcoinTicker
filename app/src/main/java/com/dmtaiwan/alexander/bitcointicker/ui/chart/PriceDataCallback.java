package com.dmtaiwan.alexander.bitcointicker.ui.chart;

import com.dmtaiwan.alexander.bitcointicker.model.Price;

/**
 * Created by Alexander on 9/1/2017.
 */

public interface PriceDataCallback {
    void returnPriceData(Price price);
}
