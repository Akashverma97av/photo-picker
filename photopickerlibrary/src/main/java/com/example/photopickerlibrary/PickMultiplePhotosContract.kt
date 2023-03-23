package com.example.photopickerlibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi

private const val MIME_TYPE_IMAGE = "image/*"

/*This contract is used to pick multiple photos from the gallery*/
class PickMultiplePhotosContract(val maxPhotos:Int?) : ActivityResultContract<Unit, List<Uri>>(){
    @RequiresApi(33)
    override fun createIntent(context: Context, input: Unit): Intent {
        /*This function will open new intent*/
        return if (PhotoPickerAvailabilityChecker.isPhotoPickerAvailable()) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
                .putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX,maxPhotos?:10)
        } else {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
                .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }.apply { type = MIME_TYPE_IMAGE }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
        /*This function will call when you come back from the intent i.e gallery*/
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.clipData?.let { clipData ->
            val selectedUris: MutableList<Uri> = mutableListOf()

            for (index in 0 until clipData.itemCount) {
                val uri: Uri = clipData.getItemAt(index).uri
                if (uri != null) {
                    selectedUris.add(uri)
                }
            }

            ArrayList(selectedUris)
        } ?: emptyList()
    }
}
