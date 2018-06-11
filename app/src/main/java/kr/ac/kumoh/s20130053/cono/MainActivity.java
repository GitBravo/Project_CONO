package kr.ac.kumoh.s20130053.cono;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance(); // 파이어스토어(DB) 초기화

    // ★중요★ 모든 Activity 에서 사용하기 위한 토큰 필드 선언부 ★ 중요 ★
    public static String hairshop_name = null;
    public static String hairshop_token = null;

    private ArrayList<String> mArray = null;
    private ArrayList<String> mToken = null;
    private ListView mList = null;
    private ArrayAdapter<String> mAdapter = null;

    // EditText 를 통해 리스트뷰를 검색하기 위한 레퍼런스 선언
    private ArrayList<String> mBackupArray = null; // 리스트뷰 백업용
    private ArrayList<String> mBackupToken = null; // 토큰 백업용
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArray = new ArrayList<>(); // 미용실 이름을 저장하는 리스트
        mToken = new ArrayList<>(); // 미용실 토큰을 저장하는 리스트
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mArray);

        mBackupArray = new ArrayList<>(); // 검색 기능 구현을 위한 Array 백업 생성
        mBackupToken = new ArrayList<>(); // 검색 기능 구현을 위한 Token 백업 생성

        // 기존의 콜렉션(테이블) 에 있는 데이터 중 다큐먼트(행) 값을 통해 필드(열) 값 추출 (추후에 아래 부분은 따로 클래스로 분리해둘것)
        db.collection("Hairshop").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                mArray.add(String.valueOf(document.getData().get("name")));
                                mToken.add(String.valueOf(document.getId()));
                            }
                            mAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                            mBackupArray.addAll(mArray);
                            mBackupToken.addAll(mToken);
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });

        mList = findViewById(R.id.hairshoplist);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i,
                                    long l) {
                // MainAcitity -> TabActivity 로 전환되기 전에 중요한 정보를 모두 저장한 뒤 인텐트 실시
                hairshop_name = mArray.get(i); // 선택한 미용실 이름 저장
                hairshop_token = mToken.get(i); // 선택한 미용실 토큰 저장
                startActivity(new Intent(MainActivity.this, TabActivity.class));
                finish();
            }
        });

        // EditText 에 입력할 때마다 검색된 결과를 보여주는 코드
        editText = findViewById(R.id.search_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Search(editText.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        new NetworkThread("서울 헤어샵").start(); // DB에 미용실 데이터를 저장하는 코드
//        new NetworkThread("대전 헤어샵").start();
//        new NetworkThread("대구 헤어샵").start();
//        new NetworkThread("부산 헤어샵").start();
    }

    public void Search(String searchText) {
        searchText = searchText.toLowerCase(); // 입력된 문자를 소문자로 변환
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        mArray.clear();
        mToken.clear();
        if (searchText.length() == 0) { // 문자 입력이 없을 때는 모든 데이터를 보여준다.
            mArray.addAll(mBackupArray);
            mToken.addAll(mBackupToken);
        }
        else { // 문자 입력이 있을 때
            for (int i = 0; i < mBackupArray.size(); i++) { // 리스트의 모든 데이터를 검색한다.
                if (mBackupArray.get(i).toLowerCase().contains(searchText)) { // mSearchArray의 모든 데이터에 입력받은 단어(searchText)가 포함되어 있으면 true를 반환한다.
                    // 검색된 데이터를 리스트에 추가한다.
                    mArray.add(mBackupArray.get(i));
                    mToken.add(mBackupToken.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        mAdapter.notifyDataSetChanged();
    }
}