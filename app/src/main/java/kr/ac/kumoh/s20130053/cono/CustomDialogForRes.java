package kr.ac.kumoh.s20130053.cono;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class CustomDialogForRes extends Dialog {
    private static final int LAYOUT = R.layout.customdialog_for_res;

    private Context mContext;
    private TextView mResDayTv;
    private TextView mResTimeTv;
    private TextView mResDesignerTv;

    private String mResDay;
    private String mResTime;
    private String mResDesigner;

    private Button Res_btn;
    private boolean success = false;


    public CustomDialogForRes(@NonNull Context context, String day, String time, String designer) {
        super(context);
        this.mContext = context;
        this.mResDay = day;
        this.mResTime = time;
        this.mResDesigner = designer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mResDayTv = findViewById(R.id.dialog_res_day);
        mResTimeTv = findViewById(R.id.dialog_res_time);
        mResDesignerTv = findViewById(R.id.dialog_res_designer);

        mResDayTv.setText(mResDay);
        mResTimeTv.setText(mResTime);
        mResDesignerTv.setText(mResDesigner);

        Res_btn = findViewById(R.id.dialog_res_detail_btn);
        Res_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 세부 예약 내용을 DB로 보내는 소스코드
                String res_id = RandomString.getRandomString(20); // 다큐먼트 id 생성을 위한 20자리 랜덤 난수(알파벳+숫자) 추출

                Map<String, Object> item = new HashMap<>();
                item.put("id", mAuth.getCurrentUser().getEmail() + "");
                item.put("date", mResDay);
                item.put("time", mResTime);
                item.put("designer", mResDesigner);
                item.put("document_id", res_id);
                db.collection("Hairshop").document(hairshop_token).collection("Reservation").document(res_id).set(item); // DB에 삽입

                Toast.makeText(mContext, R.string.nav_res_write_complete, Toast.LENGTH_SHORT).show(); // 성공 알림창 생성
                success = true;
                cancel();
            }
        });
    }

    public boolean onSuccess(){
        return success;
    }
}

