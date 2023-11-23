package com.example.experiment2;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// 确保导入了 ClockView 类
import com.example.experiment2.view.ClockView;

public class ClockViewFragment extends Fragment {

    private ClockView clockView; // 添加这个变量

    // 其他构造方法和实例方法...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clock_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clockView = view.findViewById(R.id.clockView); // 获取 ClockView 实例
        clockView.start(); // 启动时钟
    }

    @Override
    public void onPause() {
        super.onPause();
        if (clockView != null) {
            clockView.stop(); // 停止时钟
        }
    }

    // 其他方法...
}
