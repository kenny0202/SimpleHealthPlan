package logofhealth.com.kenny.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import logofhealth.com.kenny.recipe.CreateRecipe;
import logofhealth.com.kenny.recipe.MealPlan;
import logofhealth.com.kenny.recipe.Recipes;

/**
 * Created by Kenny on 2/15/2015.
 */
public class RecipeViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public RecipeViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new Recipes();
            case 1:
                return new MealPlan();
            case 2:
                return new CreateRecipe();
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