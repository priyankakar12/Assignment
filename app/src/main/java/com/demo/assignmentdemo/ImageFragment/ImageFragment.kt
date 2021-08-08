package com.demo.assignmentdemo.ImageFragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.assignmentdemo.Activity.ui.adduser.AddUserFragment.Companion.profile_image
import com.demo.assignmentdemo.ImageCompresion.ImageCompressionListener
import com.demo.assignmentdemo.ImagePicker.ImagePicker
import com.demo.assignmentdemo.R
import com.demo.assignmentdemo.databinding.FragmentImageBinding


class ImageFragment : Fragment() {
    private lateinit var binding:FragmentImageBinding
    private var imagePicker: ImagePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentImageBinding.inflate(inflater,container,false)
        imagePicker = ImagePicker()
        imagePicker!!.withFragment(this).withCompression(true).start()
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            imagePicker!!.addOnCompressListener(object : ImageCompressionListener {
                override fun onStart() {}
                override fun onCompressed(filePath: String?) {
                    val selectedImage = BitmapFactory.decodeFile(filePath)
                    Log.e("FILee11111", "" + selectedImage)
                    profile_image.setImageBitmap(selectedImage)
                }
            })
            val filePath = imagePicker!!.getImageFilePath(data)
            Log.e("FILee", "" + filePath)
            if (filePath != null) {
                val selectedImage = BitmapFactory.decodeFile(filePath)
                profile_image.setImageBitmap(selectedImage)

            }
        }
    }



}