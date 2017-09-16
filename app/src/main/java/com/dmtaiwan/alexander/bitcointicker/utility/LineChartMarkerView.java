package com.dmtaiwan.alexander.bitcointicker.utility;

import android.content.Context;
import android.widget.TextView;

import com.dmtaiwan.alexander.bitcointicker.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Created by Alexander on 9/16/2017.
 */

public class LineChartMarkerView extends MarkerView {

    private TextView tvContent;
    private String currency;

    public LineChartMarkerView(Context context, int layoutResource, String currency) {
        super(context, layoutResource);
        this.currency = currency;
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatCurrency(String.valueOf(ce.getHigh()), currency));
        } else {

            tvContent.setText("" + Utils.formatCurrency(String.valueOf(e.getY()), currency));
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}