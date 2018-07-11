package com.visa.say2pay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.visa.say2pay.util.ImageHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AWS extends Activity {

    Uri file = null;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aws);
        String amount = getIntent().getStringExtra("AMOUNT");
        Log.d("AWS", amount);
        TextView amount_text = (TextView)findViewById(R.id.amount);
        amount_text.setText(amount + " SGD");

        Button pay_button = (Button)findViewById(R.id.pay);
        pay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = ImageHelper.rotateImage(imageBitmap, 90);
            ImageView mImageView = (ImageView)findViewById(R.id.imageView);
            mImageView.setImageBitmap(imageBitmap);

            OkHttpHandler okHttpHandler= new OkHttpHandler();
            okHttpHandler.execute("https://7888aac3.ngrok.io/api/healthcheck");

            Log.d("Multipart", "Send Image");

            Context context = this.getBaseContext();
            MultipartRequest multipartRequest;
            multipartRequest = new MultipartRequest(context);

            multipartRequest.addFile("image",imageBitmap,"image");
            multipartRequest.execute("https://7888aac3.ngrok.io/api/face-scanning");

        }
    }
}
