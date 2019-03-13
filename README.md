# 前言
>实现一个折线图，效果如下：

<img width="469" height="288" src="https://github.com/xuzhb24/LineChart/blob/master/app/src/main/res/mipmap-hdpi/demo.PNG"/>

# 属性说明
```java
CharSequence[] xLabel;          //X轴刻度值文本
float xLabelTextSize;           //X轴刻度值文本字体大小
int xLabelTextColor;            //X轴刻度值文本字体颜色
float xLabelTextPadding;        //X轴刻度值文本顶部和X轴的边距

CharSequence[] yLabel;          //Y轴刻度值文本
float yLabelTextSize;           //Y轴刻度值文本字体大小
int yLabelTextColor;            //Y轴刻度值文本字体颜色
float yLabelTextPadding;        //Y轴刻度值文本右部和Y轴的边距
float maxYValue;                //Y轴上的最大刻度值，未设置这个属性的值时，如果yLabel[yLabel.length-1]可以转换成数值，
                                //默认为yLabel[yLabel.length-1]对应的数值，否则默认取99999999f
                                
boolean showYValueText;         //是否显示数据文本，默认不显示
float yValueTextSize;           //数据文本字体大小
int yValueTextColor;            //数据文本字体颜色
float yValueTextPadding;        //数据文本和折线交点的距离
int yValueTextLocation;         //数据文本显示的位置：折线交点上方、折线交点下方

float axisWidth;                //轴线宽度
int axisColor;                  //轴线颜色
int axisPointStyle;             //轴线交点的样式：圆环、实心圆、无样式
float axisPointRadius;          //轴线交点半径，仅当axisPointStyle为circle或arc时有效
float axisPointArcWidth;        //轴线交点圆弧宽度，仅当axisPointStyle为arc时有效
float axisExtendLength;         //轴线向上和向右延伸的长度

float lineWidth;                //折线宽度
int lineColor;                  //折线颜色
int linePointStyle;             //折线交点的样式：圆环、实心圆、无样式
float linePointRadius;          //折线交点半径，仅当linePointStyle为circle或arc时有效
float linePointArcWidth;        //折线交点圆弧宽度，仅当linePointStyle为arc时有效

boolean showScale;              //是否显示轴线上的小刻度线，默认显示
float scaleLength;              //轴线上的小刻度线长度

boolean showGrid;               //是否显示网格，默认显示
float gridWidth;                //网格线宽度
int gridColor;                  //网格线颜色
float gridExtendLength;         //网格线向上和向右延伸的长度

String description;             //图表描述信息
boolean showDescription;        //是否显示图表描述信息，默认显示
float descriptionTextSize;      //图表描述信息文本字体大小
int descriptionTextColor;       //图表描述信息文本字体颜色
float descriptionTextPadding;   //图表描述信息文本和图表最上方的距离
int descriptionTextLocation;    //图表描述信息文本的位置：图表左边、中间或右边

float leftPadding;              //左边缘边距
float topPadding;               //上边缘边距
float rightPadding;             //右边缘边距
float bottomPadding;            //下边缘边距

boolean showDefault;            //是否显示无数据时的默认视图
String defaultText;             //无数据时的默认提示
float defaultTextSize;          //无数据时的提示文本字体大小
int defaultTextColor;           //无数据时的提示文本字体颜色

int defaultXLabelCount;         //X轴默认的刻度值个数
int defaultYLabelCount;         //Y轴默认的刻度值个数
```
# 使用方法
首先说明几点
## 第一
   X轴和Y轴上的刻度值xLabel和YLabel都是一个CharSequence数组，支持从xml资源文件引入，也可以在代码中设置，比如可以在XML布局中设置LineChart组件的yLabel属性如下：
```xml
app:yLabel="@array/speed"
<!--arrays.xml定义speed数组-->
<string-array name="speed">
  <item>4MB</item>
  <item>8MB</item>
  <item>12MB</item>
  <item>16MB</item>
</string-array>
```
也可以在代码中创建String数组再设置给X轴或Y轴。
```java
String[] dates = {"01/24", "02/02", "02/12", "02/25", "03/01"};
```
## 第二
当Y轴上的刻度值是数值类型的话，Y轴的最大刻度值，即maxYValue取Y轴最上面，也是值最大的刻度值对应的数值，这里注意设置yLabel时按从小到大排序。
如果Y轴上的刻度值不是数值类型，而是文本类型，那么要手动设置maxYValue的值，否则Y轴的最大刻度值默认取99999999f，而这就失去意义了。
## 第三
在设置了X轴刻度值，即xLabel属性的情况下，X轴的刻度值个数默认为xLabel的个数，前提是xLabel的个数比defaultXLabelCount大，这里的defaultXLabelCount就是X轴默认的刻度值个数，比如设置defaultXLabelCount为7，而xLabel数组的个数为4，那么X轴的刻度值个数总共有7个，而只会绘制前4个刻度值的文本，即xLabel中4个刻度值的文本内容。
## 第四
>>共有三种绘制折线图数据的方法：
```java
void drawData(float[] yValue);     //绘制折线图的数据
void drawData(float[] yValue, String[] xLabel);      //同时绘制折线图数据、X轴刻度值
void drawData(float[] yValue, String[] xLabel, String[] yLabel);   //同时绘制折线图数据、X轴刻度值、Y轴刻度值
```
>>以上图为例，代码中是这么编写的：
```java
float[] yValue = {16, 11.9f, 8.2f, 9, 12};
String[] dates = {"01/24", "02/02", "02/12", "02/25", "03/01"};
lineChart.drawData(yValue, dates);
```
