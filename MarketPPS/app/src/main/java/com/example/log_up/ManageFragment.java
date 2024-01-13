package com.example.log_up;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


public class ManageFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage, container, false);
        viewPager = view.findViewById(R.id.viewPager_manage);
        tabLayout = view.findViewById(R.id.tab_layout_manage);

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerMange viewPagerMange = new ViewPagerMange(getChildFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerMange);

        return view;
    }
}