package com.example.lovedeepsingh.imagesearch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    Button button1;
    ImageView Imagetaken;
    private static final int CAM_REQUEST = 1311;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button)findViewById(R.id.btnpicture);
        Imagetaken=(ImageView)findViewById(R.id.imageview);
       button1.setOnClickListener(new button1Clicker());

    }

      protected void onActivityResult(int requestcode, int resultcode,Intent data)
  {
         super.onActivityResult(requestcode,resultcode,data);
      if(requestcode==CAM_REQUEST)
      {
          Bitmap thumbnail = (Bitmap)data.getExtras().get("data");
          Imagetaken.setImageBitmap(thumbnail);

      }
  }
    class button1Clicker implements View.OnClickListener {
        public void onClick(View v)
        {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAM_REQUEST);
        }
    }
}
