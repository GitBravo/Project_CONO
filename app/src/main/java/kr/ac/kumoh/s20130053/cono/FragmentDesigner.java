package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static kr.ac.kumoh.s20130053.cono.MainActivity.hairshop_token;

public class FragmentDesigner extends android.support.v4.app.Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String[]> mArray;
    private View rootView;

    private FireStorageImageManager fireStorageImageManager;
    private CustomDialogForDesigner dialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_designer, container, false);

        // Firestorage 를 조작하기 위한 객체 할당
        fireStorageImageManager = new FireStorageImageManager();

        // Item 리스트에 아이템 객체 넣기
        mArray = new ArrayList<>();

        // RecyclerView 뷰를 갖고와서 뷰를 가변크기로 설정한다.
        mRecyclerView = rootView.findViewById(R.id.frag_designer);
        mRecyclerView.setHasFixedSize(true);

        // 2열 세로 구조의 StaggeredGrid 레이아웃을 사용한다.
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        // 지정된 레이아웃매니저를 RecyclerView에 부착한다.
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyCustomAdapter(mArray, getContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 아직 DB 에서 데이터를 가져오지 않았는데 다이얼로그를 실행하는 경우를 방지하기 위한 예외 if 문
                if(mArray.size() > 0 && position < mArray.size()) {
                    dialog = new CustomDialogForDesigner(rootView.getContext(), mArray, position);
                    dialog.show();
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        new LocalDataRefresher(hairshop_token).TryDesignerRefresh(mArray, mAdapter);
    }

    class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.ViewHolder> {
        private Context mContext;
        private ArrayList<String[]> mArray;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public MyCustomAdapter(ArrayList<String[]> items, Context context) {
            this.mArray = items;
            this.mContext = context;
        }
        // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // 새로운 뷰를 만든다
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
            return new ViewHolder(view);
        }

        // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.titleTv.setText(mArray.get(position)[0]);
            holder.infoTv.setText(mArray.get(position)[1]);
            fireStorageImageManager.imageDownload(rootView.getContext(), holder.imageView, "designer_image/",mArray.get(position)[2]);

            setAnimation(holder.imageView, position);
        }



        // 필수로 Generate 되어야 하는 메소드 3
        @Override
        public int getItemCount() {
            return mArray.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView imageView;
            public TextView titleTv;
            public TextView infoTv;

            public ViewHolder(View view) {
                super(view);
                imageView = view.findViewById(R.id.cardview_image);
                titleTv = view.findViewById(R.id.cardview_title);
                infoTv = view.findViewById(R.id.cardview_content);
            }
        }

        private void setAnimation(View viewToAnimate, int position) {
            // 새로 보여지는 뷰라면 애니메이션을 해줍니다
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }
}