package lokter.hu.coolwallet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lokter.hu.coolwallet.OptionsActivity;
import lokter.hu.coolwallet.R;
import lokter.hu.coolwallet.model.Item;
import lokter.hu.coolwallet.model.Lab3l;
import lokter.hu.coolwallet.model.Repository;

import static java.lang.Math.abs;

/**
 * Created by Balint on 2017. 10. 13..
 */

public class DiagramFragment extends Fragment implements AdapterView.OnItemSelectedListener, ViewPager.OnPageChangeListener {
    public static final String DIAGRAM_TYPE = "diagram_type";
    public static final String DIAGRAM_MONTHLY = "Monthly";
    public static final String DIAGRAM_YEARLY = "Yearly";
    public static final String DIAGRAM_POS = "diagram_pos";
    @BindView(R.id.tvIncome)
    TextView tvIncome;
    @BindView(R.id.tvExpense)
    TextView tvExpense;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    private int pos;
    @BindView(R.id.bar_text)
    TextView barText;
    @BindView(R.id.label_spinner)
    Spinner spinner;
    @BindView(R.id.chartExpense)
    PieChart chartExpense;
    @BindView(R.id.barChartBalance)
    LineChart barChartBalance;
    Unbinder unbinder;

    private String type = DIAGRAM_MONTHLY;
    public static DateTime selectedDate = DateTime.now();
    DateTime startDate;
    DateTime endDate;
    ViewPager vp;
    private int startingAmount = 0;

    public DiagramFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diagram, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        selectedDate = DateTime.now();
        selectedDate = selectedDate.secondOfDay().setCopy(0);
        type = (String) getArguments().getSerializable(DIAGRAM_TYPE);
        pos = getArguments().getInt(DIAGRAM_POS);
        barText.setText(typeToString());
        fillSpinner();
        //startDate = DateTime.now();
        //refreshDates();
        spinner.setOnItemSelectedListener(this);
        chartExpense.setRotationEnabled(false);
        chartExpense.getDescription().setEnabled(false);
        chartExpense.setCenterText(typeToString() + " " + getString(R.string.dispersion));
        barChartBalance.getDescription().setEnabled(false);
        //barChartBalance.getLegend().setEnabled(false);

        vp = (ViewPager) getActivity().findViewById(R.id.vpMain);
        vp.addOnPageChangeListener(this);
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        startingAmount = settings.getInt(OptionsActivity.STARTING_AMOUNT,startingAmount);
        Log.i("Life_CYCLE","OnResume");

    }

    private void animate(){
        boolean anim = true;
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        anim = settings.getBoolean(OptionsActivity.ANIMATION,anim);
        if(anim){
            chartExpense.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            //barChartBalance.animateX(700);
            barChartBalance.invalidate();
        }else {
            chartExpense.invalidate();
            barChartBalance.invalidate();
        }
    }
    private void loadExpenseChart() {

        List<PieEntry> entries = new ArrayList<>();
        List<Item> items = Repository.between(startDate, endDate);
        if(items.size() == 0)
            chartExpense.setCenterText(getString(R.string.no_items));
        else
            chartExpense.setCenterText(typeToString() + " " + getString(R.string.dispersion));
        List<Lab3l> labels = Repository.getLabels();
        for (Lab3l label : labels) {
            int sum = 0;

            for (Item item : items) {
                if (item.getAmount() < 0 && item.getLabel().getId() == label.getId())
                    sum += abs(item.getAmount());

            }
            if (sum != 0)
                entries.add(new PieEntry(sum, label.getName()));
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData(dataSet);
        chartExpense.setData(data);
        // vp = (ViewPager) getActivity().findViewById(R.id.vpMain);
        // vp.addOnPageChangeListener(this);

    }

    private void fillSpinner() {
        switch (type) {
            case DIAGRAM_MONTHLY:
                List<String> months;
                months = new ArrayList<>();
                months.add(getString(R.string.january));
                months.add(getString(R.string.february));
                months.add(getString(R.string.march));
                months.add(getString(R.string.april));
                months.add(getString(R.string.may));
                months.add(getString(R.string.june));
                months.add(getString(R.string.jule));
                months.add(getString(R.string.august));
                months.add(getString(R.string.september));
                months.add(getString(R.string.october));
                months.add(getString(R.string.november));
                months.add(getString(R.string.december));
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, months);
                spinner.setAdapter(adapter);
                spinner.setSelection(DateTime.now().getMonthOfYear() - 1);
                break;
            case DIAGRAM_YEARLY:
                List<String> years = new ArrayList<>();
                years.add("2016");
                years.add("2017");
                years.add("2018");
                years.add("2019");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, years);
                spinner.setAdapter(adapter1);
                spinner.setSelection(1);
                break;
        }
    }

    public void refreshDates() {
        switch (type) {
            case DIAGRAM_MONTHLY:

                startDate = new DateTime(selectedDate);
                startDate = startDate.monthOfYear().setCopy(spinner.getSelectedItemPosition() + 1);
                startDate = startDate.dayOfMonth().setCopy(1);
                endDate = new DateTime(startDate).plusMonths(1);
                Log.i("Date", startDate.toString());
                Log.i("Date", endDate.toString());

                break;
            case DIAGRAM_YEARLY:
                startDate = new DateTime(selectedDate);
                selectedDate = selectedDate.year().setCopy(Integer.parseInt((String) spinner.getSelectedItem()));
                startDate = startDate.year().setCopy(Integer.parseInt((String) spinner.getSelectedItem()));
                startDate = startDate.monthOfYear().setCopy(1);
                endDate = new DateTime(startDate).plusYears(1);
                Log.i("Date", startDate.toString());
                Log.i("Date", endDate.toString());
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        refreshDates();
        loadExpenseChart();
        loadBalanceChart();
        animate();
        refreshBalances();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        int currPos = vp.getCurrentItem();
        Log.i("SCROLL", "Type : " + type);
        Log.i("SCROLL", "CurrPos : " + currPos + " Pos : " + pos);
        Log.i("SCROLL", "State : " + state);

        if (state == 1 && (currPos == pos - 1 || currPos == pos + 1)) {
            refreshDates();
            loadExpenseChart();
            loadBalanceChart();
            animate();
            refreshBalances();
        }
    }

    private void refreshBalances() {
        int[] values = Repository.balance(startDate,endDate);
        tvIncome.setText("" + values[0]);
        tvExpense.setText("" + values[1]);
        tvBalance.setText("" + (values[0] + values[1]));
    }

    private void loadBalanceChart() {
        int dataCount = 0;
        DateTime start;
        start = new DateTime(startDate);
        DateTime end = null;
        switch (type){
            case DIAGRAM_MONTHLY:
                dataCount = startDate.dayOfMonth().getMaximumValue();
                end = start.plusDays(1);
                break;
            case DIAGRAM_YEARLY:
                dataCount = 12;
                end = start.plusMonths(1);
                break;
        }

        List<Entry> entries = new ArrayList<>();
        int sum = Repository.currentAmount(start) + startingAmount;
        for (int j = 0; j < dataCount ; j++){
            int[] incomeExpense = Repository.balance(start,end);
            sum += incomeExpense[0] + incomeExpense[1];
            entries.add(new Entry( (float)j,(float)sum));
            Log.i("Line",type + " : " + sum);
            switch (type){
                case DIAGRAM_MONTHLY:
                    start = start.plusDays(1);
                    end = end.plusDays(1);
                    break;
                case DIAGRAM_YEARLY:
                    start = start.plusMonths(1);
                    end = end.plusMonths(1);
                    break;
            }
        }



        LineDataSet dataSet = new LineDataSet(entries,"Balance");
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataSet.setDrawCircles(false);
        dataSet.setDrawCircleHole(false);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setDrawValues(false);

        LineData data = new LineData(dataSet);
        barChartBalance.setData(data);

    }
    public String typeToString(){
        switch (type){
            case DIAGRAM_MONTHLY :
                return getString(R.string.monthly);
            case DIAGRAM_YEARLY:
                return getString(R.string.yearly);
        }
        return "";
    }
}