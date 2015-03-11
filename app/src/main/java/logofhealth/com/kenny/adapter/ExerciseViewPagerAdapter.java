package logofhealth.com.kenny.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import logofhealth.com.kenny.exercise.Exercises;
import logofhealth.com.kenny.exercise.Programs;

/**
 * Created by Kenny on 2/15/2015.
 */
public class ExerciseViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ExerciseViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Exercises();
            case 1:
                return new Programs();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}