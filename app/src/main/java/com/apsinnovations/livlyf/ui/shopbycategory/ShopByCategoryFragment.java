package com.apsinnovations.livlyf.ui.shopbycategory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.apsinnovations.livlyf.R;
import com.apsinnovations.livlyf.adapters.MyFragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ShopByCategoryFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter myFragmentPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        View view = inflater.inflate(R.layout.fragment_shop_by_category, container, false);
        tabLayout = view.findViewById(R.id.frag_tablayout);
        viewPager = view.findViewById(R.id.frag_viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Plants"));
        tabLayout.addTab(tabLayout.newTab().setText("Pots"));
        tabLayout.addTab(tabLayout.newTab().setText("Seeds"));
        tabLayout.addTab(tabLayout.newTab().setText("Pebbles"));
        tabLayout.addTab(tabLayout.newTab().setText("Tools"));
        tabLayout.addTab(tabLayout.newTab().setText("Decor"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(myFragmentPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }
}