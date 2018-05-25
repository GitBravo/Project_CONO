package kr.ac.kumoh.s20130053.cono;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
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

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    ArrayList mArray;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_designer, container, false);

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
                CustomDialogForDesigner dialog = new CustomDialogForDesigner(rootView.getContext(), mArray, position);// 여기서 아이템은 필요없고 포지션만 보내기
                dialog.show();
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
        private Context context;
        private ArrayList mItems;

        // Allows to remember the last item shown on screen
        private int lastPosition = -1;

        public MyCustomAdapter(ArrayList items, Context mContext) {
            mItems = items;
            context = mContext;
        }
        // 필수로 Generate 되어야 하는 메소드 1 : 새로운 뷰 생성

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 새로운 뷰를 만든다
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        // 필수로 Generate 되어야 하는 메소드 2 : ListView의 getView 부분을 담당하는 메소드
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
//            holder.imageView.setImageResource(mItems.get(position).cardview_image);
//            holder.textView.setText(mItems.get(position).cardview_title);
            holder.titleTv.setText(((ClipData.Item) mItems.get(position)).getText());
            holder.infoTv.setText(((ClipData.Item) mItems.get(position)).getHtmlText());

            setAnimation(holder.imageView, position);
        }

        // 필수로 Generate 되어야 하는 메소드 3
        @Override
        public int getItemCount() {
            return mItems.size();
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
                Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }
}