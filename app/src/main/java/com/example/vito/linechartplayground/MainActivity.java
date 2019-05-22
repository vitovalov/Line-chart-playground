package com.example.vito.linechartplayground;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.vito.linechartplayground.R;

import java.util.*;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    com.example.vito.linechartplayground.LineChartLayout linechart = findViewById(R.id.linechart);

    caseSimple(linechart);
  }

  private void case1(com.example.vito.linechartplayground.LineChartLayout linechart) {
    linechart.setBottomLeftText("Start");
    linechart.setBottomRightText("End");
    linechart.setBottomLeftmostText("70 °");
    linechart.setTopLeftmostText("-10 °");

    List<List<Float>> data = new ArrayList<>();
    // X       Y
    data.add(Arrays.asList(10f, 0f));
    data.add(Arrays.asList(30f, -10f));
    data.add(Arrays.asList(33f, 0f));
    data.add(Arrays.asList(40f, 70f));
    data.add(Arrays.asList(50f, 10f));

    List<Float> xRange = new ArrayList<>();
    xRange.add(10f); // minX
    xRange.add(100f);// maxX

    List<Float> yRange = new ArrayList<>();
    yRange.add(70f); //minY
    yRange.add(-10f); //maxY

    linechart.setPrimaryChartData(data, xRange, yRange);
    linechart.setPrimaryCharHorizontalLine(10, "GOAL", Color.parseColor("#7ED321"));
  }

  private void caseSimple(com.example.vito.linechartplayground.LineChartLayout linechart) {
    linechart.setBottomLeftText("Start");
    linechart.setBottomRightText("End");
    linechart.setBottomLeftmostText("0 °");
    linechart.setTopLeftmostText("10 °");

    List<List<Float>> data = new ArrayList<>();
    // X       Y
    data.add(Arrays.asList(0f, 1f));
    data.add(Arrays.asList(2f, 2f));
    data.add(Arrays.asList(3f, 3f));
    data.add(Arrays.asList(10f, 5f));

    List<Float> xRange = new ArrayList<>();
    xRange.add(0f); // minX
    xRange.add(10f);// maxX

    List<Float> yRange = new ArrayList<>();
    yRange.add(0f); //minY
    yRange.add(10f); //maxY

    linechart.setPrimaryChartData(data, xRange, yRange);
    linechart.setPrimaryCharHorizontalLine(4, "GOAL", Color.parseColor("#7ED321"));
  }

  private void case2(com.example.vito.linechartplayground.LineChartLayout linechart) {
    linechart.setBottomLeftText("Start");
    linechart.setBottomRightText("End");
    linechart.setBottomLeftmostText("0 °");
    linechart.setTopLeftmostText("200 °");

    List<List<Float>> data = new ArrayList<>();
    // X       Y
    data.add(Arrays.asList(10f, 0f));
    data.add(Arrays.asList(30f, 200f));
    data.add(Arrays.asList(33f, 0f));
    data.add(Arrays.asList(40f, 0f));
    data.add(Arrays.asList(50f, 10f));

    List<Float> xRange = new ArrayList<>();
    xRange.add(10f); // minX
    xRange.add(100f);// maxX

    List<Float> yRange = new ArrayList<>();
    yRange.add(0f); //minY
    yRange.add(200f); //maxY

    linechart.setPrimaryChartData(data, xRange, yRange);
    linechart.setPrimaryCharHorizontalLine(10, "GOAL", Color.parseColor("#7ED321"));
  }

  private void case3(com.example.vito.linechartplayground.LineChartLayout linechart) {
    linechart.setBottomLeftText("Start");
    linechart.setBottomRightText("End");
    linechart.setBottomLeftmostText("0 °");
    linechart.setTopLeftmostText("200 °");

    List<List<Float>> data = new ArrayList<>();
    // X       Y
    data.add(Arrays.asList(10f, 0f));
    data.add(Arrays.asList(30f, 200f));
    data.add(Arrays.asList(33f, 0f));
    data.add(Arrays.asList(40f, 0f));
    data.add(Arrays.asList(50f, 10f));

    linechart.setPrimaryChartData(data, null, null);
    linechart.setPrimaryCharHorizontalLine(10, "GOAL", Color.parseColor("#7ED321"));
  }

  private void case4(com.example.vito.linechartplayground.LineChartLayout linechart) {
    linechart.setBottomLeftText("Start");
    linechart.setBottomRightText("End");
    linechart.setBottomLeftmostText("0 °");
    linechart.setTopLeftmostText("200 °");

    List<List<Float>> data = new ArrayList<>();
    // X       Y
    data.add(Arrays.asList(10f, 0f));
    data.add(Arrays.asList(30f, 200f));
    data.add(Arrays.asList(33f, 0f));
    data.add(Arrays.asList(40f, 90f));
    data.add(Arrays.asList(50f, 10f));

    List<Float> xRange = new ArrayList<>();
    xRange.add(10f); // minX
    xRange.add(100f);// maxX

    List<Float> yRange = new ArrayList<>();
    yRange.add(0f); //minY
    yRange.add(200f); //maxY

    linechart.setPrimaryChartData(data, xRange, yRange);
    linechart.setPrimaryCharHorizontalLine(100, "GOAL", Color.parseColor("#7ED321"));

    List<List<Float>> data2 = new ArrayList<>();
    // X       Y
    data2.add(Arrays.asList(10f, 0f));
    data2.add(Arrays.asList(30f, 20f));
    data2.add(Arrays.asList(33f, 50f));
    data2.add(Arrays.asList(40f, 120f));
    data2.add(Arrays.asList(50f, 100f));

    List<Float> xRange2 = new ArrayList<>();
    xRange2.add(0f); // minX
    xRange2.add(100f);// maxX

    List<Float> yRange2 = new ArrayList<>();
    yRange2.add(20f); //minY
    yRange2.add(150f); //maxY

    linechart.setSecondaryChartData(data2, xRange2, yRange2);
    //linechart.setPrimaryCharHorizontalLine(10, "GOAL", Color.parseColor("#7ED321"));
  }
}
