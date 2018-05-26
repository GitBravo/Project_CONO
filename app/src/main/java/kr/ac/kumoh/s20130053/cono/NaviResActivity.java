package kr.ac.kumoh.s20130053.cono;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;

public class NaviResActivity extends AppCompatActivity {

    private CalendarView mCalendarView;
    private Spinner mTimeSpinner;
    private ArrayAdapter mAdapter;
    private TextView mTextView;

    private Spinner mDesignerSpinner;
    private ArrayList<String> mArray;
    private ArrayAdapter<String> mDesignerAdapter;

    private Button res_btn;
    private CustomDialogForRes dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_res);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);

        mTextView = findViewById(R.id.nav_res_day);
        mTextView.setText(getCurrentYear()+"."+getCurrentMonth()+"."+getCurrentDay()); // 기본 날짜값 => 현재 날짜

        mCalendarView = findViewById(R.id.nav_res_calendarView);
        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, final int year, final int month, final int dayOfMonth) {
                mTextView = findViewById(R.id.nav_res_day);
                if(year < getCurrentYear()
                        || (year == getCurrentYear() && month+1 < getCurrentMonth()
                        || (year == getCurrentYear() && month+1 == getCurrentMonth() && dayOfMonth < getCurrentDay()))) {
                    // 예약이 불가능한 경우
                    mTextView.setText(year + "." + (month + 1) + "." + dayOfMonth);
                    TextView undone_tv = findViewById(R.id.nav_res_undone);
                    TextView done_tv = findViewById(R.id.nav_res_done);
                    done_tv.setVisibility(View.GONE); // 예약 가능 숨기기
                    undone_tv.setVisibility(View.VISIBLE); // 예약 불가 표시
                    res_btn.setEnabled(false);
                }else{
                    // 예약이 가능한 경우
                    mTextView.setText(year + "." + (month + 1) + "." + dayOfMonth);
                    TextView undone_tv = findViewById(R.id.nav_res_undone);
                    TextView done_tv = findViewById(R.id.nav_res_done);
                    done_tv.setVisibility(View.VISIBLE); // 예약 가능 표시
                    undone_tv.setVisibility(View.GONE); // 예약 불가 숨기기
                    res_btn.setEnabled(true);
                }

            }
        });

        // DB 로부터 디자이너 정보 획득
        mArray = new ArrayList<>();
        db.collection("Hairshop").document(hairshop_token).collection("Designer").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                mArray.add(String.valueOf(document.getData().get("name")));
                            }
                            mDesignerAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });

        mAdapter = ArrayAdapter.createFromResource(this, R.array.TimeSpinner, android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner = findViewById(R.id.nav_res_TimeSpinner);
        mTimeSpinner.setAdapter(mAdapter);

        mDesignerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mArray);
        mDesignerSpinner = findViewById(R.id.nav_res_DesignerSpinner);
        mDesignerSpinner.setAdapter(mDesignerAdapter);

        res_btn = findViewById(R.id.nav_res_btn);
        res_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 예약버튼 클릭 시 Detail 다이얼로그 출력
                dialog = new CustomDialogForRes(NaviResActivity.this,
                        mTextView.getText().toString(),
                        mTimeSpinner.getSelectedItem().toString(),
                        mDesignerSpinner.getSelectedItem().toString());
                dialog.show();

                // 예약 정보 서버에 추가가 성공했을 때, 내 예약정보 보여주는 액티비티로 인텐트
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog_) {
                        if (dialog.onSuccess()) {
                            startActivity(new Intent(NaviResActivity.this, NaviMyResActivity.class));
                            finish();
                        }
                    }
                });
            }
        });
    }

    // 현재 년 값 추출 메소드
    public int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.YEAR);
    }

    // 현재 월 값 추출 메소드
    public int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.MONTH) + 1;
    }

    // 현재 일 값 추출 메소드
    public int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(cal.DATE);
    }
}
