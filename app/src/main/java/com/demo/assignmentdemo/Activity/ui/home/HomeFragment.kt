package com.demo.assignmentdemo.Activity.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.assignmentdemo.Adapter.PhotosAdapter
import com.demo.assignmentdemo.Models.PhotosModel
import com.demo.assignmentdemo.ViewModel.PhotosViewModel
import com.demo.assignmentdemo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding

    var PhotosList:ArrayList<PhotosModel> = ArrayList()
    private lateinit var photosAdapter : PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = FragmentHomeBinding.inflate(inflater, container, false)


        getPhotos()

        return binding.root
    }

    private fun getPhotos() {
        val viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        viewModel.getPhotoList().observe(requireActivity(),Observer{
            PhotosList= it as ArrayList<PhotosModel>
            photosAdapter = PhotosAdapter(requireContext(),PhotosList)
            binding.recPhotos.adapter = photosAdapter
            binding.recPhotos.itemAnimator = DefaultItemAnimator()
            binding.recPhotos.layoutManager = GridLayoutManager(requireContext(), 2)
        })
        viewModel.initializeCall()

    }

}