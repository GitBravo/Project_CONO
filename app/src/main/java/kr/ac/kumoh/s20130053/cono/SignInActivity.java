package kr.ac.kumoh.s20130053.cono;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123; // sign-in 인텐트를 위한 상수선언
    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance(); // Firebase 인스턴스 호출

        if (mAuth.getCurrentUser() != null) {
            // 이미 로그인 되어있는 경우 -> MainActivity 호출
            startActivity(new Intent(SignInActivity.this, TabActivity.class));
            finish();
        } else {
            // 로그인이 필요한 경우 -> Firebase 의 회원가입 인텐트 생성 후 호출
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(), // 이메일 인증
                                    new AuthUI.IdpConfig.GoogleBuilder().build())) // 구글 인증
                            .setLogo(R.drawable.title_image)
                            .setTheme(R.style.AppTheme_Signin_Theme)
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 회원가입 결과 처리하는 메소드
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // 회원가입 성공
                startActivity(new Intent(SignInActivity.this, MainActivity.class)); // 새로운 MainActivity 로 전환
                finish(); // 현재 SignInActivity 종료
            } else // 회원가입 실패
                finish();
        }
    }
}

