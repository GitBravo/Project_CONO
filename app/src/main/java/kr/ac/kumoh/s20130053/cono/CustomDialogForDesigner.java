package kr.ac.kumoh.s20130053.cono;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomDialogForDesigner extends Dialog {
    private static final int LAYOUT = R.layout.customdialog_for_designer; // 이부분 수정요망

    private Context mContext;
    private TextView mName;
    private TextView mInfo;
    private ImageView mImageView;
    private ArrayList<String[]> mArray;
    private int mPosition;


    public CustomDialogForDesigner(@NonNull Context context, ArrayList<String[]> array, int position) {
        super(context);
        this.mContext = context;
        this.mArray = array;
        this.mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mName = findViewById(R.id.dialog_designer_name);
        mInfo = findViewById(R.id.dialog_designer_info);
        mImageView = findViewById(R.id.cardview_image);

        mName.setText(mArray.get(mPosition)[0]); // 이름
        mInfo.setText(mArray.get(mPosition)[1]); // 정보
        new FireStorageImageManager().imageDownload(mContext, mImageView, "designer_image/", mArray.get(mPosition)[2]);
    }
}
