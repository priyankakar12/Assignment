package com.demo.assignmentdemo.Activity.ui.adduser

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.assignmentdemo.Database.UserDb
import com.demo.assignmentdemo.Database.UserModelRoomDb
import com.demo.assignmentdemo.ImageCompresion.ImageCompression.Companion.fileUri
import com.demo.assignmentdemo.ImageFragment.ImageFragment
import com.demo.assignmentdemo.R
import com.demo.assignmentdemo.Repository.UserRoomRepository
import com.demo.assignmentdemo.Utils.CoroutineMethods
import com.demo.assignmentdemo.ViewModel.UserViewModel
import com.demo.assignmentdemo.ViewModel.UserViewModelFactory
import com.demo.assignmentdemo.databinding.FragmentAddUserBinding
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class AddUserFragment : Fragment(),View.OnClickListener {
    private lateinit var binding : FragmentAddUserBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var repository : UserRoomRepository
    private lateinit var factory : UserViewModelFactory
    private lateinit var database : UserDb

    private var value : String =""
    private var user : UserModelRoomDb? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAddUserBinding.inflate(inflater,container,false)

        value = requireArguments().getString("id").toString()
        Log.e("VALUE",""+value)

        database = UserDb(requireContext())
        repository = UserRoomRepository(database)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[UserViewModel::class.java]

        binding.btnAddUser.setOnClickListener(this)
        binding.profileImage.setOnClickListener(this)
        profile_image=binding.profileImage
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (value == "")
        {
            binding.btnAddUser.text ="Add User"
        }
        else
        {
            binding.btnAddUser.text = "Update User"
            CoroutineMethods.main {
                viewModel.getSelectUserById(value.toInt()).observe(requireActivity(),{
                    if (it == null) {
                        Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        fileUri = getUri(it.byteArray)
                        binding.edName.setText(it.name)
                        binding.edEmailaddress.setText(it.email)
                        binding.edPhonenumber.setText(it.phone)
                        binding.edAddress.setText(it.address)
                        setImageViewWithByteArray(binding.profileImage, it.byteArray!!)
                    }
                })
            }
        }
    }


    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_add_user->{
                val id = if (user != null) user?.id else null
                var mName: String = binding.edName.text.toString()
                var mPhone: String = binding.edPhonenumber.text.toString()
                var mEmail: String = binding.edEmailaddress.text.toString()
                var mAddress: String = binding.edAddress.text.toString()
                var cancel = false
                var focusView: View? = null
                if(TextUtils.isEmpty(mName))
                {
                    Toast.makeText(requireContext(), "User name cannot be blank", Toast.LENGTH_SHORT).show()
                    focusView = binding.edName
                    cancel = true
                }
                else if (TextUtils.isEmpty(mEmail))
                {
                    Toast.makeText(requireContext(), "User email cannot be blank", Toast.LENGTH_SHORT).show()
                    focusView = binding.edEmailaddress
                    cancel = true
                }
                else if (!isEmailValid(mEmail)){
                    Toast.makeText(requireContext(), "Please insert valid email", Toast.LENGTH_SHORT).show()
                    focusView = binding.edEmailaddress
                    cancel = true
                }
                else if (TextUtils.isEmpty(mPhone))
                {
                    Toast.makeText(requireContext(), "User phone cannot be blank", Toast.LENGTH_SHORT).show()
                    focusView = binding.edPhonenumber
                    cancel = true
                }
                else if (TextUtils.isEmpty(mAddress))
                {
                    Toast.makeText(requireContext(), "User address cannot be blank", Toast.LENGTH_SHORT).show()
                    focusView = binding.edAddress
                    cancel = true
                }
                else if (!isPhoneValid(mPhone))
                {
                    Toast.makeText(requireContext(), "Please insert valid phone number", Toast.LENGTH_SHORT).show()
                    focusView = binding.edPhonenumber
                    cancel = true
                }

                if (cancel) {
                    focusView?.requestFocus()
                }
                else{
                    val iStream: InputStream = fileUri?.let {
                        requireActivity().getContentResolver().openInputStream(it) }!!
                    val inputData: ByteArray = getBytes(iStream)!!


                    val user = UserModelRoomDb(id,mName,mEmail,mPhone,mAddress,inputData)
                    CoroutineMethods.main {
                        viewModel.insertUser(user).also {
                            Toast.makeText(requireContext(),"Successfully save user",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
            R.id.profile_image->{
                val imageFragment: Fragment = ImageFragment()
                val transaction = requireFragmentManager().beginTransaction()
                transaction.replace(R.id.content, imageFragment)
                transaction.commit()
            }


        }
    }
    companion object{
        lateinit var profile_image: ImageView
    }

    private fun isPhoneValid(phone: String): Boolean {
        return phone.length > 9
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    private fun getUri(byteArray: ByteArray?): Uri? {
        val buf = byteArray!!
        val s = String(buf, charset("UTF-8"))
        val uri = Uri.parse(s)

        return uri
    }




    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }


    fun setImageViewWithByteArray(view: ImageView, data: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        view.setImageBitmap(bitmap)
    }
}