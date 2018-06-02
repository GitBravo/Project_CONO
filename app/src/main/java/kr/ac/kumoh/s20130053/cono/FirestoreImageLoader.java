package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirestoreImageLoader {
    private Context context;
    private ImageView imageView;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    FirestoreImageLoader(Context context, ImageView imageView){
        this.context = context;
        this.imageView = imageView;

        storage = FirebaseStorage.getInstance(); // FireStorage 인스턴스 획득
        storageReference = storage.getReferenceFromUrl("gs://cono-bf6c7.appspot.com/comment_image/0Iiu5XDotIpacn8haUYO.png"); // 저장된 파일 위치로부터 참조객체 생성
    }

    public void ImageLoad(){
        // 이미지를 로드하여 ImageView 로 출력
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageView);
    }
}
