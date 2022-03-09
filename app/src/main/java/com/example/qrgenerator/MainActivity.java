package com.example.qrgenerator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {
    private Button generate,scan;
    private EditText mytext;
    private ImageView qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generate = findViewById(R.id.generate);
        scan = findViewById(R.id.scan);
        mytext = findViewById(R.id.text);
        qr_code = findViewById(R.id.qrcode);

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                String text = mytext.getText().toString();
                if (text != null && ! text.isEmpty()){
                    try {
                        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_code.setImageBitmap(bitmap);
                    } catch (WriterException e){
                        e.printStackTrace();
                    }

                }


            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                //intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                intentIntegrator.setCameraId(0);
               // intentIntegrator.setOrientationLocked(false);
                intentIntegrator.setPrompt("Scanning");
                intentIntegrator.setBeepEnabled(true);
               // intentIntegrator.setBarcodeImageEnabled(true);
                intentIntegrator.initiateScan();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null && result.getContents()!=null){
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(result.getContents())
                    .setPositiveButton("copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("result",result.getContents());
                            manager.setPrimaryClip(data);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}