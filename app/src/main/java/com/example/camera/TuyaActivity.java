package com.example.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class TuyaActivity extends AppCompatActivity {

    TuyaView tuyaView;
    File outputImage;
    TextView clear;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuya);
        outputImage = new File(getExternalCacheDir(), "output_img.jpg");
        initView();
    }

    private void initView() {
        tuyaView = findViewById(R.id.tuyaView);
        tuyaView.setImage(getBitmap());
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(v -> tuyaView.clear());
        save = findViewById(R.id.save);
        save.setOnClickListener(v -> getFile(tuyaView.new1_Bitmap));
    }

    private Bitmap getBitmap() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getAbsolutePath(), options);
        return bitmap;
    }

    public File getFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(getExternalCacheDir(), "saved.jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


}