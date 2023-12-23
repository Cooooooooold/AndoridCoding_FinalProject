package com.example.experiment2.TopUI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.experiment2.R;
import com.example.experiment2.data.DataBank;
import com.example.experiment2.data.TaskItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WeeklyStatisticsFragment extends Fragment {
    private final Calendar startCalendar = Calendar.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startCalendar.set(2023, Calendar.DECEMBER, 1, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_statistics, container, false);
        setupLineChart(view);
        return view;
    }
    public void setupLineChart(View view) {
        LineChart lineChartIncome = view.findViewById(R.id.weeklylineChartIncome);
        LineChart lineChartExpense = view.findViewById(R.id.weeklylineChartExpense);
        LineChart lineChartProfit = view.findViewById(R.id.weeklylineChartProfit);
        // 设置 Marker View
        CustomMarkerView markerView = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        lineChartIncome.setMarker(markerView);
        lineChartExpense.setMarker(markerView);

        DataBank dataBank = new DataBank();
        ArrayList<TaskItem> taskItems = dataBank.loadTotalTaskItems(getActivity());
        Log.d("WeeklyStats", "Task Items: " + taskItems.size());
        ArrayList<TaskItem> rewardItems = dataBank.loadrewardItems(getActivity());
        Log.d("RewardStats", "Task Items: " + taskItems.size());
//        ArrayList<PointChange> pointChanges = dataBank.loadPointChanges(getActivity());

        // 禁用描述标签
        lineChartIncome.getDescription().setEnabled(false);
        lineChartExpense.getDescription().setEnabled(false);
        lineChartProfit.getDescription().setEnabled(false);

        // 为收入创建数据集
        ArrayList<Entry> incomeEntries = generateincomeEntriesFromItems(taskItems);
        Log.d("WeeklyStats", "Income Entries: " + incomeEntries.size());
        LineDataSet incomeDataSet = new LineDataSet(incomeEntries, "任务积分");
        LineData incomeLineData = new LineData(incomeDataSet);
        // 设置线条和数据点的颜色为蓝色
        incomeDataSet.setColor(Color.BLUE);
        incomeDataSet.setCircleColor(Color.BLUE);

        // 为支出创建数据集
        ArrayList<Entry> expenseEntries = generaterewardEntriesFromItems(rewardItems);
        Log.d("RewardStats", "Income Entries: " + incomeEntries.size());
        LineDataSet expenseDataSet = new LineDataSet(expenseEntries, "奖励积分");
        LineData expenseLineData = new LineData(expenseDataSet);
        // 设置线条和数据点的颜色为红色
        expenseDataSet.setColor(Color.RED);
        expenseDataSet.setCircleColor(Color.RED);

        // 生成盈余图表的数据点
//        ArrayList<Entry> profitEntries = generateProfitEntries(pointChanges);
        // 生成盈余图表的数据点
        ArrayList<Entry> profitEntries = new ArrayList<>();
        int totalPoints = dataBank.getTotalPoints(getActivity());
        profitEntries.add(new Entry(0, totalPoints)); // 假设只有一个数据点

        // 创建数据集
        LineDataSet profitDataSet = new LineDataSet(profitEntries, "盈余");
        LineData profitLineData = new LineData(profitDataSet);
        profitDataSet.setColor(Color.GREEN); // 设置为绿色
        profitDataSet.setCircleColor(Color.GREEN);


        // 特别为 "Day 1" 添加 500 积分
//        dailyPoints.put(0, dailyPoints.getOrDefault(0, 0f) + 500f);

        // 格式化 X 轴标签
//        lineChartIncome.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int day = (int) value + 1; // 从1开始计数
//                return "Day " + day;
//            }
//        });
        lineChartIncome.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                return daysOfWeek[(int) value];
            }
        });
//        lineChartExpense.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int day = (int) value + 1; // 从1开始计数
//                return "Day " + day;
//            }
//        });
        lineChartExpense.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                return daysOfWeek[(int) value];
            }
        });
//        lineChartProfit.getXAxis().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                int day = (int) value + 1; // 从1开始计数
//                return "Day " + day;
//            }
//        });
        // 禁用 X 轴
        XAxis xAxisProfit = lineChartProfit.getXAxis();
        xAxisProfit.setDrawLabels(false); // 不绘制标签
        xAxisProfit.setDrawAxisLine(false); // 不绘制轴线
        xAxisProfit.setDrawGridLines(false); // 不绘制网格线

        // 设置数据到相应的 LineChart
        lineChartIncome.setData(incomeLineData);
        lineChartExpense.setData(expenseLineData);
        lineChartProfit.setData(profitLineData);
        // 刷新图表以显示更新的数据

        lineChartIncome.invalidate(); // 刷新图表
        lineChartExpense.invalidate();
        lineChartProfit.invalidate();
    }
    private ArrayList<Entry> generateincomeEntriesFromItems(ArrayList<TaskItem> items) {
        Map<Integer, Float> incomePoints = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            incomePoints.put(i, 0f); // 初始化 Map
        }
        for (TaskItem item : items) {
            if (item.getCompletionTime() > 0) {
                float xValue = convertTimestampToWeekday(item.getCompletionTime());
                int dayIndex = (int) xValue;
                float currentPoints = incomePoints.getOrDefault(dayIndex, 0f);
                incomePoints.put(dayIndex, (float) (currentPoints + item.getPoint()));
            }
        }

        ArrayList<Entry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : incomePoints.entrySet()) {
            entries.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return entries;
    }
    private ArrayList<Entry> generaterewardEntriesFromItems(ArrayList<TaskItem> items) {
        Map<Integer, Float> dailyPoints = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            dailyPoints.put(i, 0f); // 初始化 Map
        }
        for (TaskItem item : items) {
            if (item.getCompletionTime() > 0) {
                float xValue = convertTimestampToWeekday(item.getCompletionTime());
                int dayIndex = (int) xValue;
                float currentPoints = dailyPoints.getOrDefault(dayIndex, 0f);
                dailyPoints.put(dayIndex, (float) (currentPoints + item.getPoint()));
            }
        }

        ArrayList<Entry> entries = new ArrayList<>();
        for (Map.Entry<Integer, Float> entry : dailyPoints.entrySet()) {
            entries.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return entries;
    }
    private float convertTimestampToWeekday(long timestamp) {
        Calendar taskCalendar = Calendar.getInstance();
        taskCalendar.setTimeInMillis(timestamp);

        // 获取一周中的天数，Calendar.DAY_OF_WEEK 返回的值是 1（周日）到 7（周六）
        int dayOfWeek = taskCalendar.get(Calendar.DAY_OF_WEEK);
        // 将周日作为一周的最后一天
        return dayOfWeek == Calendar.SUNDAY ? 6 : dayOfWeek - 2;
    }


    @Override
    public void onResume() {
        super.onResume();
        View view = getView();
        if (view != null) {
            setupLineChart(view);
        }
    }
    public class CustomMarkerView extends MarkerView {
        private TextView tvContent;
        private String[] daysOfWeek = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        public CustomMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            int dayIndex = (int) e.getX();
            String day = daysOfWeek[Math.max(0, Math.min(dayIndex, 6))]; // 确保索引在 0 到 6 的范围内
            String text = "Day: " + day + "\nValue: " + e.getY();
            tvContent.setText(text);
            super.refreshContent(e, highlight);
        }
    }

}