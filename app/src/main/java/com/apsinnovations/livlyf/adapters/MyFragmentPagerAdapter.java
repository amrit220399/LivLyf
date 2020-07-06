package com.apsinnovations.livlyf.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.apsinnovations.livlyf.ui.decor.DecorFragment;
import com.apsinnovations.livlyf.ui.pebbles.PebblesFragment;
import com.apsinnovations.livlyf.ui.plants.PlantsFragment;
import com.apsinnovations.livlyf.ui.pots.PotsFragment;
import com.apsinnovations.livlyf.ui.seeds.SeedsFragment;
import com.apsinnovations.livlyf.ui.tools.ToolsFragment;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public MyFragmentPagerAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlantsFragment();
            case 1:
                return new PotsFragment();
            case 2:
                return new SeedsFragment();
            case 3:
                return new PebblesFragment();
            case 4:
                return new ToolsFragment();
            case 5:
                return new DecorFragment();
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return tabCount;
    }
}