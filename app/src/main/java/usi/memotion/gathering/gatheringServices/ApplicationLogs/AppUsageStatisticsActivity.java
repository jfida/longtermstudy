package usi.memotion.gathering.gatheringServices.ApplicationLogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import usi.memotion.R;

public class AppUsageStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage_statistics);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, AppUsageStatisticsFragment.newInstance())
                    .commit();
        }
    }
}
