package kr.ac.kumoh.s20130053.cono;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private Button left_btn;
    private Button center_btn;
    private Button right_btn;

    public static final int GET_GALLERY_IMAGE = 112; //
    private Uri uri; // 기기 내부의 사진 파일 위치

    String comment_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counseling_write);

        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);

        left_btn = findViewById(R.id.frag_counseling_write_left_btn);
        right_btn = findViewById(R.id.frag_counseling_write_right_btn);
        center_btn = findViewById(R.id.frag_counseling_write_center_btn);
        edit_title = findViewById(R.id.frag_counseling_write_title);
        edit_content = findViewById(R.id.frag_counseling_write_content);

        left_btn.setOnClickListener(this); // 취소
        center_btn.setOnClickListener(this); // 사진첨부
        right_btn.setOnClickListener(this); // 글작성

        // 다큐먼트 id 생성을 위한 랜덤 난수(알파벳+숫자) 추출
        RandomString randomString = new RandomString();
        comment_id = randomString.getRandomString(20);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_counseling_write_left_btn:
                finish(); // 취소
                break;
            case R.id.frag_counseling_write_center_btn:
                if (center_btn.getText().equals("사진첨부")){
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GET_GALLERY_IMAGE);
                }else{
                    uri = null;
                    center_btn.setText("사진첨부");
                    center_btn.setTextColor(Color.BLACK);
                }
                break;
            case R.id.frag_counseling_write_right_btn:
                // 글 작성
                if (edit_title.getText().length() < 1) { // 제목을 입력하지 않을 경우
                    Toast.makeText(this, R.string.counseling_write_titleHint, Toast.LENGTH_SHORT).show();
                    break;
                } else if (edit_content.getText().length() < 1) { // 내용을 입력하지 않을 경우
                    Toast.makeText(this, R.string.counseling_write_contentHint, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    // Firestore 데이터 저장
                    Map<String, Object> item = new HashMap<>();
                    item.put("comment_id", comment_id);
                    item.put("title", edit_title.getText() + "");
                    item.put("content", edit_content.getText() + "");
                    item.put("id", mAuth.getCurrentUser().getEmail());
                    item.put("timestamp", getTimestamp());
                    item.put("hairshop_name", hairshop_name);
                    db.collection("Hairshop").document(hairshop_token).collection("Comment").document(comment_id).set(item);

                    // 사진 파일 첨부되었다면, Firestorage 사진 파일 저장
                    if(uri != null)
                        new FirestoreImageManager().imageUpload(comment_id, uri);

                    Toast.makeText(this, R.string.counseling_write_complete, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                }
        }
    }

    public String getTimestamp() {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy.M.d");
        String str = dayTime.format(new Date(time));
        return str;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GET_GALLERY_IMAGE:
                    uri = data.getData(); //URI 형식으로 사진 데이터 획득
                    center_btn.setText("첨부취소");
                    center_btn.setTextColor(Color.BLUE);
                    break;
                default:
                    break;
            }
        }
    }
}
