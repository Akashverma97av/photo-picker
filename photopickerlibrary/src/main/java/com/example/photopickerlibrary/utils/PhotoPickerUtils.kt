package com.example.photopickerlibrary.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object PhotoPickerUtils {
    fun getPathAndCreateFile(context: Context, imageUri: Uri): File {
        val path = getFileRealPath(context, imageUri)
        Log.e("Image URI :::::", "$path")
        return File(path)
    }


    private fun getFileRealPath(context: Context, uri: Uri): String? {
        try {
            val returnCursor: Cursor? =
                context.contentResolver.query(uri, null, null, null, null)
            val nameIndex = returnCursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//            val sizeIndex = returnCursor?.getColumnIndex(OpenableColumns.SIZE)
            returnCursor?.moveToFirst()
            val name = nameIndex?.let { returnCursor.getString(it) }
//            val size = returnCursor?.getLong(sizeIndex!!)?.let { java.lang.Long.toString(it) }
            val file = File(context.cacheDir, name)
            try {
                val inputStream: InputStream = context.contentResolver.openInputStream(uri)!!
                val outputStream = FileOutputStream(file)
                var read: Int
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable = inputStream.available()
                //int bufferSize = 1024;
                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream.read(buffers).also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                Log.e("File Size", "Size " + file.length())
                inputStream.close()
                outputStream.close()
                Log.e("File Path", "Path " + file.path)
                Log.e("File Size", "Size " + file.length())
            } catch (e: java.lang.Exception) {
                Log.e("Exception", e.message!!)
            }
            return file.path
        }catch (ex:Exception){
            Log.e("Real Path Util error: ", "${ex.localizedMessage}")
            return null
        }
    }
}