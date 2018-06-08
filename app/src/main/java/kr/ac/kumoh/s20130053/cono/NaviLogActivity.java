package kr.ac.kumoh.s20130053.cono;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class NaviLogActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private ArrayList<TableRow> mArray;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_log);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(R.string.log);

        // 결제 누적액 초기화
        amount = 0;

        // TableRow 정렬을 위한 객체 초기화
        mArray = new ArrayList<>();

        // TableLayout 객체 획득
        tableLayout = findViewById(R.id.nav_log_tableLayout);

        // DB 에서 현재 결제정보 획득
        db.collection("Hairshop").document(hairshop_token).collection("Payment").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (String.valueOf(document.getData().get("ID")).equals(mAuth.getCurrentUser().getEmail())) {
                                    mArray.add(DynamicTableRowCreator(
                                            String.valueOf(document.getData().get("date")),
                                            String.valueOf(document.getData().get("designer")),
                                            String.valueOf(document.getData().get("Cut")),
                                            String.valueOf(document.getData().get("Payment_Method")),
                                            String.valueOf(document.getData().get("Money"))));
                                }
                            }
                            // 누적결제액 표기
                            Toast.makeText(NaviLogActivity.this, "누적결제액 : " + CurrencyConverse(amount) + "원", Toast.LENGTH_LONG).show();

                            // 동적으로 생성한 TableRow 뷰를 날짜별로 정렬해주는 코드
                            Date date = new Date();
                            TableRow tmp;
                            for (int i = 0; i < mArray.size() - 1; i++)
                                for (int j = i + 1; j < mArray.size(); j++) {
                                    ((TextView) (mArray.get(i).getChildAt(0))).getText().toString(); // 날짜정보
                                    if (date.CompareLatestDate(
                                            ((TextView) (mArray.get(i).getChildAt(0))).getText().toString(),
                                            ((TextView) (mArray.get(j).getChildAt(0))).getText().toString())) {
                                        tmp = mArray.get(i);
                                        mArray.set(i, mArray.get(j));
                                        mArray.set(j, tmp);
                                    }
                                }

                            // TableLayout 에 정렬이 끝난 TableRow 를 삽입
                            for (int i = 0; i < mArray.size(); i++)
                                tableLayout.addView(mArray.get(i));
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    protected TableRow DynamicTableRowCreator(String date, String designer, String type, String paymentMethod, String paymentAmount) {
        // 새로 추가될 TableRow(행) 객체 생성
        TableRow tableRow = new TableRow(this);

        // TableRow(행)의 속성 정의
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        // 각 아이템(열)의 속성 정의
        TableRow.LayoutParams ItemStyle = new TableRow.LayoutParams();
        ItemStyle.width = 0;
        ItemStyle.weight = 1;
        ItemStyle.setMargins(0, 0, 0, 1);

        // 각 아이템(열)의 객체 생성
        TextView textView1 = new TextView(this);
        TextView textView2 = new TextView(this);
        TextView textView3 = new TextView(this);
        TextView textView4 = new TextView(this);
        TextView textView5 = new TextView(this);

        // 각 아이템(열)의 정렬 속성 정의
        textView1.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView2.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView3.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView4.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        textView5.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

        textView1.setBackgroundColor(Color.WHITE);
        textView2.setBackgroundColor(Color.WHITE);
        textView3.setBackgroundColor(Color.WHITE);
        textView4.setBackgroundColor(Color.WHITE);
        textView5.setBackgroundColor(Color.WHITE);

        // 데이터 삽입
        textView1.setText(date);
        textView2.setText(designer);
        textView3.setText(type);
        textView4.setText(paymentMethod);
        textView5.setText(CurrencyConverse(Integer.valueOf(paymentAmount)));

        // 객체를 TableRow 에 삽입
        tableRow.addView(textView1, ItemStyle);
        tableRow.addView(textView2, ItemStyle);
        tableRow.addView(textView3, ItemStyle);
        tableRow.addView(textView4, ItemStyle);
        tableRow.addView(textView5, ItemStyle);

        // 누적결제액 계산
        amount += Integer.valueOf(paymentAmount);

        return tableRow;
    }

    protected String CurrencyConverse(int value) {
        return new DecimalFormat("###,###,###,###").format(value);
    }
}

