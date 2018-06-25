package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;

public class FragmentCounseling extends android.support.v4.app.Fragment {
    public static ArrayList<ListData> mArray = null;
    public static MyCustomAdapter mAdapter = null;
    private ListView mList = null;

    private View rootView;

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_counseling, container, false);

        mArray = new ArrayList<>();
        mAdapter = new MyCustomAdapter(rootView.getContext(), R.layout.customlist_for_counseling);
        mList = rootView.findViewById(R.id.frag_counseling_list);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /* Fragment <-> Activity 상호간에 데이터 교환은 Intent.putExtra() 메소드를 통해서는 기본타입만 가능하다.
                 * 이 방법은 오로지 Activity <-> Activity 상호간에서만 가능한 방법이며 데이터 전달을 위해 다른 방법을 사용해야한다.
                 * 본 소스코드에서는 Bundle 객체에 데이터를 담아 Intent.putExtras() 메소드를 이용해서 번들 자체를 보내는 방법을 사용한다
                 * (사실 이게 가장 쉬운 방법이다 -_-) */
                Intent intent = new Intent(getContext(), CounselingDetailActivity.class);
                Bundle bundle = new Bundle(); // 번들 객체 선언
                bundle.putString("Stitle", mArray.get(position).getTitle());
                bundle.putString("Sid", mArray.get(position).getId());
                bundle.putString("Stimestamp", mArray.get(position).getTimestamp());
                bundle.putString("Scontent", mArray.get(position).getContent());
                bundle.putString("ScommentId", mArray.get(position).getCommentId());
                intent.putExtras(bundle); // 번들 객체 자체를 Intent.putExtras() 메소드를 통해 전송
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LocalDataRefresher(mArray, mAdapter).TryCommentRefresh(hairshop_token); // 상담글 갱신
    }

    // ListData 클래스는 FragmentCounseling 내부의 ListView 에 들어갈 데이터를 정의한다.
    protected static class ListData {
        private String title; // 게시글 명
        private String timestamp; // 게시글 작성시간
        private String id; // 작성 아이디
        private String content; // 게시글 내용
        private String commentId; // 게시글 아이디

        public ListData(String title, String id, String timestamp, String content, String commentId) {
            this.title = title;
            this.id = id;
            this.timestamp = timestamp;
            this.content = content;
            this.commentId = commentId;
        }

        public String getTitle() {
            return this.title;
        }

        public String getId() {
            return this.id;
        }

        public String getTimestamp() {
            return this.timestamp;
        }

        public String getContent() {
            return content;
        }

        public String getCommentId() {
            return commentId;
        }
    }

    // MyCustomAdapter 를 위한 ViewHolder 디자인패턴
    protected static class ViewHolder {
        TextView TextView_1; // 글제목이 실리는 곳
        TextView TextView_2; // 게시시간이 실리는 곳
        TextView TextView_3; // 아이디가 실리는 곳
    }

    // ListData(ArrayList<ListData>) -> ViewHolder(MyCustomAdapter) -> View(ConvertView)
    protected class MyCustomAdapter extends ArrayAdapter<ListData> {
        protected LayoutInflater inf;

        public MyCustomAdapter(@NonNull Context context, int resource) {
            super(context, resource);
            inf = LayoutInflater.from(context);
        }

        public int getCount() {
            return mArray.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            // 여기서 convertView 는 이전에 그려졌던 View 를 재활용한 객체
            ViewHolder vh;
            if (convertView == null) {
                convertView = inf.inflate(R.layout.customlist_for_counseling, parent, false);
                vh = new ViewHolder();
                vh.TextView_1 = convertView.findViewById(R.id.custom_TextView1);
                vh.TextView_2 = convertView.findViewById(R.id.custom_TextView2);
                vh.TextView_3 = convertView.findViewById(R.id.custom_TextView3);
                convertView.setTag(vh); // findViewById 재활용
            } else
                vh = (ViewHolder) convertView.getTag();
            vh.TextView_1.setText(mArray.get(position).getTitle());
            vh.TextView_2.setText(mArray.get(position).getTimestamp());
            vh.TextView_3.setText(mArray.get(position).getId());
            return convertView;
        }
    }
}
