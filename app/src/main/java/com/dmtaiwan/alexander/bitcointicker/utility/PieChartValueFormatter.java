package com.dmtaiwan.alexander.bitcointicker.utility;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 9/3/2017.
 */

public class PieChartValueFormatter implements IValueFormatter {

    protected DecimalFormat decimalFormat;

    public PieChartValueFormatter() {
        decimalFormat = new DecimalFormat("###,###,##0.0");
    }

    @Override

    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value < 2f) {
            return "";
        } else return decimalFormat.format(value)+ " %";
    }
}
