package com.example.photopickercomponent

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageSwitcher
import android.widget.ImageView
import androidx.activity.result.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.photopickerlibrary.PickMultiplePhotosContract
import com.example.photopickerlibrary.PickSinglePhotoContract
import com.example.photopickerlibrary.utils.PhotoPickerUtils.getPathAndCreateFile
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnPickSinglePhoto: Button = findViewById(R.id.btn_pick_single)
        val btnPickMultiplePhoto: Button = findViewById(R.id.btn_pick_multiple)
        val imageSwitcher: ImageSwitcher = findViewById(R.id.is_photo)
        val btnPrevious: Button = findViewById(R.id.btn_previous)
        val btnNext: Button = findViewById(R.id.btn_next)
        var index = 0

        imageSwitcher.setFactory {
            val imageView = ImageView(applicationContext)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView
        }


        val singlePhotoPickerLauncher =
            registerForActivityResult(PickSinglePhotoContract()) { imageUri: Uri? ->
                Log.e("Image URI::::::", imageUri.toString())
                btnPrevious.visibility = View.GONE
                btnNext.visibility = View.GONE
                if (imageUri != null) {
                    val file = getPathAndCreateFile(this,imageUri)
                    imageSwitcher.setImageURI(file.absolutePath.toUri())
                }
                else{
                    Log.e("Image URI null::::::", "")
                }
            }


        val multiplePhotoPickerLauncher =
            registerForActivityResult(PickMultiplePhotosContract(5)) { imageUris: List<Uri> ->
                if (imageUris.isNotEmpty()){
                    Log.e("Image URI::::::", imageUris.toString())
                    if (imageUris.size == 1){
                        btnNext.visibility = View.GONE
                        btnPrevious.visibility = View.GONE
                    }else{
                        btnNext.visibility = View.VISIBLE
                        btnPrevious.visibility = View.VISIBLE
                    }

                    val fileArray = arrayListOf<File>()
                    for (uri in imageUris){
                        fileArray.add(getPathAndCreateFile(this,uri))
                    }
                    imageSwitcher.setImageURI(fileArray[index].absolutePath.toUri())
                    /*previous button functionality*/
                    btnPrevious.setOnClickListener {
                        if (index!=0){
                            index = if (index - 1 >= 0) index - 1 else imageUris.size.also { index = it.minus(1) }
                        }else index = imageUris.size.minus(1)

                        imageSwitcher.setImageURI(fileArray[index].absolutePath.toUri())
                    }
                    /* next button functionality*/
                    btnNext.setOnClickListener {
                        index = if (index + 1 < imageUris.size) index + 1 else 0
                        imageSwitcher.setImageURI(fileArray[index].absolutePath.toUri())
                    }
                }

            }


        btnPickSinglePhoto.setOnClickListener {
            singlePhotoPickerLauncher.launch()
        }
        btnPickMultiplePhoto.setOnClickListener {
            multiplePhotoPickerLauncher.launch()
        }
    }



}

