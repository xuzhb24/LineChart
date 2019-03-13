package com.xuzhb.linechart;

import android.app.Activity;
import android.os.Bundle;

import com.elestic.LineChart;

public class MainActivity extends Activity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lineChart = (LineChart) findViewById(R.id.lineChart);
        float[] yValue = {16, 11.9f, 8.2f, 9, 12};
        String[] dates = {"01/24", "02/02", "02/12", "02/25", "03/01"};
        lineChart.drawData(yValue, dates);
    }
}
