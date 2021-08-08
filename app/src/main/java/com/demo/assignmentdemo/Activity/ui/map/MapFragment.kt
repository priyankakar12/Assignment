package com.demo.assignmentdemo.Activity.ui.map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.demo.assignmentdemo.Activity.IntroductionActivity.Companion.latitude
import com.demo.assignmentdemo.Activity.IntroductionActivity.Companion.longitude
import com.demo.assignmentdemo.R
import com.demo.assignmentdemo.databinding.FragmentHomeBinding
import com.demo.assignmentdemo.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    private var mMap:GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater, container, false)

        val supportMapFragment : SupportMapFragment = childFragmentManager.findFragmentById(R.id.gmap) as SupportMapFragment

        supportMapFragment.getMapAsync { googleMap ->
            mMap = googleMap

            val sydney = LatLng(latitude!!.toDouble(), longitude!!.toDouble())
            mMap!!.addMarker(MarkerOptions().position(sydney).title("You are here"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        }


        return binding.root
    }


}