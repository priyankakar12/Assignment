package com.demo.assignmentdemo.Adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.demo.assignmentdemo.Models.BannerModel
import com.demo.assignmentdemo.R
import com.makeramen.roundedimageview.RoundedImageView


class ViewpagerAdapter(private val context:Context,private val viewPager2: ViewPager2,private val imageList : ArrayList<BannerModel>) :
    RecyclerView.Adapter<ViewpagerAdapter.ViewHolder>() {
    private val images = arrayOf<Int>(
        R.drawable.androidone,
        R.drawable.androidtwo,
        R.drawable.androidthree,
        R.drawable.androidfour
    )

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        fun setImage(bannerModel: BannerModel) {
          imageView.setImageResource(bannerModel.images)
        }

        var imageView = view.findViewById<RoundedImageView>(R.id.imageView)
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewpagerAdapter.ViewHolder {
       return ViewHolder(LayoutInflater.from(context).inflate(R.layout.custom_layout,parent,false))
    }

    override fun onBindViewHolder(holder: ViewpagerAdapter.ViewHolder, position: Int) {
       holder.setImage(imageList[position])
        if(position == imageList.size - 2) {
            viewPager2.post(runnable);
        }
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}

