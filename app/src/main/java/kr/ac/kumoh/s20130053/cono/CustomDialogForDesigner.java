package kr.ac.kumoh.s20130053.cono;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomDialogForDesigner extends Dialog {
    private static final int LAYOUT = R.layout.customdialog_for_designer; // 이부분 수정요망

    // 이 클래스는 아직 아무것도 수정 안했으니 다시 개발할 것
    private Context mContext;
    private TextView mName;
    private TextView mInfo;
    private ArrayList mList;
    private int mPosition;


    public CustomDialogForDesigner(@NonNull Context context, ArrayList array, int position) {
        super(context);
        this.mContext = context;
        this.mList = array;
        this.mPosition = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mInfo = findViewById(R.id.dialog_designer_info);
        mName = findViewById(R.id.dialog_designer_name);

        ClipData.Item item = (ClipData.Item) mList.get(mPosition);
        mInfo.setText(item.getHtmlText());
        mName.setText(item.getText());
    }
}
