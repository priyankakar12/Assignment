package com.demo.assignmentdemo.ImagePicker

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.demo.assignmentdemo.ImageCompresion.ImageCompression
import com.demo.assignmentdemo.ImageCompresion.ImageCompressionListener
import java.io.*


class ImagePicker {
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    private var isCompress = true
    private var isCamera = true
    private var isGallery = true
    private var imageCompressionListener: ImageCompressionListener? = null
    fun withActivity(activity: Activity?): ImagePicker {
        this.activity = activity
        return this
    }

    fun withFragment(fragment: Fragment?): ImagePicker {
        this.fragment = fragment
        return this
    }

    fun chooseFromCamera(isCamera: Boolean): ImagePicker {
        this.isCamera = isCamera
        return this
    }

    fun chooseFromGallery(isGallery: Boolean): ImagePicker {
        this.isGallery = isGallery
        return this
    }

    fun withCompression(isCompress: Boolean): ImagePicker {
        this.isCompress = isCompress
        return this
    }

    fun start() {
        check(!(activity != null && fragment != null)) { "Cannot add both activity and fragment" }
        check(!(activity == null && fragment == null)) { "Activity and fragment both are null" }
        check(checkPermission()) { "Write External Permission not found" }
        check(!(!isCamera && !isGallery)) { "select source to pick image" }
        if (activity != null) activity!!.startActivityForResult(pickImageChooserIntent, SELECT_IMAGE) else fragment!!.startActivityForResult(pickImageChooserIntent, SELECT_IMAGE)
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun checkPermission(): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(if (activity != null) activity!! else fragment!!.requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }// collect all gallery intents

    // Create a chooser from the main intent

    // Add all other intents
// collect all camera intents
    // Determine Uri of camera image to save.
    private val pickImageChooserIntent: Intent
        private get() {

            // Determine Uri of camera image to save.
            val outputFileUri = captureImageOutputUri
            val allIntents: MutableList<Intent> = ArrayList()
            val packageManager = if (activity != null) activity!!.packageManager else fragment!!.requireActivity().packageManager
            if (isCamera) {
                // collect all camera intents
                val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                captureIntent.action = MediaStore.ACTION_IMAGE_CAPTURE
                val listCam = packageManager.queryIntentActivities(captureIntent, 0)
                for (res in listCam) {
                    val intent = Intent(captureIntent)
                    intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                    intent.setPackage(res.activityInfo.packageName)
                    if (outputFileUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                    }
                    allIntents.add(intent)
                }
            }
            if (isGallery) {
                // collect all gallery intents
                val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
                galleryIntent.type = "image/*"
                val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
                for (res in listGallery) {
                    val intent = Intent(galleryIntent)
                    intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                    intent.setPackage(res.activityInfo.packageName)
                    allIntents.add(intent)
                }
            }
            var mainIntent: Intent? = allIntents[allIntents.size - 1]
            for (intent in allIntents) {
                if (intent.component!!.className == "com.android.documentsui.DocumentsActivity") {
                    mainIntent = intent
                    break
                }
            }
            allIntents.remove(mainIntent)

            // Create a chooser from the main intent
            val chooserIntent = Intent.createChooser(mainIntent, "Select source")

            // Add all other intents
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
            return chooserIntent
        }

    //outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
    private val captureImageOutputUri: Uri?
        private get() {
            var outputFileUri: Uri? = null
            val getImage = if (activity != null) activity!!.getExternalFilesDir("") else fragment!!.requireActivity().getExternalFilesDir("")
            if (getImage != null) {
                //outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
                outputFileUri = FileProvider.getUriForFile(if (activity != null) activity!! else fragment!!.requireActivity(),
                    if (activity != null) activity!!.applicationContext.packageName + ".provider" else fragment!!.requireActivity().applicationContext.packageName + ".provider",
                    File(getImage.path, "profile.png")
                )
            }
            return outputFileUri
        }

    private fun getPickImageResultFilePath(data: Intent?): String? {
        val isCamera = data == null || data.data == null
        //Log.e("data", +"");
        /*if (data != null) {
            isCamera = false;
            String action = data.getAction();
            isCamera = action != null && action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        } else {
            isCamera = true;
        }*/

        //Log.e("isCamera", isCamera ? "true" : "false");
        return if (isCamera) captureImageOutputUri!!.path else getRealPathFromURI(data!!.data)
        //return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private fun getRealPathFromURI(contentUri: Uri?): String? {
        /*String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = activity != null ?
                activity.getContentResolver().query(contentUri, proj, null, null, null) : fragment.getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);*/
        val out: OutputStream
        val file = File(filename)
        Log.e("File", "" + file)
        try {
            if (file.createNewFile()) {
                val iStream = if (activity != null) activity!!.contentResolver.openInputStream(contentUri!!) else fragment!!.requireContext().contentResolver.openInputStream(contentUri!!)
                val inputData = getBytes(iStream)
                out = FileOutputStream(file)
                out.write(inputData)
                out.close()
                return file.absolutePath
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(IOException::class)
    private fun getBytes(inputStream: InputStream?): ByteArray {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream!!.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun getImageFilePath(data: Intent?): String? {
        if (!isCompress) return getPickImageResultFilePath(data) else if (getPickImageResultFilePath(data) != null) {
            ImageCompression(activity ?: fragment!!.requireActivity(), getPickImageResultFilePath(data)!!, imageCompressionListener!!).execute()
        }
        return null
    }

    fun addOnCompressListener(imageCompressionListener: ImageCompressionListener?) {
        this.imageCompressionListener = imageCompressionListener
    }
    //File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Compressed");

    // Create the storage directory if it does not exist
    private val filename: String
        private get() {
            val context = if (activity != null) activity!! else fragment!!.requireContext()
            val mediaStorageDir = File(context.getExternalFilesDir(""), "uncompressed")

            //File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + "/Compressed");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs()
            }
            val mImageName = "IMG_" + System.currentTimeMillis().toString() + ".png"
            Log.e("return", "" + mediaStorageDir.absolutePath + "/" + mImageName)
            return mediaStorageDir.absolutePath + "/" + mImageName
        }

    companion object {
        const val SELECT_IMAGE = 121
    }
}