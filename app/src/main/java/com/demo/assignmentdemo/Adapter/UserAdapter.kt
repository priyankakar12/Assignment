package com.demo.assignmentdemo.Adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.demo.assignmentdemo.Database.UserModelRoomDb
import com.demo.assignmentdemo.R
import com.demo.assignmentdemo.databinding.UserLayoutBinding

class UserAdapter(private val context : Context,
                  private val mList : List<UserModelRoomDb>,
                  private val userInterface: UserInterface): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(private val mbinding:UserLayoutBinding ) : RecyclerView.ViewHolder(mbinding.root)
    {
        fun setData(position: Int)
        {
            mbinding.txtName.text = mList[position].name
            mbinding.txtEmail.text = mList[position].email
            mbinding.txtPhone.text = mList[position].phone
            mbinding.txtAddress.text = mList[position].address
            setImage(mbinding.imageView, mList[position].byteArray!!)

            mbinding.llCard.setOnClickListener {
                userInterface.getUserById(position,mList[position].id!!)
            }

            mbinding.llCard.setOnLongClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(R.string.Title)
                builder.setMessage(R.string.Message)
                builder.setIcon(android.R.drawable.ic_delete)

                builder.setPositiveButton("Yes") { dialogInterface, which ->
                    userInterface.deleteUser(position)
                }
                builder.setNeutralButton("Cancel") { dialogInterface, which ->

                }
                builder.setNegativeButton("No") { dialogInterface, which ->

                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

                true
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.ViewHolder {
        return  ViewHolder(UserLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
       holder.setData(position)
    }

    override fun getItemCount(): Int {
        return  mList.size
    }

    fun setImage(view: ImageView, data: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        view.setImageBitmap(bitmap)
    }

    interface UserInterface{
        fun getUserById(position : Int, id : Int)

        fun deleteUser(position: Int)
    }
}