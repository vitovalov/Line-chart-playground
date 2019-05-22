package com.example.vito.linechartplayground;


import android.content.Context;
import android.support.annotation.ColorInt;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.*;

public class LineChartLayout extends FrameLayout {

  public static final String DEGREE = "°";
  private static final int LAYOUT_ID = R.layout.linechart_view;
  private LineChartView linelinechartView;
  private TextView topLeftTv;
  private TextView topLeftmostTv;
  private TextView loadingTv;
  private TextView topRightTv;
  private TextView bottomLeftTv;
  private TextView bottomLeftmostTv;
  private TextView bottomRightTv;
  private TextView topRightmostTv;

  public void setPrimaryChartData(List<List<Float>> data, List<Float> xRange, List<Float> yRange) {
    linelinechartView.setPrimaryData(data, xRange, yRange);
  }

  public void setPrimaryChartVerticalLine(float xValue, String text, @ColorInt int color) {
    linelinechartView.setPrimaryVerticalLine(xValue, text, color);
  }

  public void setPrimaryCharHorizontalLine(float yValue, String text, @ColorInt int color) {
    linelinechartView.setPrimaryHorizontalLine(yValue, text, color);
  }

  public void setSecondaryChartData(List<List<Float>> data, List<Float> xRange, List<Float> yRange) {
    linelinechartView.setSecondaryData(data, xRange, yRange);
  }

  public void setTopLeftText(String text) {
    if (text != null) {
      this.topLeftTv.setText(text);
    }
  }

  public void setTopRightText(String text) {
    if (text != null) {
      this.topRightTv.setText(cleanText(text));
    }
  }

  public void setTopLeftmostText(int value, boolean useDegree) {
    this.topLeftmostTv.setText(String.valueOf(value) + (useDegree ? DEGREE : ""));
  }

  public void setTopRightmostText(int value, boolean useDegree) {
    this.topRightmostTv.setText(String.valueOf(value) + (useDegree ? DEGREE : ""));
  }

  public void setBottomLeftmostText(int value, boolean useDegree) {
    this.bottomLeftmostTv.setText(String.valueOf(value) + (useDegree ? DEGREE : ""));
    //if (text != null) {
    //  this.bottomLeftmostTv.setText(cleanText(text));
    //}
  }

  public void setTopLeftmostText(String text) {
    this.topLeftmostTv.setText(text);
  }

  public void setBottomLeftmostText(String text) {
    this.bottomLeftmostTv.setText(text);
  }
  public void setBottomLeftText(String text) {
    if (text != null) {
      this.bottomLeftTv.setText(text);
    }
  }

  public void setBottomRightText(String text) {
    if (text != null) {
      this.bottomRightTv.setText(text);
    }
  }

  public void setSecondaryChartColor(int color) {
    linelinechartView.setChartSecondaryBasicColor(color);
  }

  public void setColors(@ColorInt int barColor, @ColorInt int bottomBarColor, @ColorInt int basicChartColor, @ColorInt int circleFillColor) {
    linelinechartView.setBarColor(barColor);
    linelinechartView.setDarkGreyColor(bottomBarColor);
    linelinechartView.setChartPrimaryBasicColor(basicChartColor);
    linelinechartView.setChartCircleFillColor(circleFillColor);
  }

  public void showLoadingMessage(String text) {
    if (!TextUtils.isEmpty(text)) {
      loadingTv.setText(text);
    }
  }

  public LineChartLayout(Context context) {
    super(context);
    init(context, null);
  }

  public LineChartLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public LineChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    loadView(context);

    linelinechartView = (LineChartView) findViewById(R.id.line_linechart_view);
    topLeftTv = (TextView) findViewById(R.id.linechart_view_top_left_tv);
    topLeftmostTv = (TextView) findViewById(R.id.linechart_view_top_leftmost_tv);
    topRightTv = (TextView) findViewById(R.id.linechart_view_top_right_tv);
    bottomLeftTv = (TextView) findViewById(R.id.linechart_view_bottom_left_tv);
    bottomLeftmostTv = (TextView) findViewById(R.id.linechart_view_bottom_leftmost_tv);
    bottomRightTv = (TextView) findViewById(R.id.linechart_view_bottom_right_tv);
    loadingTv = (TextView) findViewById(R.id.linechart_view_loading_msg_tv);
    topRightmostTv = (TextView) findViewById(R.id.linechart_view_top_rightmost_tv);
  }

  private View loadView(Context context) {
    return LayoutInflater.from(context).inflate(LAYOUT_ID, this);
  }

  private String cleanText(String text) {
    if (text == null) {
      text = "-";
    } else {
      text = text.replaceAll("-", " ");
      text = text.substring(0, 1).toUpperCase() + text.substring(1);
    }
    return text;
  }
}
