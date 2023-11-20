package com.example.experiment2.BottomUI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment2.MainActivity;
import com.example.experiment2.R;
import com.example.experiment2.TopUI.DailyTaskFragment;
import com.example.experiment2.TopUI.NormalTaskFragment;
import com.example.experiment2.TopUI.WeeklyTaskFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MissionsFragment extends Fragment {
    private String[] tabHeaderStrings = {"每日任务", "每周任务", "普通任务"};

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

        return rootview;
    }

    private static class FragmentAdapter extends FragmentStateAdapter {
        private static final int NUM_TABS = 3;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new DailyTaskFragment();
                case 1:
                    return new WeeklyTaskFragment();
                case 2:
                    return new NormalTaskFragment();
                default:
                    throw new IllegalStateException("Unexpected position: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return NUM_TABS;
        }
    }
}
