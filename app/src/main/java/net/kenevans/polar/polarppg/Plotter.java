package net.kenevans.polar.polarppg;

import android.util.Log;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYRegionFormatter;
import com.androidplot.xy.XYSeriesFormatter;
import com.polar.sdk.api.model.PolarOhrData;

@SuppressWarnings("WeakerAccess")
public class Plotter implements IConstants {
    public static final int AVERAGE_SIZE = 50;

    private net.kenevans.polar.polarppg.PlotterListener mListener;
    private final XYSeriesFormatter<XYRegionFormatter> mFormatter;
    //    private final XYSeriesFormatter<XYRegionFormatter> mFormatter1;
//    private final XYSeriesFormatter<XYRegionFormatter> mFormatter2;
//    private final XYSeriesFormatter<XYRegionFormatter> mFormatter3;
    private final SimpleXYSeries mSeries;
    //    private final SimpleXYSeries mSeries1;
//    private final SimpleXYSeries mSeries2;
//    private final SimpleXYSeries mSeries3;
    private int mPrev = Integer.MAX_VALUE;
    private RunningAverage mAverage;

    /**
     * The next index in the data
     */
    private long mDataIndex;
    /**
     * The number of points to show
     */
    private final int mDataSize;
    /**
     * The total number of points to keep
     */
    private final int mTotalDataSize;


    public Plotter(int totalDataSize, int dataSize,
                   String title,
                   Integer lineColor, boolean showVertices) {
        this.mDataSize = dataSize;
        this.mTotalDataSize = totalDataSize;
        this.mDataIndex = 0;
        this.mAverage = new RunningAverage(AVERAGE_SIZE);

        mFormatter = new LineAndPointFormatter(lineColor,
                showVertices ? lineColor : null, null, null);
        mFormatter.setLegendIconEnabled(false);
//        mFormatter1 = new LineAndPointFormatter(Color.rgb(0, 128, 0),
//                showVertices ? lineColor : null, null, null);
//        mFormatter1.setLegendIconEnabled(false);
//        mFormatter2 = new LineAndPointFormatter(Color.BLUE,
//                showVertices ? lineColor : null, null, null);
//        mFormatter2.setLegendIconEnabled(false);
//        mFormatter3 = new LineAndPointFormatter(Color.rgb(255, 165, 0),
//                showVertices ? lineColor : null, null, null);
//        mFormatter3.setLegendIconEnabled(false);

        mSeries = new SimpleXYSeries("Chan 0");
//        mSeries1 = new SimpleXYSeries("Chan 1");
//        mSeries2 = new SimpleXYSeries("Chan 2");
//        mSeries3 = new SimpleXYSeries("Chan 3");
    }

    public SimpleXYSeries getmSeries() {
        return mSeries;
    }

//    public SimpleXYSeries getmSeries1() {
//        return mSeries1;
//    }
//
//    public SimpleXYSeries getmSeries2() {
//        return mSeries2;
//    }
//
//    public SimpleXYSeries getmSeries3() {
//        return mSeries3;
//    }

    public XYSeriesFormatter<XYRegionFormatter> getmFormatter() {
        return mFormatter;
    }

//    public XYSeriesFormatter<XYRegionFormatter> getmFormatter1() {
//        return mFormatter1;
//    }
//
//    public XYSeriesFormatter<XYRegionFormatter> getmFormatter2() {
//        return mFormatter2;
//    }
//
//    public XYSeriesFormatter<XYRegionFormatter> getmFormatter3() {
//        return mFormatter3;
//    }

    /**
     * Implements a strip chart adding new data at the end.
     *
     * @param plot         The associated XYPlot.
     * @param polarOhrData The data that came in.
     */
    public void addValues(XYPlot plot, PolarOhrData polarOhrData) {
//        Log.d(TAG,
//                "addValues: dataIndex=" + dataIndex + " seriesSize=" +
//                series.size());
        int nSamples = polarOhrData.samples.size();
        if (nSamples == 0) return;

        // Convert to a scale that is similar to mV for ECG
        double scale = .0001;

        // Add the new values, removing old values if needed
        int dval;
        double val;
        for (PolarOhrData.PolarOhrSample data : polarOhrData.samples) {
            if (mSeries.size() >= mTotalDataSize) {
                mSeries.removeFirst();
            }
            mDataIndex++;

            // The data value
            dval = data.channelSamples.get(0)
                    + data.channelSamples.get(1)
                    + data.channelSamples.get(2)
                    - 3 * data.channelSamples.get(3);
            mAverage.add(dval);
            val = dval - mAverage.average();
            mSeries.addLast(mDataIndex, scale * val);
//            Log.d(TAG, "dval=" + dval + " average=" +  " val=" + val);
//            Log.d(TAG, "size: " + mSeries.size() + " val: " + val
//                    + " ppg0: " + data
//                    .channelSamples.get(0)
//                    + " ppg1: " + data
//                    .channelSamples.get(1)
//                    + " ppg2: " + data
//                    .channelSamples.get(2)
//                    + " ambient: " + data
//                    .channelSamples.get(3));
            updatePlot(plot);
        }
    }

    /**
     * Implements a strip chart adding new data at the end.
     *
     * @param plot         The associated XYPlot.
     * @param polarOhrData The data that came in.
     */
    public void addValues1(XYPlot plot, PolarOhrData polarOhrData) {
//        Log.d(TAG,
//                "addValues: dataIndex=" + dataIndex + " seriesSize=" +
//                series.size());
        int nSamples = polarOhrData.samples.size();
        if (nSamples == 0) return;

        // Convert to a scale that is similar to mV for ECG
        double scale = .001;
        int dval, val;

        // Add the new values, removing old values if needed
        for (PolarOhrData.PolarOhrSample data : polarOhrData.samples) {
            if (mSeries.size() >= mTotalDataSize) {
                mSeries.removeFirst();
//                mSeries1.removeFirst();
//                mSeries2.removeFirst();
//                mSeries3.removeFirst();
            }
            mDataIndex++;

            // Derivative
            // The data value
            dval = data.channelSamples.get(0)
                    + data.channelSamples.get(1)
                    + data.channelSamples.get(2)
                    - 3 * data.channelSamples.get(3);
            if (mPrev == Integer.MAX_VALUE) {
                val = 0;
            } else {
                val = dval - mPrev;
            }
            mPrev = dval;
            mSeries.addLast(mDataIndex, scale * val);
//            Log.d(TAG, "size: " + mSeries.size() + " val: " + val
//                    + " ppg0: " + data
//                    .channelSamples.get(0)
//                    + " ppg1: " + data
//                    .channelSamples.get(1)
//                    + " ppg2: " + data
//                    .channelSamples.get(2)
//                    + " ambient: " + data
//                    .channelSamples.get(3));

            // Single value
//            int val = data.channelSamples.get(0)
//                    + data.channelSamples.get(1)
//                    + data.channelSamples.get(2)
//                    - 3 * data.channelSamples.get(3);

            // Subtract ambient
//            mSeries.addLast(mDataIndex, scale * (data.channelSamples.get
//            (0) - data.channelSamples.get(3)));
//            mSeries1.addLast(mDataIndex, scale * (data.channelSamples.get
//            (1) - data.channelSamples.get(3)));
//            mSeries2.addLast(mDataIndex, scale * (data.channelSamples.get
//            (2) - data.channelSamples.get(3)));
//            mSeries3.addLast(mDataIndex, scale * (data.channelSamples.get
//            (3) - data.channelSamples.get(3)));

            // Returned values, not subtracting ambient
//            mSeries.addLast(mDataIndex, scale * data.channelSamples.get(0));
//            mSeries1.addLast(mDataIndex, scale * data.channelSamples.get(1));
//            mSeries2.addLast(mDataIndex, scale * data.channelSamples.get(2));
//            mSeries3.addLast(mDataIndex, scale * data.channelSamples.get(3));

            updatePlot(plot);
//            Log.d(TAG, "addValues thread: " + Thread.currentThread()
//            .getName());
        }
    }

    public void updatePlot(XYPlot plot) {
        long plotMin, plotMax;
        plotMin = mDataIndex - mDataSize;
        plotMax = mDataIndex;
        plot.setDomainBoundaries(plotMin, plotMax, BoundaryMode.FIXED);
//        Log.d(TAG, "updatePlot: plotMin=" + plotMin + " plotMax=" + plotMax);
        mListener.update();
    }

    /**
     * Clear the plot and reset dataIndex;
     */
    public void clear() {
        mAverage = new RunningAverage(AVERAGE_SIZE);
        mPrev = Integer.MAX_VALUE;
        mDataIndex = 0;
        mSeries.clear();
//        mSeries1.clear();
//        mSeries2.clear();
//        mSeries3.clear();
        mListener.update();
    }

    public void setmListener(net.kenevans.polar.polarppg.PlotterListener mListener) {
        this.mListener = mListener;
    }

    public long getmDataIndex() {
        return mDataIndex;
    }
}
