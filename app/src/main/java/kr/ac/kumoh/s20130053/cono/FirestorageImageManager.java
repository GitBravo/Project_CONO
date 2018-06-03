package kr.ac.kumoh.s20130053.cono;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirestorageImageManager {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private StorageReference targetRef;
    private UploadTask uploadTask;

    public FirestorageImageManager() {
        // Firestorage 객체 할당
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public void imageUpload(String comment_id, Uri localFileURI){
        targetRef = storageRef.child("comment_image/"+comment_id); // 서버에 저장될 위치 + 파일명(게시물ID)
        uploadTask = targetRef.putFile(localFileURI); // 로컬에 있는 파일을 서버로 업로드
    }

    public void imageDownload(Context context, ImageView imageView, String comment_id){
        storageRef = storage.getReferenceFromUrl("gs://cono-bf6c7.appspot.com/comment_image/" + comment_id); // 저장된 파일 위치로부터 참조객체 생성
        Glide.with(context /* context */)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .into(imageView);
        imageView.setVisibility(View.VISIBLE);
    }

    public void imageDelete(String comment_id){
        StorageReference desertRef = storageRef.child("comment_image/" + comment_id);
        desertRef.delete();
    }
}
