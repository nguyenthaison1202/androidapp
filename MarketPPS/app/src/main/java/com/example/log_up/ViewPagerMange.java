package com.example.log_up;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerMange extends FragmentPagerAdapter {

    public ViewPagerMange(@NonNull FragmentManager fm, int behavior)
    {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new showStory();
            case 1:
                return new rejectStory();
            case 2:
                return new waitStory();
            default:
                return new showStory();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tittle = "";
        switch (position)
        {
            case 0:
                tittle = "Đang hiển thị";
                break;
            case 1:
                tittle = "Bị từ chối";
                break;
            case 2:
                tittle = "Tin đợi duyệt";
                break;
        }
        return tittle;
    }
}
