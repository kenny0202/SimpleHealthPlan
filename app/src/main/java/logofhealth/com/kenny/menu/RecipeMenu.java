package logofhealth.com.kenny.menu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.RecipeViewPagerAdapter;
import logofhealth.com.kenny.adapter.SlidingTabLayout;

/**
 * Created by Kenny on 2/15/2015.
 */
public class RecipeMenu extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;
    RecipeViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Recipes", "Meal Plan", "Add"};
    int Numboftabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_menu);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        adapter = new RecipeViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}