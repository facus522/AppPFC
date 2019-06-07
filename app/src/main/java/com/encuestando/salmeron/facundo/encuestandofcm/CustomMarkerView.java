package com.encuestando.salmeron.facundo.encuestandofcm;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class CustomMarkerView extends MarkerView {

    private TextView tvContent;

    public CustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = (TextView) findViewById(R.id.tvContent);

    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {



        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
        } else {
            if (e instanceof PieEntry) {
                tvContent.setText(((PieEntry) e).getLabel());
                tvContent.setTextColor(Color.BLACK);
            } else {
                tvContent.setText(e.getData().toString());
            }

        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}