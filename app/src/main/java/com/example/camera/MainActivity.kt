package com.example.camera

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {

    private val takePhoto = 1
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    private val EDIT_PHOTO_CODE = 10000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            createImageFile()
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(this, "com.example.camera.fileProvider", outputImage)
            } else {
                Uri.fromFile(outputImage)
            }
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, takePhoto)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            takePhoto -> {
                if (resultCode == RESULT_OK) {
//                    val bitmap = BitmapFactory.decodeFile(outputImage.absolutePath)
//                    imageView.setImageBitmap(rotateIfRequired(bitmap))
//                    val byteArray: ByteArray
//                    byteArray = bitmapToBytes(bitmap)
                    val intent = Intent(this, PhotoEditorActivity::class.java)
//                    intent.putExtra("bitmap", byteArray)
                    startActivityForResult(intent, EDIT_PHOTO_CODE)
                }
            }
            EDIT_PHOTO_CODE -> {
                if (resultCode == RESULT_OK) {
                    val savedImg = File(externalCacheDir, "saved_image.jpg")
                    val bitmap = BitmapFactory.decodeFile(savedImg.absolutePath)
                    imageView.setImageBitmap(rotateIfRequired(bitmap))
                }
            }
        }
    }

    //生成一个文件
    private fun createImageFile() {
        outputImage = File(externalCacheDir, "output_img.jpg")
        println(outputImage.absolutePath.toString())
        if (outputImage.exists()) {
            outputImage.delete()
        }
        outputImage.createNewFile()
    }

    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        return when (exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun compressBitmap(bitmap: Bitmap, sizeLimit: Long): Bitmap? {
        val baos = ByteArrayOutputStream()
        var quality = 100
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().size / 1024 > sizeLimit) {
            // 清空baos
            baos.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            quality -= 10
        }
        return BitmapFactory.decodeStream(ByteArrayInputStream(baos.toByteArray()), null, null)
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        matrix.setScale(0.5f, 0.5f)
        val rotateBitmap =
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        bitmap.recycle()
        return rotateBitmap
    }

    private fun bitmapToBytes(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }


}