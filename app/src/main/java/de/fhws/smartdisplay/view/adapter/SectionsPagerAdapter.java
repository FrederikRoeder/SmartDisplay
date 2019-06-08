package de.fhws.smartdisplay.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.view.fragments.GamesFragment;
import de.fhws.smartdisplay.view.fragments.HomeFragment;
import de.fhws.smartdisplay.view.fragments.SettingsFragment;
import de.fhws.smartdisplay.view.fragments.TimerFragment;
import de.fhws.smartdisplay.view.fragments.TodoFragment;


public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.drawable.icon_home, R.drawable.icon_todo, R.drawable.icon_timer, R.drawable.icon_games, R.drawable.icon_settings};
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
        Drawable image = mContext.getResources().getDrawable(TAB_TITLES[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    @Override
    public int getCount() {
        // Show 5 total pages.
        return 5;
    }
}