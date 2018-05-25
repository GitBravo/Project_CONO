package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class NaviModifyActivity extends AppCompatActivity {

    private TextView mDisplayNameTv;
    private TextView mIdTv;
    private TextView mSinceTv;
    private TextView mLastConnTv;
    private TextView mCommentTv;
    private TextView mAnswerTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_modify);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(R.string.nav_str_current_info);

        mDisplayNameTv = findViewById(R.id.nav_modify_displayName);
        mIdTv = findViewById(R.id.nav_modify_id);
        mSinceTv = findViewById(R.id.nav_modify_since);
        mLastConnTv = findViewById(R.id.nav_modify_lastConn);
        mCommentTv = findViewById(R.id.nav_modify_comment);
        mAnswerTv = findViewById(R.id.nav_modify_answer);

        // 구글 로그인시 획득 불가능한 정보 : PhoneNumber, ProviderId,

        mDisplayNameTv.setText(mAuth.getCurrentUser().getDisplayName());
        mIdTv.setText(mAuth.getCurrentUser().getEmail());
        mSinceTv.setText("");

        // 여기부터 코딩하기

    }
}