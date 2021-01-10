package vanson.dev.instagramclone.Utilites

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraHelper(private val activity: Activity) {
    var mImageUri: Uri? = null
    val REQUEST_CODE = 1
    private val simpleDateFormat: SimpleDateFormat =
        SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.US
        )

    fun createImageFile(): File {
        // Create an image file name
        val storageDir: File = activity.getExternalFilesDir(
            Environment.DIRECTORY_PICTURES
        )!!
        return File.createTempFile(
            "JPEG_${simpleDateFormat.format(Date())}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        )
    }

    fun takeImageFromCamera() {
        val intent =
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {//check camera on device
            val imageFile = createImageFile()
            mImageUri = FileProvider.getUriForFile(
                activity,
                "vanson.dev.instagramclone.fileprovider",
                imageFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri)
            activity.startActivityForResult(intent, REQUEST_CODE)
        }

    }
}