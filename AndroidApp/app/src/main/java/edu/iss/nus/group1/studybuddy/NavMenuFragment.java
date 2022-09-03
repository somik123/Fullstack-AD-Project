package edu.iss.nus.group1.studybuddy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class NavMenuFragment extends Fragment {

    protected TabLayout tabLayout;
    protected SharedPreferences.Editor editor;

    public NavMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_menu, container, false);
        SharedPreferences pref = this.getActivity().getSharedPreferences("tabLayout", Context.MODE_PRIVATE);
        editor = pref.edit();
        tabLayout = view.findViewById(R.id.tab_layout);

        int tabId = pref.getInt("tabId", -1);
        if (tabId != -1) {
            tabLayout.getTabAt(tabId).select();
        }

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < 4; i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(getTabView(i));
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selectedTab = tabLayout.getSelectedTabPosition();
                goToPage(selectedTab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int selectedTab = tabLayout.getSelectedTabPosition();
                goToPage(selectedTab);
            }
        });

        return view;
    }

    protected void goToPage(int selectedTab) {
        int intentFlags = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION;
        switch (selectedTab) {
            case 0:
                editor.putInt("tabId", 0);
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(intentFlags);
                startActivity(intent);
                break;
            case 1:
                editor.putInt("tabId", 1);
                editor.apply();
                intent = new Intent(getActivity(), EventActivity.class);
                intent.setFlags(intentFlags);
                startActivity(intent);
                break;
            case 2:
                editor.putInt("tabId", 2);
                editor.apply();
                intent = new Intent(getActivity(), BooksActivity.class);
                intent.setFlags(intentFlags);
                startActivity(intent);
                break;
            case 3:
                editor.putInt("tabId", 3);
                editor.apply();
                intent = new Intent(getActivity(), ProfileActivity.class);
                intent.setFlags(intentFlags);
                startActivity(intent);
                break;
        }
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(this.getContext()).inflate(R.layout.custom_tab, null);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        switch (position) {
            case 0:
                img.setImageResource(R.drawable.chat_btn);
                break;
            case 1:
                img.setImageResource(R.drawable.events_btn);
                break;
            case 2:
                img.setImageResource(R.drawable.book_btn);
                break;
            case 3:
                img.setImageResource(R.drawable.profile_btn);
                break;
        }

        return v;
    }
}