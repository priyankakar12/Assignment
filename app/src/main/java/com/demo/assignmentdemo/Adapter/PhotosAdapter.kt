package com.demo.assignmentdemo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.assignmentdemo.Models.PhotosModel
import com.demo.assignmentdemo.databinding.PhotoslayoutBinding
import com.squareup.picasso.Picasso

class PhotosAdapter(private val context: Context,private val mList :List<PhotosModel>):RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

    inner class ViewHolder(private val mbinding:PhotoslayoutBinding):RecyclerView.ViewHolder(mbinding.root)
    {
        fun setData(position: Int){
        Picasso.with(context).load(mList[position].url+".jpg").into(mbinding.imageView)
            mbinding.txtName.text=mList[position].title
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapter.ViewHolder {
        return ViewHolder(PhotoslayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PhotosAdapter.ViewHolder, position: Int) {
        holder.setData(position)
    }

    override fun getItemCount(): Int {
        return  mList.size
    }

}