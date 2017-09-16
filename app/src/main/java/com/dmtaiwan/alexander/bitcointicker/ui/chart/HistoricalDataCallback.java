package com.dmtaiwan.alexander.bitcointicker.ui.chart;

        import com.dmtaiwan.alexander.bitcointicker.model.HistoricalData;

/**
 * Created by Alexander on 8/31/2017.
 */

public interface HistoricalDataCallback {
    void returnHistoricalData(HistoricalData historicalData, int period, String currency);
}
