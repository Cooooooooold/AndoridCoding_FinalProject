package com.example.experiment2;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar; // 更正这里的导入
import androidx.fragment.app.FragmentTransaction;

import com.example.experiment2.TopUI.DailyTaskFragment;
import com.example.experiment2.TopUI.NormalTaskFragment;
import com.example.experiment2.TopUI.WeeklyTaskFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskFragment extends Fragment {

    public NewTaskFragment() {
        // Required empty public constructor
    }

    public static NewTaskFragment newInstance() {
        NewTaskFragment fragment = new NewTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_task, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar_new_task); // 确保使用的是 androidx 的 Toolbar

        // 确保宿主 Activity 已经准备好处理此方法调用
        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            activity.setSupportActionBar(toolbar);

            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }

            // 处理返回按钮的点击事件
            toolbar.setNavigationOnClickListener(v -> {
                // 模拟返回按钮按下
                getActivity().onBackPressed();
            });
        }

        // 设置 Spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.task_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // 设置 Spinner 的选择项监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Fragment selectedFragment;
                switch (position) {
                    case 0:
//                        selectedFragment = DailyTaskFragment.newInstance(/* 参数 */);
                        break;
                    case 1:
//                        selectedFragment = WeeklyTaskFragment.newInstance(/* 参数 */);
                        break;
                    case 2:
//                        selectedFragment = NormalTaskFragment.newInstance(/* 参数 */);
                        break;
                    default:
                        return;
                }
                // 切换到选择的 Fragment
//                switchFragment(selectedFragment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 无操作
            }
        });
        return view;
    }
    private void switchFragment(Fragment fragment) {
        // 执行 Fragment 事务以替换为选择的 Fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
