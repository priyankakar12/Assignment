package com.demo.assignmentdemo.Utils

import android.app.Activity
import android.os.Bundle
import androidx.navigation.Navigation
import com.demo.assignmentdemo.R

fun Activity.nextFragment(id: Int, bundle: Bundle? = null) {
    Navigation.findNavController(this, R.id.nav_host_fragment_activity).navigate(id, bundle)
}