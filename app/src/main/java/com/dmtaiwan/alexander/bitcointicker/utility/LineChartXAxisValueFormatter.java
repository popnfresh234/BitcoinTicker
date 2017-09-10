package com.dmtaiwan.alexander.bitcointicker.utility;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Alexander on 9/1/2017.
 */

public class LineChartXAxisValueFormatter implements IAxisValueFormatter {

    private String timeZone;

    public LineChartXAxisValueFormatter(String timeZone) {
        this.timeZone = timeZone;
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        //TODO implement timezone
        String date = Utils.getDateForChart((long) value, "America/Vancouver");
        return date;
    }
}
