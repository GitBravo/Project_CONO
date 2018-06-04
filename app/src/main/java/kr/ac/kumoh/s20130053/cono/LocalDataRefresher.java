package kr.ac.kumoh.s20130053.cono;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.db;

public class LocalDataRefresher {
    private ArrayList<String> mArray;
    private ArrayAdapter<String> mAdapter;

    private ArrayList<FragmentCounseling.ListData> lArray;
    private FragmentCounseling.MyCustomAdapter lAdapter;

    private String mCommentId;
    private String mHairshopToken;

    LocalDataRefresher(String hairshop_token) {
        // Hairshop & Designer Information Refresh
        this.mHairshopToken = hairshop_token;
    }

    LocalDataRefresher(ArrayList<String> array, ArrayAdapter<String> adapter, String commentId) {
        // Answer Refresh
        this.mArray = array;
        this.mAdapter = adapter;
        this.mCommentId = commentId;
    }

    LocalDataRefresher(ArrayList<FragmentCounseling.ListData> lArray, FragmentCounseling.MyCustomAdapter lAdapter) {
        // Comment Refresh
        this.lArray = lArray;
        this.lAdapter = lAdapter;
    }

    protected void TryAnswerRefresh(String hairshop_token) {
        mArray.clear();
        db.collection("Hairshop").document(hairshop_token).collection("Comment").document(mCommentId).collection("Answer").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                mArray.add(String.valueOf("┕> " + document.getData().get("id") + " : " + document.getData().get("content")));
                            }
                            mAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    protected void TryCommentRefresh(String hairshop_token) {
        lArray.clear();
        db.collection("Hairshop").document(hairshop_token).collection("Comment").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // 현재 미용실에 부합하는 코멘트만 서버에서 로딩
                                lArray.add(new FragmentCounseling.ListData(String.valueOf(document.getData().get("title")),
                                        String.valueOf(document.getData().get("id")),
                                        String.valueOf(document.getData().get("timestamp")),
                                        String.valueOf(document.getData().get("content")),
                                        String.valueOf(document.getData().get("comment_id"))));
                            }
                            // 날짜순으로 리스트 갱신
                            Date date = new Date();
                            FragmentCounseling.ListData tmp;
                            for (int i = 0 ; i < lArray.size() - 1 ; i ++){
                                for (int j = i + 1 ; j < lArray.size() ; j ++){
                                    if (date.CompareLatestDate(lArray.get(i).getTimestamp(), lArray.get(j).getTimestamp())) {
                                        tmp = lArray.get(i);
                                        lArray.set(i, lArray.get(j));
                                        lArray.set(j, tmp);
                                    }
                                }
                            }

                            lAdapter.notifyDataSetChanged(); // 데이터가 추가가 끝날 때 리스트뷰 갱신
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    protected void TryInfoRefresh(final TextView intro,
                                  final TextView service,
                                  final TextView businessHour,
                                  final TextView holiday,
                                  final TextView phoneNumber,
                                  final TextView address,
                                  final TextView reprt,
                                  final TextView name,
                                  final TextView repretAddress) {
        db.collection("Hairshop").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.getId().equals(mHairshopToken)) {
                                    intro.setText(String.valueOf(document.getData().get("intro")));
                                    service.setText(String.valueOf(document.getData().get("service")));
                                    businessHour.setText(String.valueOf(document.getData().get("business_hours")));
                                    holiday.setText(String.valueOf(document.getData().get("holiday")));
                                    phoneNumber.setText(String.valueOf(document.getData().get("phone_number")));
                                    address.setText(String.valueOf(document.getData().get("address")));
                                    reprt.setText(String.valueOf(document.getData().get("representative")));
                                    name.setText(String.valueOf(document.getData().get("name")));
                                    repretAddress.setText(String.valueOf(document.getData().get("representative_address")));
                                }
                            }
                        } else
                            Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    protected void TryDesignerRefresh(final ArrayList<String[]> array, final RecyclerView.Adapter adapter) {
        array.clear();
        db.collection("Hairshop").document(mHairshopToken).collection("Designer").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                array.add(new String[]{
                                        String.valueOf(document.getData().get("name")),
                                        String.valueOf(document.getData().get("info")),
                                        String.valueOf(document.getData().get("designer_id"))});
                            }
                            adapter.notifyDataSetChanged();
                        } else
                            Log.d("TAG", "Error MyDesigner getting documents: ", task.getException());
                    }
                });
    }
}