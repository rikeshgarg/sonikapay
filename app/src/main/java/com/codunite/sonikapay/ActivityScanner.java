package com.codunite.sonikapay;

import static com.codunite.sonikapay.BaseApp.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.codunite.commonutility.ShowCustomToast;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ActivityScanner extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    private LinearLayout browseQrcode;
    private ImageView imgScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_scanner);
        svContext = this;
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(result -> runOnUiThread(() -> {
            Intent svIntent = new Intent(svContext, ActivityScanAmount.class);
            svIntent.putExtra("qrdata", result.getText());
            startActivity(svIntent);
            finish();
        }));
        imgScan = (ImageView) findViewById(R.id.image_scan);
        LinearLayout browseQrcode = findViewById(R.id.browse_qrcode);
//        TextView browseQrcode = findViewById(R.id.browse_qrcode);
        browseQrcode.setVisibility(View.VISIBLE);
        browseQrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent svIntent = new Intent(svContext, ActivityBrowseProfileImage.class);
                startActivity(svIntent);
//                mCodeScanner = ActivityBrowseProfileImage.imageUri;
//                mCodeScanner.setImageURI(null);
//                mCodeScanner.setImageURI(ActivityBrowseProfileImage.imageUri);

            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityBrowseProfileImage.imageUri != null) {
            Result result = GetBarcodeText(ActivityBrowseProfileImage.imageUri);
            if (result == null) {
                ShowCustomToast customToast = new ShowCustomToast(svContext);
                customToast.showCustomToast(svContext, "Please scan valid QR", customToast.ToastyError);
            } else {
                Intent svIntent = new Intent(svContext, ActivityScanAmount.class);
                svIntent.putExtra("qrdata", result.getText());
                startActivity(svIntent);
                finish();
            }

            ActivityBrowseProfileImage.imageUri = null;
        }
        mCodeScanner.startPreview();
    }

    private Context svContext;

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();

    }


    protected Result GetBarcodeText(Uri imgUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imgUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Log.e(TAG, "uri is not a bitmap," + imgUri.toString());
                return null;
            }
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bBitmap);
                return result;
            } catch (NotFoundException e) {
                Log.e(TAG, "decode exception", e);
                return null;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "can not open file" + imgUri.toString(), e);
            return null;
        }
    }
}


