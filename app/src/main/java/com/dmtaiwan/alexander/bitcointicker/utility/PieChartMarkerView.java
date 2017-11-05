package com.dmtaiwan.alexander.bitcointicker.utility;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Alexander on 9/16/2017.
 */

public class PieChartMarkerView extends MarkerView {

    private PieChart chart;

    private TextView coinNameTextView;
    private TextView marketCapTextView;

    public PieChartMarkerView(Context context, int layoutResource, PieChart chart) {

        super(context, layoutResource);
        this.chart = chart;
        marketCapTextView = (TextView) findViewById(R.id.pie_chart_marker_market_cap);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        PieEntry entry = (PieEntry) chart.getData().getEntryForHighlight(highlight);
        Log.i("YESSSS", entry.getLabel());
        marketCapTextView.setText(entry.getLabel() +": " + Utils.formatPercentage(String.valueOf(e.getY())));
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}