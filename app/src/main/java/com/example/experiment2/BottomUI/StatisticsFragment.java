package com.example.experiment2.BottomUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment2.R;
import com.example.experiment2.TopUI.AnnuallyStatisticsFragment;
import com.example.experiment2.TopUI.DailyStatisticsFragment;
import com.example.experiment2.TopUI.MonthlyStatisticsFragment;
import com.example.experiment2.TopUI.WeeklyStatisticsFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StatisticsFragment extends Fragment {
    private String[] tabHeaderStrings = {"日","周","月","年"};
    private Fragment currentActiveFragment;
    private int currentTabIndex = 0;
    public StatisticsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_statistics, container, false);
        ViewPager2 viewPager = rootview.findViewById(R.id.statistics_view_pager_final);
        TabLayout tabLayout = rootview.findViewById(R.id.statistics_tab_layout_final);
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
    public void setCurrentActiveFragment(Fragment fragment) {
        this.currentActiveFragment = fragment;
        // 其他逻辑...
    }
    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 4;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new DailyStatisticsFragment();
                    break;
                case 1:
                    fragment = new WeeklyStatisticsFragment();
                    break;
                case 2:
                    fragment = new MonthlyStatisticsFragment();
                    break;
                case 3:
                    fragment = new AnnuallyStatisticsFragment();
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