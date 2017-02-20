package com.example.rishab.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;

public class MainActivity extends AppCompatActivity {

    private Button b_gallery, b_capture;
    private ImageView mImageView;
    private StorageReference storage;
    private static final int GALLERY_INTENT = 2;
    private static final int CAMERA_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storage = FirebaseStorage.getInstance().getReference();

        //b_gallery = (Button) findViewById(R.id.);
        b_capture = (Button) findViewById(R.id.button);
        mImageView = (ImageView) findViewById(R.id.imageView);


        progressDialog = new ProgressDialog(this);

        b_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dispatchTakePictureIntent();
                //startActivityForResult(intent,CAMERA_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            //set the progress dialog
            progressDialog.setMessage("Fetching Results...");
            progressDialog.show();

            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bitOut = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100 , bitOut);
            byte[] dataBAOS = bitOut.toByteArray();

            mImageView.setImageBitmap(bitmap);

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();

            StorageReference imagesRef = storageRef.child("photo" + "123");

            UploadTask uploadTask = imagesRef.putBytes(dataBAOS);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Sending failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri s = taskSnapshot.getDownloadUrl();

                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/searchbyimage?hl=en&site=imghp&image_url="+s.toString())));
                    

                    progressDialog.dismiss();
                }
            });
        }
    }
}


