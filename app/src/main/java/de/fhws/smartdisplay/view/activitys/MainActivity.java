package de.fhws.smartdisplay.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import de.fhws.smartdisplay.R;
import de.fhws.smartdisplay.database.SettingsDataSource;
import de.fhws.smartdisplay.view.adapter.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    SettingsDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        dataSource = new SettingsDataSource(this);
    }
}