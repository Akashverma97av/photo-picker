package com.example.photopickerlibrary

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi

private const val MIME_TYPE_IMAGE = "image/*"

/*This contract is used to pick single photo from the gallery*/
class PickSinglePhotoContract : ActivityResultContract<Void?, Uri?>(){
    @RequiresApi(33)
    override fun createIntent(context: Context, input: Void?): Intent {
        /*This function will open new intent*/
        return Intent(if (PhotoPickerAvailabilityChecker.isPhotoPickerAvailable()) {
            Intent(MediaStore.ACTION_PICK_IMAGES)
        } else {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        }).apply { type = MIME_TYPE_IMAGE}
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        /*This function will call when you come back from the intent i.e gallery*/
        return intent.takeIf { resultCode == Activity.RESULT_OK }?.data
    }
}