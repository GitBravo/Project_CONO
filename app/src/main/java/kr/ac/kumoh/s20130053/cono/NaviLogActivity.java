package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;

public class NaviLogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_log);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);
    }
}
