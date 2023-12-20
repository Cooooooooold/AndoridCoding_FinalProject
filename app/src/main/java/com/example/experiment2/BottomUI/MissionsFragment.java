package com.example.experiment2.BottomUI;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment2.MainActivity;
import com.example.experiment2.R;
import com.example.experiment2.TopUI.DailyTaskFragment;
import com.example.experiment2.TopUI.NormalTaskFragment;
import com.example.experiment2.TopUI.WeeklyTaskFragment;
import com.example.experiment2.data.TaskItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MissionsFragment extends Fragment {
    private String[] tabHeaderStrings = {"每日任务", "每周任务", "普通任务"};

    // 添加一个变量来存储当前选中的 Tab 索引
    private int currentTabIndex = 0;
    public MissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_missions, container, false);

        ViewPager2 viewPager = rootview.findViewById(R.id.view_pager_final);
        TabLayout tabLayout = rootview.findViewById(R.id.tab_layout_final);

        FragmentAdapter pagerAdapter = new FragmentAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabHeaderStrings[position])).attach();
        // 设置 ViewPager2 的页面更改监听器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentTabIndex = position;  // 更新当前选中的 Tab 索引
            }
        });
        return rootview;
    }
    public void handleTaskResult(Intent data) {
        // 在开始处添加日志
        Log.d("MissionsFragment", "handleTaskResult called");

        // 从 Intent 中获取任务数据
        String name = data.getStringExtra("name");
        double points = data.getDoubleExtra("points", 0);
        int quantity = data.getIntExtra("quantity", 0);
        String category = data.getStringExtra("category");

        Fragment currentFragment = getChildFragmentManager().getFragments().get(currentTabIndex);
        if (currentFragment instanceof DailyTaskFragment) {
            Log.d("MissionsFragment", "Adding task to DailyTaskFragment");
            ((DailyTaskFragment) currentFragment).addTaskItem(new TaskItem(name, points, quantity, category));
        } else if (currentFragment instanceof WeeklyTaskFragment) {
            Log.d("MissionsFragment", "Adding task to WeeklyTaskFragment");
            // 类似地处理 WeeklyTaskFragment
            ((WeeklyTaskFragment) currentFragment).addTaskItem(new TaskItem(name, points, quantity, category));
        } else if (currentFragment instanceof NormalTaskFragment) {
            Log.d("MissionsFragment", "Adding task to NormalTaskFragment");
            // 类似地处理 NormalTaskFragment
            ((NormalTaskFragment) currentFragment).addTaskItem(new TaskItem(name, points, quantity, category));
        }
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            String tag;
            switch (position) {
                case 0:
                    fragment = new DailyTaskFragment();
                    break;
                case 1:
                    fragment = new WeeklyTaskFragment();
                    break;
                case 2:
                    fragment = new NormalTaskFragment();
                    break;
                default:
                    throw new IllegalStateException("Unexpected position: " + position);
            }

            // 为每个 Fragment 设置一个唯一的标签
            fragment.setArguments(new Bundle());
            fragment.getArguments().putString("fragmentTag", "fragment" + position);
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}
