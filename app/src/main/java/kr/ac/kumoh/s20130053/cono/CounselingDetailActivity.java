package kr.ac.kumoh.s20130053.cono;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class CounselingDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;
    private TextView mTimestamp;
    private TextView mId;
    private TextView mContent;
    private String mCommentId = null;

    private ArrayList<String> mArray = null;
    private ListView mList = null;
    private ArrayAdapter<String> mAdapter = null;

    /* Fragment -> Detail 로 보내주는 데이터는 총 5가지
    * 1. commentID
    * 2. title
    * 3. timestamp
    * 4. id
    * 5. content */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counseling_detail);

        if (hairshop_name != null)
            getSupportActionBar().setTitle(hairshop_name);

        /* FragmentCounseling 에서 보낸 String 데이터 Bundle 을 받아서 저장 */
        Bundle bundle = getIntent().getExtras();
        mCommentId = bundle.getString("ScommentId");

        // 서버로 부터 받아서 번들로 전송된 각 게시글의 데이터를 출력하는 부분(Firestore -> mArray -> Bundle -> View)
        mTitle = findViewById(R.id.frag_counseling_detail_title);
        mTitle.setText(bundle.getString("Stitle"));
        mTimestamp = findViewById(R.id.frag_counseling_detail_timestamp);
        mTimestamp.setText(bundle.getString("Stimestamp"));
        mId = findViewById(R.id.frag_counseling_detail_id);
        mId.setText(bundle.getString("Sid"));
        mContent = findViewById(R.id.frag_counseling_detail_content);
        mContent.setText(bundle.getString("Scontent"));

        // DB에 저장된 댓글 불러오는 코드
        mArray = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArray);
        mList = findViewById(R.id.frag_counseling_detail_list);
        mList.setAdapter(mAdapter);

        // 리스너 부착
        findViewById(R.id.frag_counseling_detail_btn_comment).setOnClickListener(this);
        findViewById(R.id.frag_counseling_detail_btn_delete).setOnClickListener(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new LocalDataRefresher(mArray,mAdapter, mCommentId).TryAnswerRefresh(hairshop_token); // 댓글 갱신
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_counseling_detail_btn_comment:
                // 커스텀 다이얼로그 띄우기 위해 미리 구현해둔 객체 인스턴스 할당
                CustomDialogForAnswer dialog = new CustomDialogForAnswer(CounselingDetailActivity.this, mContent.getText().toString(), mCommentId, mArray, mAdapter);
                dialog.show();
                break;
            case R.id.frag_counseling_detail_btn_delete:
                mId = findViewById(R.id.frag_counseling_detail_id);
                // 본인이 작성한 글일 경우 삭제 수행
                if (mId.getText().equals(mAuth.getCurrentUser().getEmail())) {
                    db.collection("Hairshop").document(hairshop_token).collection("Comment").document(mCommentId)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error deleting document", e);
                                }
                            });
                    Toast.makeText(this, R.string.counseling_delete_complete, Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    // 본인이 작성한 글이 아닐 경우 삭제 실패
                    Toast.makeText(this, R.string.counseling_delete_failure, Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }
}
