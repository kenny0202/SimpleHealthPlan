package logofhealth.com.kenny.menu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import logofhealth.com.kenny.R;
import logofhealth.com.kenny.adapter.ExerciseViewPagerAdapter;
import logofhealth.com.kenny.adapter.SlidingTabLayout;

/**
 * Created by Kenny on 2/15/2015.
 */
public class ExerciseMenu extends ActionBarActivity {

    Toolbar toolbar;
    ViewPager pager;
    ExerciseViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Exercise", "Program"};
    int Numboftabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_menu);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        adapter = new ExerciseViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(false);

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.exerciseTabScrollColor);
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