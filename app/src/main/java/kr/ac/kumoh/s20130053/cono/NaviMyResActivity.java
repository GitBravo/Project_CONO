package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_name;
import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;
import static kr.ac.kumoh.s20130053.cono.SignInActivity.mAuth;

public class NaviMyResActivity extends AppCompatActivity{

    private ListView mListView;
    private ArrayList<String[]> mArray;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_myres);
        if (hairshop_name != null)
            getSupportActionBar().setTitle(R.string.myReservation);

        mArray = new ArrayList<>();
        // DB 로부터 예약 정보 획득
        db.collection("Hairshop").document(hairshop_token).collection("Reservation").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // 내가 예약한 정보만 가져오기
                                if(document.getData().get("id").equals(mAuth.getCurrentUser().getEmail())){
                                    mArray.add(new String[]{document.getData().get("date").toString(),
                                            document.getData().get("time").toString(),
                                            document.getData().get("designer").toString(),
                                            document.getData().get("id").toString(),
                                            document.getData().get("document_id").toString()
                                    });
                                }
                            }
                            mAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });

        mAdapter = new CustomAdapter(this, R.id.nav_res_myres_tv1, mArray);
        mListView = findViewById(R.id.nav_res_myres_listview);
        mListView.setAdapter(mAdapter);
    }

    private class CustomAdapter extends ArrayAdapter<String[]> {
        private ArrayList<String[]> item;
        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private Button delete_btn;

        private Context mContext;

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<String[]> object) {
            super(context, textViewResourceId, object);
            this.mContext = context;
            this.item = object;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.customlist_for_res, null);
            }

            textView1 = v.findViewById(R.id.nav_res_myres_tv1);
            textView2 = v.findViewById(R.id.nav_res_myres_tv2);
            textView3 = v.findViewById(R.id.nav_res_myres_tv3);
            textView4 = v.findViewById(R.id.nav_res_myres_tv4);

            textView1.setText(item.get(position)[0]); // [0] 예약 날짜
            textView2.setText(item.get(position)[1]); // [1] 예약 시간
            textView3.setText(item.get(position)[2]); // [2] 디자이너
            textView4.setText(item.get(position)[3]); // [3] 예약자

            delete_btn = v.findViewById(R.id.nav_res_myres_delete_btn); // 헤어샵 예약 취소 버튼
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // DB의 예약 정보 삭제
                    db.collection("Hairshop").document(hairshop_token).collection("Reservation").document(item.get(position)[4])
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
                    // 로컬의 예약 정보 삭제
                    mArray.remove(position);
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(mContext, R.string.nav_res_delete_complete, Toast.LENGTH_SHORT).show(); // 삭제 성공 알림창 생성
                }
            });

            // 만약 예약 날짜가 지났다면 취소버튼 비활성화
            Date date = new Date();
            int[] intDate = date.getIntegerDate(textView1.getText().toString());
            if(date.isOverdue(intDate[0],intDate[1],intDate[2]))
                delete_btn.setEnabled(false);
            else
                delete_btn.setEnabled(true);
            return v;
        }
    }
}

