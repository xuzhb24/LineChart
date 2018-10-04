package com.elestic;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class LineChart extends View {

    private static final int NONE_STYLE = 0;
    private static final int CIRCLE_STYLE = 1;
    private static final int ARC_STYLE = 2;
    private static final float DEFAULT_MAXYVALUE = 99999999f;

    private Paint axisPaint;//绘制轴线和轴线上的小刻度线
    private Paint gridPaint;//绘制网格线
    private Paint linePaint;//绘制折线
    private Paint textPaint;//绘制文本
    private Paint pointPaint;//绘制折线交点和轴线交点
    private int width;//宽度
    private int height;//高度
    private float xPoint;//原点的X坐标
    private float yPoint;//原点的Y坐标
    private float xScale;//X轴刻度长度
    private float yScale;//Y轴刻度长度
    private float xLength;//X轴长度
    private float yLength;//Y轴长度
    private int xLabelCount = 7;//X轴刻度个数
    private int yLabelCount = 4;//Y轴刻度个数
    private float maxYLabelTextWidth = 0;
    private float[] yValue;//数据

    //可设置的属性
    //设置X轴标签个数，只有在设置了该属性的值且值大于等于XLabelCount时该属性才有效
    private int defaultXLabelCount;
    //设置Y轴标签个数，只有在设置了该属性的值且值大于等于YLabelCount时该属性才有效
    private int defaultYLabelCount;
    private CharSequence[] xLabel;//X轴刻度值
    private CharSequence[] yLabel;//Y轴刻度值
    private float xLabelTextSize;//X轴刻度值字体大小
    private int xLabelTextColor;//X轴刻度值字体颜色
    private float xLabelTextPadding;//X轴刻度值和X轴的距离
    private float yLabelTextSize;//Y轴刻度值字体大小
    private int yLabelTextColor;//Y轴刻度值字体颜色
    private float yLabelTextPadding;//Y轴刻度值和Y轴的距离
    //maxYValue是Y轴上的最大刻度值，它和yLabel[yLabel.length-1]的关系如下
    //如果设置了maxYValue属性，Y轴上的最大刻度值为maxYValue的值
    //如果未设置maxYValue属性
    //此时如果yLabel[yLabel.length-1]可以转换成数值，则Y轴上的最大刻度值为yLabel[yLabel.length-1]表示的数值
    //否则Y轴上的最大刻度值取maxYValue的默认值，这里设置为DEFAULT_MAXYVALUE
    private float maxYValue;
    private boolean showYValueText;//是否显示数据文本，默认不显示
    private float yValueTextSize;//数据文本字体大小
    private int yValueTextColor;//数据文本字体颜色
    private float yValueTextPadding;//数据文本和折线交点的距离
    private int yValueTextLocation;//数据文本显示的位置：折线交点上方、折线交点下方
    private float axisWidth;//轴线宽度
    private int axisColor;//轴线颜色
    private int axisPointStyle;//轴线交点的样式：圆环、实心圆、无样式
    private float axisPointRadius;//轴线交点半径，仅当axisPointStyle为circle或arc时有效
    private float axisPointArcWidth;//轴线交点圆弧宽度，仅当axisPointStyle为arc时有效
    private float axisExtendLength;//轴线向上和向右延伸的长度
    private float lineWidth;//折线宽度
    private int lineColor;//折线颜色
    private int linePointStyle;//折线交点的样式：圆环、实心圆、无样式
    private float linePointRadius;//折线交点半径，仅当linePointStyle为circle或arc时有效
    private float linePointArcWidth;//折线交点圆弧宽度，仅当linePointStyle为arc时有效
    private boolean showScale;//是否显示轴线上的小刻度线，默认显示
    private float scaleLength;//轴线上的小刻度线长度
    private boolean showGrid;//是否显示网格，默认显示
    private float gridWidth;//网格线宽度
    private int gridColor;//网格线颜色
    private float gridExtendLength;//网格线向上和向右延伸的长度
    private String description;//图表描述信息
    private boolean showDescription;//是否显示图表描述信息，默认显示
    private float descriptionTextSize;//图表描述信息文本字体大小
    private int descriptionTextColor;//图表描述信息文本字体颜色
    private float descriptionTextPadding;//图表描述信息文本和图表最上方的距离
    private int descriptionTextLocation;//图表描述信息文本的位置：图表左边、中间或右边
    private float leftPadding;//左边缘边距
    private float topPadding;//上边缘边距
    private float rightPadding;//右边缘边距
    private float bottomPadding;//下边缘边距
    private boolean showDefault;//是否显示无数据时的默认视图
    private String defaultText;//无数据时的默认提示
    private float defaultTextSize;//无数据时的提示文本字体大小
    private int defaultTextColor;//无数据时的提示文本字体颜色

    public int getDefaultXLabelCount() {
        return defaultXLabelCount;
    }

    public void setDefaultXLabelCount(int defaultXLabelCount) {
        this.defaultXLabelCount = defaultXLabelCount;
    }

    public int getDefaultYLabelCount() {
        return defaultYLabelCount;
    }

    public void setDefaultYLabelCount(int defaultYLabelCount) {
        this.defaultYLabelCount = defaultYLabelCount;
    }

    public CharSequence[] getXLabel() {
        return xLabel;
    }

    public void setXLabel(CharSequence[] xLabel) {
        this.xLabel = xLabel;
    }

    public CharSequence[] getYLabel() {
        return yLabel;
    }

    public void setYLabel(CharSequence[] yLabel) {
        this.yLabel = yLabel;
    }

    public float getXLabelTextSize() {
        return xLabelTextSize;
    }

    public void setXLabelTextSize(float xLabelTextSize) {
        this.xLabelTextSize = xLabelTextSize;
    }

    public int getXLabelTextColor() {
        return xLabelTextColor;
    }

    public void setXLabelTextColor(int xLabelTextColor) {
        this.xLabelTextColor = xLabelTextColor;
    }

    public float getXLabelTextPadding() {
        return xLabelTextPadding;
    }

    public void setXLabelTextPadding(float xLabelTextPadding) {
        this.xLabelTextPadding = xLabelTextPadding;
    }

    public float getYLabelTextSize() {
        return yLabelTextSize;
    }

    public void setYLabelTextSize(float yLabelTextSize) {
        this.yLabelTextSize = yLabelTextSize;
    }

    public int getYLabelTextColor() {
        return yLabelTextColor;
    }

    public void setYLabelTextColor(int yLabelTextColor) {
        this.yLabelTextColor = yLabelTextColor;
    }

    public float getYLabelTextPadding() {
        return yLabelTextPadding;
    }

    public void setYLabelTextPadding(float yLabelTextPadding) {
        this.yLabelTextPadding = yLabelTextPadding;
    }

    public float getMaxYValue() {
        return maxYValue;
    }

    public void setMaxYValue(float maxYValue) {
        this.maxYValue = maxYValue;
    }

    public boolean isShowYValueText() {
        return showYValueText;
    }

    public void setShowYValueText(boolean showYValueText) {
        this.showYValueText = showYValueText;
    }

    public float getYValueTextSize() {
        return yValueTextSize;
    }

    public void setYValueTextSize(float yValueTextSize) {
        this.yValueTextSize = yValueTextSize;
    }

    public int getYValueTextColor() {
        return yValueTextColor;
    }

    public void setYValueTextColor(int yValueTextColor) {
        this.yValueTextColor = yValueTextColor;
    }

    public float getYValueTextPadding() {
        return yValueTextPadding;
    }

    public void setYValueTextPadding(float yValueTextPadding) {
        this.yValueTextPadding = yValueTextPadding;
    }

    public int getYValueTextLocation() {
        return yValueTextLocation;
    }

    public void setYValueTextLocation(int yValueTextLocation) {
        this.yValueTextLocation = yValueTextLocation;
    }

    public float getAxisWidth() {
        return axisWidth;
    }

    public void setAxisWidth(float axisWidth) {
        this.axisWidth = axisWidth;
    }

    public int getAxisColor() {
        return axisColor;
    }

    public void setAxisColor(int axisColor) {
        this.axisColor = axisColor;
    }

    public int getAxisPointStyle() {
        return axisPointStyle;
    }

    public void setAxisPointStyle(int axisPointStyle) {
        this.axisPointStyle = axisPointStyle;
    }

    public float getAxisPointRadius() {
        return axisPointRadius;
    }

    public void setAxisPointRadius(float axisPointRadius) {
        this.axisPointRadius = axisPointRadius;
    }

    public float getAxisPointArcWidth() {
        return axisPointArcWidth;
    }

    public void setAxisPointArcWidth(float axisPointArcWidth) {
        this.axisPointArcWidth = axisPointArcWidth;
    }

    public float getAxisExtendLength() {
        return axisExtendLength;
    }

    public void setAxisExtendLength(float axisExtendLength) {
        this.axisExtendLength = axisExtendLength;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getLinePointStyle() {
        return linePointStyle;
    }

    public void setLinePointStyle(int linePointStyle) {
        this.linePointStyle = linePointStyle;
    }

    public float getLinePointRadius() {
        return linePointRadius;
    }

    public void setLinePointRadius(float linePointRadius) {
        this.linePointRadius = linePointRadius;
    }

    public float getLinePointArcWidth() {
        return linePointArcWidth;
    }

    public void setLinePointArcWidth(float linePointArcWidth) {
        this.linePointArcWidth = linePointArcWidth;
    }

    public boolean isShowScale() {
        return showScale;
    }

    public void setShowScale(boolean showScale) {
        this.showScale = showScale;
    }

    public float getScaleLength() {
        return scaleLength;
    }

    public void setScaleLength(float scaleLength) {
        this.scaleLength = scaleLength;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public float getGridWidth() {
        return gridWidth;
    }

    public void setGridWidth(float gridWidth) {
        this.gridWidth = gridWidth;
    }

    public int getGridColor() {
        return gridColor;
    }

    public void setGridColor(int gridColor) {
        this.gridColor = gridColor;
    }

    public float getGridExtendLength() {
        return gridExtendLength;
    }

    public void setGridExtendLength(float gridExtendLength) {
        this.gridExtendLength = gridExtendLength;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isShowDescription() {
        return showDescription;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public float getDescriptionTextSize() {
        return descriptionTextSize;
    }

    public void setDescriptionTextSize(float descriptionTextSize) {
        this.descriptionTextSize = descriptionTextSize;
    }

    public int getDescriptionTextColor() {
        return descriptionTextColor;
    }

    public void setDescriptionTextColor(int descriptionTextColor) {
        this.descriptionTextColor = descriptionTextColor;
    }

    public float getDescriptionTextPadding() {
        return descriptionTextPadding;
    }

    public void setDescriptionTextPadding(float descriptionTextPadding) {
        this.descriptionTextPadding = descriptionTextPadding;
    }

    public int getDescriptionTextLocation() {
        return descriptionTextLocation;
    }

    public void setDescriptionTextLocation(int descriptionTextLocation) {
        this.descriptionTextLocation = descriptionTextLocation;
    }

    public float getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(float leftPadding) {
        this.leftPadding = leftPadding;
    }

    public float getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(float topPadding) {
        this.topPadding = topPadding;
    }

    public float getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(float rightPadding) {
        this.rightPadding = rightPadding;
    }

    public float getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(float bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public boolean isShowDefault() {
        return showDefault;
    }

    public void setShowDefault(boolean showDefault) {
        this.showDefault = showDefault;
        setLinePointStyle(NONE_STYLE);
    }

    public String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public float getDefaultTextSize() {
        return defaultTextSize;
    }

    public void setDefaultTextSize(float defaultTextSize) {
        this.defaultTextSize = defaultTextSize;
    }

    public int getDefaultTextColor() {
        return defaultTextColor;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void drawData(float[] yValue) {
        this.yValue = yValue;
        invalidate();
    }

    public void drawData(float[] yValue, String[] xLabel) {
        this.yValue = yValue;
        this.xLabel = xLabel;
        invalidate();
    }

    public void drawData(float[] yValue, String[] xLabel, String[] yLabel) {
        this.yValue = yValue;
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        invalidate();
    }

    public LineChart(Context context) {
        super(context);
        initPaint();
    }

    public LineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        parseAttribute(context, attrs);
        initPaint();
    }

    private void parseAttribute(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
        defaultXLabelCount = ta.getInteger(R.styleable.LineChart_defaultXLabelCount, 0);
        defaultYLabelCount = ta.getInteger(R.styleable.LineChart_defaultYLabelCount, 0);
        xLabel = ta.getTextArray(R.styleable.LineChart_xLabel);
        yLabel = ta.getTextArray(R.styleable.LineChart_yLabel);
        xLabelTextSize = ta.getDimension(R.styleable.LineChart_xLabelTextSize, SizeUtil.sp2px(getResources(), 20));
        xLabelTextColor = ta.getColor(R.styleable.LineChart_xLabelTextColor, Color.parseColor("#dedede"));
        xLabelTextPadding = ta.getDimension(R.styleable.LineChart_xLabelTextPadding, SizeUtil.dp2px(getResources(), 8));
        yLabelTextSize = ta.getDimension(R.styleable.LineChart_yLabelTextSize, SizeUtil.sp2px(getResources(), 20));
        yLabelTextColor = ta.getColor(R.styleable.LineChart_yLabelTextColor, Color.parseColor("#dedede"));
        yLabelTextPadding = ta.getDimension(R.styleable.LineChart_yLabelTextPadding, SizeUtil.dp2px(getResources(), 8));
        maxYValue = ta.getFloat(R.styleable.LineChart_maxYValue, DEFAULT_MAXYVALUE);
        showYValueText = ta.getBoolean(R.styleable.LineChart_showYValueText, false);
        yValueTextSize = ta.getDimension(R.styleable.LineChart_yValueTextSize, SizeUtil.sp2px(getResources(), 20));
        yValueTextColor = ta.getColor(R.styleable.LineChart_yValueTextColor, Color.parseColor("#26c510"));
        yValueTextPadding = ta.getDimension(R.styleable.LineChart_yValueTextPadding, SizeUtil.dp2px(getResources(), 8));
        yValueTextLocation = ta.getInt(R.styleable.LineChart_yValueTextLocation, 0);
        axisWidth = ta.getDimension(R.styleable.LineChart_axisWidth, SizeUtil.dp2px(getResources(), 1.5f));
        axisColor = ta.getColor(R.styleable.LineChart_axisColor, Color.parseColor("#dedede"));
        axisPointStyle = ta.getInt(R.styleable.LineChart_axisPointStyle, 0);
        axisPointRadius = ta.getDimension(R.styleable.LineChart_axisPointRadius, axisWidth * 2.0f);
        axisPointArcWidth = ta.getDimension(R.styleable.LineChart_axisPointArcWidth, axisWidth);
        axisExtendLength = ta.getDimension(R.styleable.LineChart_axisExtendLength, 0);
        lineWidth = ta.getDimension(R.styleable.LineChart_lineWidth, SizeUtil.dp2px(getResources(), 1.5f));
        lineColor = ta.getColor(R.styleable.LineChart_lineColor, Color.parseColor("#34b1eb"));
        linePointStyle = ta.getInt(R.styleable.LineChart_linePointStyle, 2);
        linePointRadius = ta.getDimension(R.styleable.LineChart_linePointRadius, lineWidth * 2.0f);
        linePointArcWidth = ta.getDimension(R.styleable.LineChart_linePointArcWidth, lineWidth);
        showScale = ta.getBoolean(R.styleable.LineChart_showScale, true);
        scaleLength = ta.getDimension(R.styleable.LineChart_scaleLength, SizeUtil.dp2px(getResources(), 5));
        showGrid = ta.getBoolean(R.styleable.LineChart_showGrid, true);
        gridWidth = ta.getDimension(R.styleable.LineChart_gridWidth, SizeUtil.dp2px(getResources(), 0.5f));
        gridColor = ta.getColor(R.styleable.LineChart_gridColor, Color.parseColor("#dedede"));
        gridExtendLength = ta.getDimension(R.styleable.LineChart_gridExtendLength, 0);
        description = ta.getString(R.styleable.LineChart_description);
        showDescription = ta.getBoolean(R.styleable.LineChart_showDescription, false);
        descriptionTextSize = ta.getDimension(R.styleable.LineChart_descriptionTextSize, SizeUtil.sp2px(getResources(), 25));
        descriptionTextColor = ta.getColor(R.styleable.LineChart_descriptionTextColor, Color.parseColor("#dedede"));
        descriptionTextPadding = ta.getDimension(R.styleable.LineChart_descriptionTextPadding, SizeUtil.dp2px(getResources(), 8));
        descriptionTextLocation = ta.getInt(R.styleable.LineChart_descriptionTextLocation, 0);
        leftPadding = ta.getDimension(R.styleable.LineChart_leftPadding, SizeUtil.dp2px(getResources(), 60));
        topPadding = ta.getDimension(R.styleable.LineChart_topPadding, SizeUtil.dp2px(getResources(), 60));
        rightPadding = ta.getDimension(R.styleable.LineChart_rightPadding, SizeUtil.dp2px(getResources(), 60));
        bottomPadding = ta.getDimension(R.styleable.LineChart_bottomPadding, SizeUtil.dp2px(getResources(), 60));
        showDefault = ta.getBoolean(R.styleable.LineChart_showDefault, false);
        defaultText = TextUtils.isEmpty(ta.getString(R.styleable.LineChart_defaultText)) ? "暂无数据" : ta.getString(R.styleable.LineChart_defaultText);
        defaultTextSize = ta.getDimension(R.styleable.LineChart_defaultTextSize, SizeUtil.sp2px(getResources(), 30));
        defaultTextColor = ta.getColor(R.styleable.LineChart_defaultTextColor, Color.RED);
        ta.recycle();
    }

    private void initPaint() {
        axisPaint = new Paint();
        axisPaint.setAntiAlias(true);
        axisPaint.setStrokeWidth(axisWidth);
        axisPaint.setColor(axisColor);
        gridPaint = new Paint();
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(gridWidth);
        gridPaint.setColor(gridColor);
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        float extendLength = axisExtendLength >= gridExtendLength ? axisExtendLength : gridExtendLength;
        xLength = width - leftPadding - rightPadding - extendLength;
        yLength = height - topPadding - bottomPadding - extendLength;
        xPoint = leftPadding;
        yPoint = height - bottomPadding;
        if (xLabel != null && xLabel.length > 0) {
            xLabelCount = xLabel.length;
        }
        if (yLabel != null && yLabel.length > 0) {
            yLabelCount = yLabel.length;
        }
        if (defaultXLabelCount >= xLabelCount) {
            xLabelCount = defaultXLabelCount;
        }
        if (defaultYLabelCount >= yLabelCount) {
            yLabelCount = defaultYLabelCount;
        }
        xScale = xLength / (float) xLabelCount;
        yScale = yLength / (float) yLabelCount;
        //绘制原点、X轴和Y轴
        if (axisPointStyle == ARC_STYLE) {//轴线交点为圆环样式
            //绘制原点
            pointPaint.setColor(axisColor);
            pointPaint.setStrokeWidth(axisPointArcWidth);
            pointPaint.setStyle(Paint.Style.STROKE);
            RectF rectF = new RectF(xPoint - axisPointRadius, yPoint - axisPointRadius,
                    xPoint + axisPointRadius, yPoint + axisPointRadius);
            canvas.drawArc(rectF, 0, 360, false, pointPaint);//原点
            //绘制X轴
            if (linePointStyle == ARC_STYLE) {
                if (yValue != null && yValue.length > 0) {
                    canvas.drawLine(xPoint + axisPointRadius, yPoint, xPoint + xScale / 2.0f, yPoint, axisPaint);
                    for (int i = 0; i < yValue.length - 1; i++) {
                        if (yPoint - calculateYPosition(yValue[i]) < linePointRadius) {
                            float radian = (float) (Math.asin((float) (yPoint - calculateYPosition(yValue[i])) / (float) linePointRadius));
                            canvas.drawLine(xPoint + xScale / 2.0f + i * xScale, yPoint,
                                    xPoint + (i + 1) * xScale - (float) (linePointRadius * Math.cos(radian)), yPoint, axisPaint);
                            canvas.drawLine(xPoint + (i + 1) * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint,
                                    xPoint + xScale / 2.0f + (i + 1) * xScale, yPoint, axisPaint);
                        } else {
                            canvas.drawLine(xPoint + xScale / 2.0f + i * xScale, yPoint,
                                    xPoint + xScale / 2.0f + (i + 1) * xScale, yPoint, axisPaint);
                        }
                    }
                    if (calculateYPosition(yValue[yValue.length - 1]) + linePointRadius >= yPoint) {
                        float radian = (float) (Math.asin((float) (yPoint - calculateYPosition(yValue[yValue.length - 1])) / (float) linePointRadius));
                        canvas.drawLine(xPoint + xScale / 2.0f + (yValue.length - 1) * xScale, yPoint,
                                xPoint + yValue.length * xScale - (float) (linePointRadius * Math.cos(radian)), yPoint, axisPaint);
                        if (yValue.length < xLabelCount) {
                            canvas.drawLine(xPoint + yValue.length * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint,
                                    width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                        } else if (yValue.length == xLabelCount) {
                            if (axisExtendLength > linePointRadius * Math.cos(radian)) {
                                canvas.drawLine(xPoint + yValue.length * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint,
                                        width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                            }
                        }
                    } else {
                        canvas.drawLine(xPoint + xScale / 2.0f + (yValue.length - 1) * xScale, yPoint,
                                width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                    }
                } else {
                    canvas.drawLine(xPoint + axisPointRadius, yPoint,
                            width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                }
            } else {
                canvas.drawLine(xPoint + axisPointRadius, yPoint,
                        width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
            }
            canvas.drawLine(xPoint, yPoint - axisPointRadius, xPoint, topPadding + extendLength - axisExtendLength - axisWidth / 2.0f, axisPaint);//绘制Y轴
        } else {//轴线交点为实心圆样式或无样式
            if (axisPointStyle == CIRCLE_STYLE) {
                pointPaint.setColor(axisColor);
                pointPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(xPoint, yPoint, axisPointRadius, pointPaint);//绘制原点
            }
            //绘制X轴
            if (linePointStyle == ARC_STYLE) {
                if (yValue != null && yValue.length > 0) {
                    canvas.drawLine(xPoint - axisWidth / 2.0f, yPoint, xPoint + xScale / 2.0f, yPoint, axisPaint);
                    for (int i = 0; i < yValue.length - 1; i++) {
                        if (yPoint - calculateYPosition(yValue[i]) < linePointRadius) {
                            float radian = (float) (Math.asin((float) (yPoint - calculateYPosition(yValue[i])) / (float) linePointRadius));
                            canvas.drawLine(xPoint + xScale / 2.0f + i * xScale, yPoint,
                                    xPoint + (i + 1) * xScale - (float) (linePointRadius * Math.cos(radian)), yPoint, axisPaint);
                            canvas.drawLine(xPoint + (i + 1) * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint,
                                    xPoint + xScale / 2.0f + (i + 1) * xScale, yPoint, axisPaint);
                        } else {
                            canvas.drawLine(xPoint + xScale / 2.0f + i * xScale, yPoint,
                                    xPoint + xScale / 2.0f + (i + 1) * xScale, yPoint, axisPaint);
                        }
                    }
                    canvas.drawLine(xPoint + xScale / 2.0f + (yValue.length - 1) * xScale, yPoint,
                            width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                } else {
                    canvas.drawLine(xPoint - axisWidth / 2.0f, yPoint,
                            width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
                }
            } else {
                canvas.drawLine(xPoint - axisWidth / 2.0f, yPoint,
                        width - rightPadding - extendLength + axisExtendLength + axisWidth / 2.0f, yPoint, axisPaint);
            }
            canvas.drawLine(xPoint, yPoint + axisWidth / 2.0f, xPoint, topPadding + extendLength - axisExtendLength - axisWidth / 2.0f, axisPaint);//绘制Y轴
        }
        if (yLabel != null && yLabel.length > 0) {
            for (int i = 0; i < yLabel.length; i++) {
                textPaint.setTextSize(yLabelTextSize);
                float textWidth = textPaint.measureText(yLabel[i].toString());
                if (maxYLabelTextWidth <= textWidth) {
                    maxYLabelTextWidth = textWidth;
                }
            }
        }
        for (int i = 0; i < xLabelCount; i++) {
            //绘制网格线：竖刻线
            if (showGrid) {
                if (linePointStyle == ARC_STYLE) {
                    if (yValue != null && yValue.length > i) {
                        if (calculateYPosition(yValue[i]) + linePointRadius >= yPoint) {
                            canvas.drawLine(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]) - linePointRadius,
                                    xPoint + (i + 1) * xScale, topPadding + extendLength - gridExtendLength - gridWidth / 2.0f, gridPaint);
                        } else {
                            canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale,
                                    calculateYPosition(yValue[i]) + linePointRadius, gridPaint);
                            if (calculateYPosition(yValue[i]) - linePointRadius > yPoint - yLabel.length * yScale) {
                                canvas.drawLine(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]) - linePointRadius,
                                        xPoint + (i + 1) * xScale, topPadding + extendLength - gridExtendLength - gridWidth / 2.0f, gridPaint);
                            } else {
                                if (topPadding + extendLength - gridExtendLength < calculateYPosition(yValue[i]) - linePointRadius) {
                                    canvas.drawLine(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]) - linePointRadius,
                                            xPoint + (i + 1) * xScale, topPadding + extendLength - gridExtendLength - gridWidth / 2.0f, gridPaint);
                                }
                            }
                        }
                    } else {
                        canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale,
                                topPadding + extendLength - gridExtendLength - gridWidth / 2.0f, gridPaint);
                    }
                } else {
                    canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale,
                            topPadding + extendLength - gridExtendLength - gridWidth / 2.0f, gridPaint);
                }
            }
            //绘制X轴上的小刻度线
            if (showScale) {
                if (linePointStyle == ARC_STYLE) {
                    if (yValue != null && yValue.length > i) {
                        if (calculateYPosition(yValue[i]) + linePointRadius > yPoint - scaleLength) {
                            if (calculateYPosition(yValue[i]) - linePointRadius <= yPoint - scaleLength) {
                                if (calculateYPosition(yValue[i]) + linePointRadius < yPoint) {
                                    canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale,
                                            calculateYPosition(yValue[i]) + linePointRadius, axisPaint);
                                }
                            } else {
                                canvas.drawLine(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]) - linePointRadius,
                                        xPoint + (i + 1) * xScale, yPoint - scaleLength, axisPaint);
                                if (calculateYPosition(yValue[i]) + linePointRadius < yPoint) {
                                    canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale,
                                            calculateYPosition(yValue[i]) + linePointRadius, axisPaint);
                                }
                            }
                        } else {
                            canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale, yPoint - scaleLength, axisPaint);
                        }
                    } else {
                        canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale, yPoint - scaleLength, axisPaint);
                    }
                } else {
                    canvas.drawLine(xPoint + (i + 1) * xScale, yPoint, xPoint + (i + 1) * xScale, yPoint - scaleLength, axisPaint);
                }
            }
            //绘制X轴上的刻度值
            if (xLabel != null && xLabel.length > i) {
                textPaint.setTextSize(xLabelTextSize);
                textPaint.setColor(xLabelTextColor);
                Paint.FontMetrics f = textPaint.getFontMetrics();
                canvas.drawText(xLabel[i].toString(), xPoint + (i + 1) * xScale, yPoint - f.ascent + xLabelTextPadding, textPaint);
            }
        }
        for (int i = 0; i < yLabelCount; i++) {
            //绘制网格线：横刻线
            if (showGrid) {
                if (linePointStyle == ARC_STYLE) {
                    if (yValue != null && yValue.length > 0) {
                        canvas.drawLine(xPoint, yPoint - (i + 1) * yScale, xPoint + xScale / 2.0f, yPoint - (i + 1) * yScale, gridPaint);
                        for (int j = 0; j < yValue.length - 1; j++) {
                            if (Math.abs(calculateYPosition(yValue[j]) - (yPoint - (i + 1) * yScale)) < linePointRadius) {
                                float radian = (float) (Math.asin((float) ((calculateYPosition(yValue[j]) - (yPoint - (i + 1) * yScale)) / (float) linePointRadius)));
                                canvas.drawLine(xPoint + xScale / 2.0f + j * xScale, yPoint - (i + 1) * yScale,
                                        xPoint + (j + 1) * xScale - (float) (linePointRadius * Math.cos(radian)), yPoint - (i + 1) * yScale, gridPaint);
                                canvas.drawLine(xPoint + (j + 1) * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint - (i + 1) * yScale,
                                        xPoint + xScale / 2.0f + (j + 1) * xScale, yPoint - (i + 1) * yScale, gridPaint);
                            } else {
                                canvas.drawLine(xPoint + xScale / 2.0f + j * xScale, yPoint - (i + 1) * yScale,
                                        xPoint + xScale / 2.0f + (j + 1) * xScale, yPoint - (i + 1) * yScale, gridPaint);
                            }
                        }
                        if (Math.abs(calculateYPosition(yValue[yValue.length - 1]) - (yPoint - (i + 1) * yScale)) < linePointRadius) {
                            float radian = (float) (Math.asin((float) (calculateYPosition(yValue[yValue.length - 1]) - (yPoint - (i + 1) * yScale)) / linePointRadius));
                            canvas.drawLine(xPoint + xScale / 2.0f + (yValue.length - 1) * xScale, yPoint - (i + 1) * yScale,
                                    xPoint + yValue.length * xScale - (float) (linePointRadius * Math.cos(radian)), yPoint - (i + 1) * yScale, gridPaint);
                            if (yValue.length < xLabelCount) {
                                canvas.drawLine(xPoint + yValue.length * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint - (i + 1) * yScale,
                                        width - rightPadding - extendLength + gridExtendLength, yPoint - (i + 1) * yScale, gridPaint);
                            } else if (yValue.length == xLabelCount) {
                                if (gridExtendLength > linePointRadius * Math.cos(radian)) {
                                    canvas.drawLine(xPoint + yValue.length * xScale + (float) (linePointRadius * Math.cos(radian)), yPoint - (i + 1) * yScale,
                                            width - rightPadding - extendLength + gridExtendLength, yPoint - (i + 1) * yScale, gridPaint);
                                }
                            }
                        } else {
                            canvas.drawLine(xPoint + xScale / 2.0f + (yValue.length - 1) * xScale, yPoint - (i + 1) * yScale,
                                    width - rightPadding - extendLength + gridExtendLength, yPoint - (i + 1) * yScale, gridPaint);
                        }
                    } else {
                        canvas.drawLine(xPoint, yPoint - (i + 1) * yScale, width - rightPadding - extendLength + gridExtendLength
                                , yPoint - (i + 1) * yScale, gridPaint);
                    }
                } else {
                    canvas.drawLine(xPoint, yPoint - (i + 1) * yScale, width - rightPadding - extendLength + gridExtendLength,
                            yPoint - (i + 1) * yScale, gridPaint);
                }
            }
            //绘制Y轴上的小刻度线
            if (showScale) {
                canvas.drawLine(xPoint, yPoint - (i + 1) * yScale, xPoint + scaleLength,
                        yPoint - (i + 1) * yScale, axisPaint);
            }
            //绘制Y轴上的刻度值
            if (yLabel != null && yLabel.length > i) {
                textPaint.setTextSize(yLabelTextSize);
                textPaint.setColor(yLabelTextColor);
                Paint.FontMetrics f = textPaint.getFontMetrics();
                canvas.drawText(yLabel[i].toString(), xPoint - maxYLabelTextWidth / 2.0f - yLabelTextPadding,
                        yPoint - (i + 1) * yScale + (f.descent - f.ascent) / 2.0f - f.descent, textPaint);
            }
        }
        //绘制图表描述信息
        if (showDescription) {
            if (!TextUtils.isEmpty(description)) {
                textPaint.setTextSize(yLabelTextSize);
                Paint.FontMetrics f = textPaint.getFontMetrics();
                float top = (f.descent - f.ascent) / 2.0f > axisExtendLength ? (f.descent - f.ascent) / 2.0f : 0;
                textPaint.setTextSize(descriptionTextSize);
                textPaint.setColor(descriptionTextColor);
                if (descriptionTextLocation == 0) {
                    canvas.drawText(description, xPoint,
                            topPadding + extendLength - axisExtendLength - descriptionTextPadding - top, textPaint);
                } else if (descriptionTextLocation == 1) {
                    canvas.drawText(description, xPoint + xLength / 2.0f,
                            topPadding + extendLength - axisExtendLength - descriptionTextPadding - top, textPaint);
                } else if (descriptionTextLocation == 2) {
                    canvas.drawText(description, xPoint + xLength,
                            topPadding + extendLength - axisExtendLength - descriptionTextPadding - top, textPaint);
                }
            }
        }
        if (showDefault) {//绘制无数据时的默认提示
            textPaint.setTextSize(defaultTextSize);
            textPaint.setColor(defaultTextColor);
            Paint.FontMetrics f = textPaint.getFontMetrics();
            canvas.drawText(defaultText, xPoint + xLength / 2.0f,
                    yPoint - yLength / 2.0f + (f.descent - f.ascent) / 2.0f - f.descent, textPaint);
        } else {
            //绘制数据图
            if (yValue != null && yValue.length > 0) {
                pointPaint.setColor(lineColor);
                textPaint.setTextSize(yValueTextSize);
                textPaint.setColor(yValueTextColor);
                Paint.FontMetrics f = textPaint.getFontMetrics();
                for (int i = 0; i < yValue.length; i++) {
                    //绘制折线和折线交点
                    if (linePointStyle == ARC_STYLE) {
                        pointPaint.setStrokeWidth(linePointArcWidth);
                        pointPaint.setStyle(Paint.Style.STROKE);
                        RectF rectF1 = new RectF(xPoint + (i + 1) * xScale - linePointRadius, calculateYPosition(yValue[i]) - linePointRadius,
                                xPoint + (i + 1) * xScale + linePointRadius, calculateYPosition(yValue[i]) + linePointRadius);
                        canvas.drawArc(rectF1, 0, 360, false, pointPaint);//绘制交点
                        //绘制折线
                        if (yValue.length > 1 && i < yValue.length - 1) {
                            float radian = (float) (Math.atan((calculateYPosition(yValue[i]) - calculateYPosition(yValue[i + 1])) / (float) xScale));
                            canvas.drawLine((float) (xPoint + (i + 1) * xScale + linePointRadius * Math.cos(radian)), (float) (calculateYPosition(yValue[i]) - linePointRadius * Math.sin(radian)),
                                    (float) (xPoint + (i + 2) * xScale - linePointRadius * Math.cos(radian)), (float) (calculateYPosition(yValue[i + 1]) + linePointRadius * Math.sin(radian)), linePaint);
                        }
                    } else {
                        pointPaint.setStyle(Paint.Style.FILL);
                        if (yValue.length > 1 && i < yValue.length - 1) {
                            canvas.drawLine(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]), xPoint + (i + 2) * xScale,
                                    calculateYPosition(yValue[i + 1]), linePaint);//绘制折线
                        }
                        if (linePointStyle == CIRCLE_STYLE) {
                            canvas.drawCircle(xPoint + (i + 1) * xScale, calculateYPosition(yValue[i]), linePointRadius, pointPaint);//绘制交点
                        }
                        if (linePointStyle == NONE_STYLE && yValue.length == 1) {
                            canvas.drawCircle(xPoint + xScale, calculateYPosition(yValue[i]), linePointRadius, pointPaint);
                        }
                    }
                    //绘制数据文本
                    if (showYValueText) {
                        if (calculateYPosition(yValue[i]) - linePointRadius - yValueTextPadding - f.descent + f.ascent <= yPoint - yLength) {
                            canvas.drawText(String.format("%.1f", yValue[i]), xPoint + (i + 1) * xScale,
                                    calculateYPosition(yValue[i]) + linePointRadius - f.ascent + yValueTextPadding, textPaint);
                        } else if (calculateYPosition(yValue[i]) + linePointRadius + yValueTextPadding + f.descent - f.ascent >= yPoint) {
                            canvas.drawText(String.format("%.1f", yValue[i]), xPoint + (i + 1) * xScale,
                                    calculateYPosition(yValue[i]) - linePointRadius - yValueTextPadding, textPaint);
                        } else {
                            if (yValueTextLocation == 0) {
                                canvas.drawText(String.format("%.1f", yValue[i]), xPoint + (i + 1) * xScale,
                                        calculateYPosition(yValue[i]) - linePointRadius - yValueTextPadding, textPaint);
                            } else if (yValueTextLocation == 1) {
                                canvas.drawText(String.format("%.1f", yValue[i]), xPoint + (i + 1) * xScale,
                                        calculateYPosition(yValue[i]) + linePointRadius - f.ascent + yValueTextPadding, textPaint);
                            }
                        }
                    }
                }
            }
        }
    }

    private int calculateYPosition(float yValue) {
        if (maxYValue == DEFAULT_MAXYVALUE) {//未设置maxYValue的值
            if (yLabel != null && yLabel.length > 0) {
                try {
                    maxYValue = Float.parseFloat(yLabel[yLabel.length - 1].toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return (int) (yPoint - yValue / maxYValue * yLength);
    }

}
