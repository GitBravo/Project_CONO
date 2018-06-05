package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class NaviModifyActivity extends AppCompatActivity {

    private TextView mDisplayNameTv;
    private TextView mIdTv;
    private TextView mCommentTv;
    private int mCommentCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_modify);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(R.string.nav_str_current_info);

        mDisplayNameTv = findViewById(R.id.nav_modify_displayName);
        mIdTv = findViewById(R.id.nav_modify_id);
        mCommentTv = findViewById(R.id.nav_modify_comment);

        // 구글 로그인시 획득 불가능한 정보 : PhoneNumber, ProviderId,

        mDisplayNameTv.setText(mAuth.getCurrentUser().getDisplayName());
        mIdTv.setText(mAuth.getCurrentUser().getEmail());
        ShowCommentCount();
    }

    protected void ShowCommentCount(){
        mCommentCount = 0;
        db.collection("Hairshop").document(hairshop_token).collection("Comment").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getData().get("id").equals(mAuth.getCurrentUser().getEmail()))
                                    mCommentCount++;
                            }
                            mCommentTv.setText(String.valueOf(mCommentCount));
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }
}