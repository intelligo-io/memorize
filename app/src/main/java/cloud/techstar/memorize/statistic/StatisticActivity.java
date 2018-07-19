package cloud.techstar.memorize.statistic;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import cloud.techstar.memorize.AppMain;
import cloud.techstar.memorize.Injection;
import cloud.techstar.memorize.R;
import cloud.techstar.memorize.database.Words;
import cloud.techstar.memorize.options.OptionsContract;
import cloud.techstar.memorize.options.OptionsPresenter;

public class StatisticActivity extends AppCompatActivity implements StatisticContract.View{
    private PieChart mChart;

    private StatisticContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        new StatisticPresenter(Injection.provideWordsRepository(AppMain.getContext()),
                this);

        mChart = findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);


        presenter.init();
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setStatData(List<Words> words) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        List<Words> memorizedWords = new ArrayList<Words>();
        List<Words> favWords = new ArrayList<Words>();
        List<Words> activeWords = new ArrayList<Words>();

        for (Words word : words) {

            if (word.isMemorize()) {
                memorizedWords.add(word);
            } else if (word.isFavorite() && !word.isMemorize()) {
                favWords.add(word);
            } else {
                activeWords.add(word);
            }
        }

        entries.add(new PieEntry((float) memorizedWords.size(),
                "Цээжилсэн үг "+memorizedWords.size(),
                getResources().getDrawable(R.drawable.ic_timeline)));
        entries.add(new PieEntry((float) favWords.size(),
                "Цээжилж байгаа "+favWords.size(),
                getResources().getDrawable(R.drawable.ic_timeline)));
        entries.add(new PieEntry((float) activeWords.size(),
                "Цээжлээгүй "+activeWords.size(),
                getResources().getDrawable(R.drawable.ic_timeline)));

        PieDataSet dataSet = new PieDataSet(entries, "Memorize results");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setCenterText(generateCenterSpannableText(words.size()));
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(int size) {

        SpannableString s = new SpannableString("Нийт үг :"+size);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()),0, s.length(), 0);
        return s;
    }

    @Override
    public void setPresenter(StatisticContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(String message) {

    }
}
