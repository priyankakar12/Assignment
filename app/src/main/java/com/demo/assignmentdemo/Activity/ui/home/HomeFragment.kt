package com.demo.assignmentdemo.Activity.ui.home

import android.graphics.Color
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


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var PhotosList:ArrayList<PhotosModel> = ArrayList()
    private lateinit var photosAdapter : PhotosAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        getPhotos()

        return root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}