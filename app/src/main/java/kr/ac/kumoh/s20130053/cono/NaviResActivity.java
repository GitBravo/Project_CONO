package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;

public class NaviResActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private CalendarView mCalendarView;
    private Spinner mSpinner;
    private ArrayAdapter mAdapter;
    private TextView mTextView;

    private ArrayList<String> mArray;
    private ArrayAdapter<String> mDesignerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_res);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);

        mCalendarView = findViewById(R.id.nav_res_calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                mTextView = findViewById(R.id.nav_res_day);
                mTextView.setText(year + "년 " + (month + 1) + "월 " + dayOfMonth + "일");
            }
        });

        mArray = new ArrayList<>();
        db.collection("Hairshop").document(hairshop_token).collection("Designer").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                mArray.add(String.valueOf(document.getData().get("name")) + " : " + String.valueOf(document.getData().get("info")));
                            }
                            mDesignerAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });

        mAdapter = ArrayAdapter.createFromResource(this, R.array.TimeSpinner, android.R.layout.simple_spinner_dropdown_item);
        mSpinner = findViewById(R.id.nav_res_TimeSpinner);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(this);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 이부분 수정해야함
        mDesignerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mArray);
        mSpinner = findViewById(R.id.nav_res_DesignerSpinner);
        mSpinner.setAdapter(mDesignerAdapter);
        mSpinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.nav_res_TimeSpinner:
                Toast.makeText(NaviResActivity.this, "핡", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_res_DesignerSpinner:
                Toast.makeText(NaviResActivity.this, "핡", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance(); // 현재 날짜 추출
        return cal.get(cal.YEAR);
    }

    public int getCurrentMonth() {
        Calendar cal = Calendar.getInstance(); // 현재 날짜 추출
        return cal.get(cal.MONTH) + 1;
    }

    public int getCurrentDay() {
        Calendar cal = Calendar.getInstance(); // 현재 날짜 추출
        return cal.get(cal.DATE);
    }
}
