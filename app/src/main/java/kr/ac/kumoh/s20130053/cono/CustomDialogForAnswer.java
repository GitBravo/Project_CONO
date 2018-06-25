package kr.ac.kumoh.s20130053.cono;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class CustomDialogForAnswer extends Dialog implements View.OnClickListener {
    private static final int LAYOUT = R.layout.customdialog_for_answer;

    private Context context;

    private TextView contentTv;
    private TextView cancelTv;
    private TextView commitTv;
    private TextInputEditText editText;
    private String mCommentId;

    // ListView 를 갱신해주기 위해서는 반드시 mArray 와 mAdapter 객체가 필요함
    private ArrayAdapter<String> mAdapter = null;
    private ArrayList<String> mArray = null;

    private String content;

    public CustomDialogForAnswer(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public CustomDialogForAnswer(Context context, String content, String mCommentId, ArrayList<String> mArray, ArrayAdapter<String> mAdapter) {
        super(context);
        this.context = context;
        this.content = content;
        this.mCommentId = mCommentId;
        this.mAdapter = mAdapter;
        this.mArray = mArray;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        contentTv = findViewById(R.id.dialog_comment_content);
        editText = findViewById(R.id.edit_text);

        cancelTv = findViewById(R.id.dialog_left_btn);
        commitTv = findViewById(R.id.dialog_right_btn);

        cancelTv.setOnClickListener(this);
        commitTv.setOnClickListener(this);

        if (content != null) {
            contentTv.setText(content);
        }
    }

    @Override
    public void onClick(View v) {
        // 덧글 작성 버튼을 누를 경우 다이얼로그에 대한 액션설정
        switch (v.getId()) {
            case R.id.dialog_left_btn:
                cancel();
                break;
            case R.id.dialog_right_btn:
                if (editText.getText().length() < 1) {
                    Toast.makeText(this.context, R.string.dialog_write_failure, Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    // 여기부터 댓글 작성한 내용을 추출해서 DB로 보내는 소스코드
                    String Answer_id = RandomString.getRandomString(20); // 다큐먼트 id 생성을 위한 랜덤 난수(알파벳+숫자) 추출

                    Map<String, Object> item = new HashMap<>();
                    item.put("id", mAuth.getCurrentUser().getEmail() + "");
                    item.put("content", editText.getText() + "");
                    db.collection("Hairshop").document(hairshop_token).collection("Comment").document(mCommentId).collection("Answer").document(Answer_id).set(item); // DB에 삽입

                    new LocalDataRefresher(mArray, mAdapter, mCommentId).TryAnswerRefresh(hairshop_token); // 댓글 갱신
                    Toast.makeText(this.context, R.string.counseling_write_complete, Toast.LENGTH_SHORT).show(); // 성공 알림창 생성
                    cancel();
                }
        }
    }
}


