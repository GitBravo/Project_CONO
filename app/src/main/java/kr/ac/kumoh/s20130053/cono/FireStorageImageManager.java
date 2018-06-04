package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FireStorageImageManager {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference targetRef;
    private UploadTask uploadTask;

    public FireStorageImageManager() {
        // Firestorage 객체 할당
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void imageUpload(String ServerPath, String fileName, Uri localFileURI) {
        targetRef = storageRef.child(ServerPath + fileName); // 서버 경로 + 파일명
        uploadTask = targetRef.putFile(localFileURI); // 로컬에 있는 파일을 서버로 업로드 진행
    }

    public void imageDownload(Context context, final ImageView imageView, String ServerPath, String image_id) {
        // 저장된 파일 위치로부터 참조객체 생성
        storageRef = storage.getReferenceFromUrl("gs://cono-bf6c7.appspot.com/" + ServerPath + image_id);

        // Glide API 를 사용하여 복잡한 과정 없이 이미지뷰로 데이터 다운로드
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .listener(new RequestListener<StorageReference, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, StorageReference model, Target<GlideDrawable> target, boolean isFirstResource) {
                        // 예외사항 처리
                        Log.d("FireStorageImageManager", "현재 메소드의 매개변수 'image_id' 해당하는 파일이 서버에 존재하지 않습니다.");
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, StorageReference model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // 이미지 로드 완료됬을 때 처리
                        imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .placeholder(R.drawable.default_designer_image) // 이미지 로드되기 전에 임시로 보여줄 이미지
                .error(R.drawable.default_designer_image) // 이미지 로드가 실패했을 때 보여질 이미지
                .into(imageView);
    }

    public void imageDelete(String ServerPath, String image_id) {
        StorageReference desertRef = storageRef.child(ServerPath + image_id);
        desertRef.delete();
    }
}
