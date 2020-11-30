package com.example.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import java.io.File;
import java.io.IOException;


public class EditImageActivity extends Activity {

    private EditImageView mEditImageView;
    private SeekBar mBrightnessBar;
    private SeekBar mContrastBar;
    File outputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        outputImage = new File(getExternalCacheDir(), "output_img.jpg");
        initView();
    }

    private void initView() {
        initEditView();
        initSeekBar();
    }

    private void initSeekBar() {
        mBrightnessBar = (SeekBar) findViewById(R.id.activity_main_brightness_seek_bar);
        mBrightnessBar.setOnSeekBarChangeListener(mOnBrightnessSeekBarChangeListener);

        mContrastBar = (SeekBar) findViewById(R.id.activity_main_contrast_seek_bar);
        mContrastBar.setOnSeekBarChangeListener(mOnContrastSeekBarChangeListener);
    }

    private Bitmap getBitmap() {
        //InputStream flowerStream = getResources().getAssets().open("flower.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(outputImage.getAbsolutePath());


            return bitmap;
    }

    private Bitmap rotateIfRequired(Bitmap bitmap) throws IOException {
        ExifInterface exif = new ExifInterface(outputImage.getPath());
        switch (exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
        )) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmap(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmap(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmap(bitmap, 270);
        }
        return null;
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float) degree);
        Bitmap rotateBitmap =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotateBitmap;
    }


    private void initEditView() {
        mEditImageView = (EditImageView) findViewById(R.id.activity_main_edit_image);
        Bitmap bitmap = getBitmap();
        mEditImageView.setImage(bitmap);
    }

    public void withdrawClick(View view) {
        mEditImageView.withDraw();
        dismissSeekBar();
    }

    public void penClick(View view) {
        mEditImageView.drawLine();
        dismissSeekBar();
    }

    public void rotateClick(View view) {
        mEditImageView.rotate();
        dismissSeekBar();
    }

    public void reverseXClick(View view) {
        mEditImageView.reverseX();
        dismissSeekBar();
    }

    public void reverseYClick(View view) {
        mEditImageView.reverseY();
        dismissSeekBar();
    }

    public void brightnessClick(View view) {
        showBrightnessBar();
        dismissContrastBar();
    }

    public void contrastClick(View view) {
        showContrastBar();
        dismissBrightnessBar();
    }

    private void dismissSeekBar() {
        dismissContrastBar();
        dismissBrightnessBar();
    }

    private void showBrightnessBar() {
        mBrightnessBar.setVisibility(View.VISIBLE);
    }

    private void dismissBrightnessBar() {
        mBrightnessBar.setVisibility(View.GONE);
    }

    private void showContrastBar() {
        mContrastBar.setVisibility(View.VISIBLE);
    }

    private void dismissContrastBar() {
        mContrastBar.setVisibility(View.GONE);
    }

    private SeekBar.OnSeekBarChangeListener mOnBrightnessSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mEditImageView.brightness(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mEditImageView.brightnessDone(seekBar.getProgress());
        }
    };

    private SeekBar.OnSeekBarChangeListener mOnContrastSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mEditImageView.contrast(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mEditImageView.contrastDone(seekBar.getProgress());
        }
    };

}
