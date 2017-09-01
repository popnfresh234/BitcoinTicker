package com.dmtaiwan.alexander.bitcointicker.utility;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Alexander on 9/1/2017.
 */

public class XAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        String date = Utils.getDateForChart((long) value);
        return date;
    }
}
