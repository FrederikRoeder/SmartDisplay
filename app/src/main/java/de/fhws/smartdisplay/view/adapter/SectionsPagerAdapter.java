package de.fhws.smartdisplay.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.view.fragments.GamesFragment;
import de.fhws.smartdisplay.view.fragments.HomeFragment;
import de.fhws.smartdisplay.view.fragments.SettingsFragment;
import de.fhws.smartdisplay.view.fragments.TimerFragment;
import de.fhws.smartdisplay.view.fragments.TodoFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.home, R.string.todo, R.string.timer, R.string.games, R.string.settings};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new TodoFragment();
            case 2:
                return new TimerFragment();
            case 3:
                return new GamesFragment();
            case 4:
                return new SettingsFragment();
            default: return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return 5;
    }
}