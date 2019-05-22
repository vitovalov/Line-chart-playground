package com.example.vito.linechartplayground;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import java.util.*;

public class LineChartView extends View {

  private static final int defaultGreenColor = Color.parseColor("#7ED321");
  private static final int defaultRedColor = Color.parseColor("#FF5060");
  private static final int defaultBlueColor = Color.parseColor("#61B4FF");
  public static final int MIN_BARS = 10;
  public static final int MAX_BARS = 20;
  private int chartCircleFillColor = Color.WHITE;
  private int barColor = Color.GRAY;
  private int darkGreyColor = Color.DKGRAY;
  private float chartLineWidth;
  private float circleDiameter;
  private float chartHorizontalOffset;
  private float chartHorizontalBarOffset;
  private float chartVerticalOffset;
  private boolean barsEnabled;
  private boolean horizontalLinesEnabled = true;
  private int viewWidth;
  private int viewHeight;
  private Paint darkGreyPaint = new Paint();
  private Paint greyPaint = new Paint();
  private Paint circleWhitePaint = new Paint();
  private float barLeft;

  //private boolean primaryMinMaxFound;
  private int chartPrimaryBasicColor = defaultBlueColor;
  private Paint primaryLinePaint = new Paint();
  //private Float primaryMinX;
  //private Float primaryMaxX;
  //private Float primaryMinY;
  //private Float primaryMaxY;
  private float primaryLineStartX;
  private float primaryLineStartY;
  private int primaryBarCount;
  private float primaryIncrement;

  private List<Float> primaryXRange;
  private List<Float> primaryYRange;
  private List<List<Float>> primaryValues;

  private Float primaryHorizontalLineValue;
  private String primaryHorizontalLineLabel;
  private int primaryHorizontalLineColor;
  private Paint primaryHorizontalLinePaint = new Paint();

  private int chartSecondaryBasicColor = Color.MAGENTA;
  private Paint secondaryLinePaint = new Paint();
  private float secondaryLineStartX;
  private float secondaryLineStartY;
  private int secondaryBarCount;
  private float secondaryIncrement;
  private List<Float> secondaryXRange;
  private List<Float> secondaryYRange;
  private List<List<Float>> secondaryValues;
  private float horizontalLineWidth;

  private List<Float> rangeX() { // same scale
    if (primaryXRange != null && secondaryXRange == null) {
      return primaryXRange;
    } else if (primaryXRange == null && secondaryXRange != null) {
      return secondaryXRange;
    } else if (primaryXRange == null && secondaryXRange == null) {
      List<Float> range = new ArrayList<>();
      range.add(0f);
      range.add(0f);
      return range;
    } else {
      List<Float> range = new ArrayList<>();
      range.add(Math.max(primaryXRange.get(0), secondaryXRange.get(0)));
      range.add(Math.max(primaryXRange.get(1), secondaryXRange.get(1)));
      return range;
    }
  }

  private List<Float> rangeY() { // same scale
    if (primaryYRange != null && secondaryYRange == null) {
      return primaryYRange;
    } else if (primaryYRange == null && secondaryYRange != null) {
      return secondaryYRange;
    } else if (primaryYRange == null && secondaryYRange == null) {
      List<Float> range = new ArrayList<>();
      range.add(0f);
      range.add(0f);
      return range;
    } else {
      List<Float> range = new ArrayList<>();
      range.add(Math.max(primaryYRange.get(0), secondaryYRange.get(0)));
      range.add(Math.max(primaryYRange.get(1), secondaryYRange.get(1)));
      return range;
    }
  }

  //region public API
  public void setPrimaryData(List<List<Float>> data, List<Float> xRange, List<Float> yRange) {
    this.primaryValues = data;
    this.primaryXRange = xRange;
    this.primaryYRange = yRange;
    if (xRange == null || yRange == null) {
      // iterate points and def xrange
      float primaryMinX = Float.MAX_VALUE;
      float primaryMaxX = Float.MIN_VALUE;
      float primaryMinY = Float.MAX_VALUE;
      float primaryMaxY = Float.MIN_VALUE;

      for (List<Float> value : primaryValues) {
        primaryMinX = Math.min(value.get(0), primaryMinX);
        primaryMaxX = Math.max(value.get(0), primaryMaxX);
        if (value.get(1) != null) {
          primaryMinY = Math.min(value.get(1), primaryMinY);
          primaryMaxY = Math.max(value.get(1), primaryMaxY);
        }
      }

      if (xRange == null) {
        primaryXRange = new ArrayList<Float>();
        primaryXRange.add(primaryMinX);
        primaryXRange.add(primaryMaxX);
      }
      if (yRange == null) {
        primaryYRange = new ArrayList<>();
        primaryYRange.add(primaryMinY);
        primaryYRange.add(primaryMaxY);
      }
    }

    primaryBarCount = Math.min(Math.max(MIN_BARS, primaryValues.size()), MAX_BARS);

    primaryIncrement = viewWidth / (primaryBarCount - 1);

    postInvalidate();
  }

  public void setPrimaryVerticalLine(Float xValue, String text, @ColorInt int color) {
    // TODO
  }

  public void setPrimaryHorizontalLine(Float yValue, String text, @ColorInt int color) {
    this.primaryHorizontalLineValue = yValue;
    this.primaryHorizontalLineLabel = text;
    this.primaryHorizontalLineColor = color;
  }

  public void setSecondaryData(List<List<Float>> data, List<Float> xRange, List<Float> yRange) {
    this.secondaryValues = data;
    this.secondaryXRange = xRange;
    this.secondaryYRange = yRange;

    if (xRange == null || yRange == null) {
      float secondaryMinX = Float.MAX_VALUE;
      float secondaryMaxX = Float.MIN_VALUE;
      float secondaryMinY = Float.MAX_VALUE;
      float secondaryMaxY = Float.MIN_VALUE;

      for (List<Float> value : secondaryValues) {
        secondaryMinX = Math.min(value.get(0), secondaryMinX);
        secondaryMaxX = Math.max(value.get(0), secondaryMaxX);
        if (value.get(1) != null) {
          secondaryMinY = Math.min(value.get(1), secondaryMinY);
          secondaryMaxY = Math.max(value.get(1), secondaryMaxY);
        }
      }

      if (xRange == null) {
        secondaryXRange = new ArrayList<Float>();
        secondaryXRange.add(secondaryMinX);
        secondaryXRange.add(secondaryMaxX);
      }
      if (yRange == null) {
        secondaryYRange = new ArrayList<>();
        secondaryYRange.add(secondaryMinY);
        secondaryYRange.add(secondaryMaxY);
      }
    }

    secondaryBarCount = Math.min(Math.max(MIN_BARS, secondaryValues.size()), MAX_BARS);

    secondaryIncrement = viewWidth / (secondaryBarCount - 1);

    postInvalidate();
  }

  public void setBarsEnabled(boolean barsEnabled) {
    this.barsEnabled = barsEnabled;

    postInvalidate();
  }

  public void setHorizontalLinesEnabled(boolean horizontalLinesEnabled) {
    this.horizontalLinesEnabled = horizontalLinesEnabled;
    postInvalidate();
  }

  public static Path RoundedRect(float left, float top, float right, float bottom, float rx, float ry, boolean tl, boolean tr, boolean br,
      boolean bl) {
    Path path = new Path();
    if (rx < 0) rx = 0;
    if (ry < 0) ry = 0;
    float width = right - left;
    float height = bottom - top;
    if (rx > width / 2) rx = width / 2;
    if (ry > height / 2) ry = height / 2;
    float widthMinusCorners = (width - (2 * rx));
    float heightMinusCorners = (height - (2 * ry));

    path.moveTo(right, top + ry);
    if (tr) {
      path.rQuadTo(0, -ry, -rx, -ry);//top-right corner
    } else {
      path.rLineTo(0, -ry);
      path.rLineTo(-rx, 0);
    }
    path.rLineTo(-widthMinusCorners, 0);
    if (tl) {
      path.rQuadTo(-rx, 0, -rx, ry); //top-left corner
    } else {
      path.rLineTo(-rx, 0);
      path.rLineTo(0, ry);
    }
    path.rLineTo(0, heightMinusCorners);

    if (bl) {
      path.rQuadTo(0, ry, rx, ry);//bottom-left corner
    } else {
      path.rLineTo(0, ry);
      path.rLineTo(rx, 0);
    }

    path.rLineTo(widthMinusCorners, 0);
    if (br) {
      path.rQuadTo(rx, 0, rx, -ry); //bottom-right corner
    } else {
      path.rLineTo(rx, 0);
      path.rLineTo(0, -ry);
    }

    path.rLineTo(0, -heightMinusCorners);

    path.close();//Given close, last lineto can be removed.

    return path;
  }

  public enum ChartPart {
    LINES, CIRCLES
  }

  public void setBarColor(@ColorInt int color) {
    this.barColor = color;
    greyPaint.setColor(barColor);
  }

  public void setChartPrimaryBasicColor(@ColorInt int color) {
    this.chartPrimaryBasicColor = color;
    primaryLinePaint.setColor(chartPrimaryBasicColor);
  }

  public void setChartSecondaryBasicColor(@ColorInt int color) {
    this.chartSecondaryBasicColor = color;
    secondaryLinePaint.setColor(chartSecondaryBasicColor);
  }

  public void setChartCircleFillColor(@ColorInt int chartCircleFillColor) {
    this.chartCircleFillColor = chartCircleFillColor;
    circleWhitePaint.setColor(chartCircleFillColor);
  }

  public void setDarkGreyColor(@ColorInt int darkGreyColor) {
    this.darkGreyColor = darkGreyColor;
    darkGreyPaint.setColor(darkGreyColor);
  }

  public LineChartView(Context context) {
    super(context);
    init(context);
  }

  public LineChartView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }
  //endregion

  private void init(Context context) {
    Resources resources = getResources();

    chartPrimaryBasicColor = defaultBlueColor;
    initDimens(resources);

    initPaints();

    if (isInEditMode()) {
      setPrimaryData(getEditModeExampleDataChart1(), null, null);
      setSecondaryData(getEditModeExampleDataChart2(), null, null);
      setPrimaryHorizontalLine(2f, "Label", defaultGreenColor);
    }
  }

  private void initDimens(Resources resources) {
    chartLineWidth = resources.getDimension(R.dimen.chart_line_width);
    circleDiameter = resources.getDimension(R.dimen.chart_circle_diameter);
    chartHorizontalOffset = resources.getDimension(R.dimen.chart_horizontal_offset);
    chartVerticalOffset = resources.getDimension(R.dimen.chart_vertical_offset);
  }

  private void initPaints() {
    primaryLinePaint.setAntiAlias(true);
    primaryLinePaint.setStyle(Paint.Style.STROKE);
    primaryLinePaint.setStrokeWidth(chartLineWidth);
    primaryLinePaint.setColor(chartPrimaryBasicColor);

    secondaryLinePaint = new Paint(primaryLinePaint);
    secondaryLinePaint.setColor(chartSecondaryBasicColor);

    circleWhitePaint.setAntiAlias(true);
    circleWhitePaint.setColor(chartCircleFillColor);

    greyPaint.setAntiAlias(true);
    greyPaint.setColor(barColor);

    darkGreyPaint.setAntiAlias(true);
    darkGreyPaint.setColor(darkGreyColor);

    primaryHorizontalLineColor = defaultGreenColor;
    primaryHorizontalLinePaint.setAntiAlias(true);
    primaryHorizontalLinePaint.setColor(primaryHorizontalLineColor);
    primaryHorizontalLinePaint.setTextAlign(Paint.Align.RIGHT);
    primaryHorizontalLinePaint.setTextSize(
        com.example.vito.linechartplayground.UiUtils.sp2px(getResources(), 15));
    horizontalLineWidth = chartLineWidth * 1.4f;
    primaryHorizontalLinePaint.setStrokeWidth(horizontalLineWidth);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    barLeft = chartHorizontalBarOffset;

    primaryLineStartX = 0;
    primaryLineStartY = 0;

    secondaryLineStartX = 0;
    secondaryLineStartY = 0;

    if (barsEnabled) {
      drawChartBars(canvas);
    }

    if (horizontalLinesEnabled) {
      drawHorizontalLines(canvas);
    } else {
      drawBottomLine(canvas);
    }

    Float maxX = rangeX().get(1);
    Float minX = rangeX().get(0);
    float ratioX = viewWidth / (maxX - minX);

    Float minY = rangeY().get(0);
    Float maxY = rangeY().get(1);
    float ratioY = viewHeight / (maxY - minY);

    if (BuildConfig.DEBUG) {
      System.out.println("minX:" + minX);
      System.out.println("maxX:" + maxX);
      System.out.println("ratioX:" + ratioX);
      System.out.println("minY:" + minY);
      System.out.println("maxY:" + maxY);
      System.out.println("ratioY:" + ratioY);
    }

    drawPrimaryChartPart(canvas, ratioX, ratioY, ChartPart.LINES);
    drawPrimaryChartPart(canvas, ratioX, ratioY, ChartPart.CIRCLES);

    if (primaryHorizontalLineValue != null) {
      drawPrimaryChartHorizontalLine(canvas, ratioX, ratioY);
    }

    if (secondaryValues != null && !secondaryValues.isEmpty()) {

      drawSecondaryChartPart(canvas, ratioX, ratioY, ChartPart.LINES);
      drawSecondaryChartPart(canvas, ratioX, ratioY, ChartPart.CIRCLES);
    }

  }

  /**
   * Had to use onLayout since when inEditMode, onMeasure wasn't returning any height/width
   */
  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
    viewWidth = getWidth();
    viewHeight = getHeight();
    primaryIncrement = viewWidth / (primaryBarCount - 1);
    secondaryIncrement = viewWidth / (secondaryBarCount - 1);

    chartHorizontalBarOffset = (viewWidth - primaryIncrement * (primaryBarCount - 1)) / 2;
  }

  private void drawBottomLine(Canvas canvas) {
    canvas.drawLine(0, viewHeight, viewWidth, viewHeight, darkGreyPaint);
  }

  private void drawHorizontalLines(Canvas canvas) {
    int iterations = 4;
    int increment = (int) ((viewHeight - chartVerticalOffset) / iterations);
    int initHeight = (int) (viewHeight - chartVerticalOffset);
      System.out.println("initHeight:"+initHeight);
    canvas.drawLine(0, initHeight, viewWidth, initHeight, darkGreyPaint);
    for (int i = 0; i < iterations; i++) {
      initHeight -= increment;
      if (initHeight < chartVerticalOffset) {
        initHeight = (int) chartVerticalOffset;
      }
      canvas.drawLine(0, initHeight, viewWidth, initHeight, darkGreyPaint);
      System.out.println("initHeight:"+initHeight);

    }
  }

  private void drawChartBars(Canvas canvas) {
    float barRight = chartHorizontalBarOffset;
    for (int i = 0; i < primaryBarCount - 1; i++) {
      barRight += primaryIncrement;
      if (i % 2 == 0) {
        canvas.drawRect(barLeft, 0, barRight, getHeight(), greyPaint);
      }
      barLeft = barRight;
    }
  }

  private float applyOffset(float point, float offset, int total) {
    System.out.println("pIN:"+point);
    int midHeight = total / 2;
    boolean belowMid = point > midHeight;
    boolean aboveMid = point < midHeight;
    boolean minusOffsetStillBelowMid = (point - offset) > midHeight;
    boolean minusOffsetStillAboveMid = (point + offset) < midHeight;

    if (belowMid && minusOffsetStillBelowMid) {
      if (point >= total - offset) {
        point = total;
      }
      point -= offset;
    } else {
      if (aboveMid && minusOffsetStillAboveMid || point <= offset) {
        point += offset;
      }
    }
    System.out.println("pOUT:"+point);
    return point;
  }

  private void drawPrimaryChartPart(Canvas canvas, float ratioX, float ratioY, ChartPart chartPart) {
    //setBackgroundColor(Color.parseColor("#cccccc"));
    if (primaryValues.size() == 1) {
      canvas.drawCircle(viewWidth / 3, viewHeight / 3, circleDiameter, circleWhitePaint);
      canvas.drawCircle(viewWidth / 3, viewHeight / 3, circleDiameter, primaryLinePaint);
    }

    for (int i = 0; i < primaryValues.size(); i++) {
      List<Float> value = primaryValues.get(i);
      float valX = value.get(0);
      Float valY = value.get(1);

      if (valY == null) {
        continue;
      }

      float x = (valX - rangeX().get(0)) * ratioX;
      float y = (rangeY().get(1) - valY) * ratioY;

      x = applyOffset(x, chartHorizontalOffset, viewWidth);
      y = applyOffset(y, chartVerticalOffset, viewHeight);

      if (chartPart == ChartPart.LINES) {

        if (primaryLineStartX == 0 && primaryLineStartY == 0) {
          primaryLineStartX = x;
          primaryLineStartY = y;
        } else {
          canvas.drawLine(primaryLineStartX, primaryLineStartY, x, y, primaryLinePaint);
        }
      } else if (chartPart == ChartPart.CIRCLES) {
        canvas.drawCircle(x, y, circleDiameter, circleWhitePaint);
        canvas.drawCircle(x, y, circleDiameter, primaryLinePaint);
      }

      primaryLineStartX = x;
      primaryLineStartY = y;
    }
  }

  private void drawSecondaryChartPart(Canvas canvas, float ratioX, float ratioY, ChartPart chartPart) {
    if (secondaryValues.size() == 1) {
      canvas.drawCircle(viewWidth / 3, viewHeight / 3, circleDiameter, circleWhitePaint);
      canvas.drawCircle(viewWidth / 3, viewHeight / 3, circleDiameter, secondaryLinePaint);
    }

    for (int i = 0; i < secondaryValues.size(); i++) {
      List<Float> value = secondaryValues.get(i);
      float valX = value.get(0);
      Float valY = value.get(1);

      if (valY == null) {
        continue;
      }

      float x = (valX - rangeX().get(0)) * ratioX;
      float y = (rangeY().get(1) - valY) * ratioY;

      x = applyOffset(x, chartHorizontalOffset, viewWidth);
      y = applyOffset(y, chartVerticalOffset, viewHeight);

      if (chartPart == ChartPart.LINES) {

        if (secondaryLineStartX == 0 && secondaryLineStartY == 0) {
          secondaryLineStartX = x;
          secondaryLineStartY = y;
        } else {
          canvas.drawLine(secondaryLineStartX, secondaryLineStartY, x, y, secondaryLinePaint);
        }
      } else if (chartPart == ChartPart.CIRCLES) {
        canvas.drawCircle(x, y, circleDiameter, circleWhitePaint);
        canvas.drawCircle(x, y, circleDiameter, secondaryLinePaint);
      }

      secondaryLineStartX = x;
      secondaryLineStartY = y;
    }
  }

  private void drawPrimaryChartHorizontalLine(Canvas canvas, float ratioX, float ratioY) {
    Resources resources = getResources();

    Rect labelBounds = new Rect();
    primaryHorizontalLinePaint.getTextBounds(primaryHorizontalLineLabel, 0, primaryHorizontalLineLabel.length(), labelBounds);
    int fontHeight = labelBounds.height();
    final int fontWidth = labelBounds.width();

    // Draw goal line
    float lineY = ((rangeY().get(1) - primaryHorizontalLineValue) * ratioY) - horizontalLineWidth / 2;
    // take into account vertical padding
    if (lineY > viewHeight / 2 && (lineY - chartVerticalOffset) > viewHeight / 2) {
      lineY -= chartVerticalOffset;
    } else if (lineY < viewHeight / 2 && (lineY + chartVerticalOffset) < viewHeight / 2) {
      lineY += chartVerticalOffset;
    }
    // goal green label shouldn't be cut
    if (lineY < 1) {
      lineY = 1;
    } else if (lineY > viewHeight) {
      lineY = viewHeight - com.example.vito.linechartplayground.UiUtils.dpToPx(resources, 2);
    }

    int lineEnd = this.viewWidth - fontWidth;
    int widthSplitted = lineEnd / 25;
    for (int posX = 0; posX < lineEnd; posX += widthSplitted) {
      float startY = lineY;
      canvas.drawLine(posX, startY, posX + widthSplitted / 2, startY, primaryHorizontalLinePaint);
    }

    // Draw square
    float boxHeightPadding = com.example.vito.linechartplayground.UiUtils.dpToPx(resources, 10);
    float boxWidthPadding = com.example.vito.linechartplayground.UiUtils.dpToPx(resources, 20);
    float cornerRadius = com.example.vito.linechartplayground.UiUtils.dpToPx(resources, 5);

    Paint paint = new Paint();
    paint.setColor(primaryHorizontalLineColor);

    float left = this.viewWidth - (fontWidth + boxWidthPadding);
    float right = this.viewWidth - cornerRadius;

    float bottom = lineY + fontHeight / 2 + boxHeightPadding / 2;
    float top = (bottom - fontHeight - boxHeightPadding);

    float aboveLowLimitOffset = 0;
    float boxHeight = bottom - top;
    if (top + boxHeight > viewHeight) {
      aboveLowLimitOffset = -(boxHeight / 2);
    } else if (top <= 0) {
      aboveLowLimitOffset = boxHeight / 2;
    }
    bottom += aboveLowLimitOffset;
    top += aboveLowLimitOffset;

    Path path = RoundedRect(left, bottom, right, top, cornerRadius, cornerRadius, true, true, true, true);
    canvas.drawPath(path, paint);

    // Draw Text
    primaryHorizontalLinePaint.setColor(Color.WHITE);
    float y = lineY + fontHeight / 2;
    y += aboveLowLimitOffset;
    float x = left + fontWidth + boxWidthPadding / 2.3f;
    canvas.drawText(primaryHorizontalLineLabel, x, y, primaryHorizontalLinePaint);
  }

  private List<List<Float>> getEditModeExampleDataChart1() {
    List<List<Float>> values = new ArrayList<>();
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 1f, 10f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 2f, 2f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 3f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 4f, 6f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 5f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 6f, 3f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 7f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 8f, 2f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 9f, 1f })));

    return values;
  }

  private List<List<Float>> getEditModeExampleDataChart2() {
    List<List<Float>> values = new ArrayList<>();
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 1f, 3f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 2f, 10f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 3f, 4f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 4f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 5f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 6f, 3f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 7f, 5f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 8f, 7f })));
    values.add(new ArrayList<>(Arrays.asList(new Float[] { 9f, 6f })));

    return values;
  }

}
