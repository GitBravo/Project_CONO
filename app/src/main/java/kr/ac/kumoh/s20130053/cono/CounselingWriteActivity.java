package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class CounselingWriteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edit_title = null;
    private EditText edit_content = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counseling_write);

        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);

        Button left_btn = findViewById(R.id.frag_counseling_write_left_btn);
        Button right_btn = findViewById(R.id.frag_counseling_write_right_btn);
        edit_title = findViewById(R.id.frag_counseling_write_title);
        edit_content = findViewById(R.id.frag_counseling_write_content);

        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_counseling_write_left_btn:
                finish();
                break;
            case R.id.frag_counseling_write_right_btn:
                if (edit_title.getText().length() < 1) { // 제목을 입력하지 않을 경우
                    Toast.makeText(this, R.string.counseling_write_titleHint, Toast.LENGTH_SHORT).show();
                    break;
                } else if (edit_content.getText().length() < 1) { // 내용을 입력하지 않을 경우
                    Toast.makeText(this, R.string.counseling_write_contentHint, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    // 다큐먼트 id 생성을 위한 랜덤 난수(알파벳+숫자) 추출
                    RandomString randomString = new RandomString();
                    String comment_id = randomString.getRandomString(20);

                    Map<String, Object> item = new HashMap<>();
                    item.put("comment_id", comment_id);
                    item.put("title", edit_title.getText() + "");
                    item.put("content", edit_content.getText() + "");
                    item.put("id", mAuth.getCurrentUser().getEmail());
                    item.put("timestamp", getTimestamp());
                    item.put("hairshop_name", hairshop_name);
                    db.collection("Hairshop").document(hairshop_token).collection("Comment").document(comment_id).set(item);
                    Toast.makeText(this, R.string.counseling_write_complete, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
        }
    }

    public String getTimestamp() {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
        String str = dayTime.format(new Date(time));
        return str;
    }
}
