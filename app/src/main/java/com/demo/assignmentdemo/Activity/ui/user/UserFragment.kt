package com.demo.assignmentdemo.Activity.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.demo.assignmentdemo.Adapter.UserAdapter
import com.demo.assignmentdemo.Database.UserDb
import com.demo.assignmentdemo.Database.UserModelRoomDb
import com.demo.assignmentdemo.R
import com.demo.assignmentdemo.Repository.UserRoomRepository
import com.demo.assignmentdemo.Utils.CoroutineMethods
import com.demo.assignmentdemo.Utils.nextFragment
import com.demo.assignmentdemo.ViewModel.UserViewModel
import com.demo.assignmentdemo.ViewModel.UserViewModelFactory
import com.demo.assignmentdemo.databinding.FragmentUserBinding


class UserFragment : Fragment(),View.OnClickListener,UserAdapter.UserInterface {
    private lateinit var binding : FragmentUserBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var repository : UserRoomRepository
    private lateinit var factory : UserViewModelFactory
    private lateinit var database : UserDb

    private lateinit var userAdapter:UserAdapter
    private var modelList : ArrayList<UserModelRoomDb> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserBinding.inflate(inflater,container,false)
        database = UserDb(requireContext())
        repository = UserRoomRepository(database)
        factory = UserViewModelFactory(repository)
        viewModel = ViewModelProvider(this,factory)[UserViewModel::class.java]
        binding.addUser.setOnClickListener(this)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getUserList()
    }

    private fun getUserList() {
        CoroutineMethods.main {
            viewModel.getAllUser().observe(requireActivity(), {
                if (it == null) {
                    Toast.makeText(requireContext(), "Sorry no date found", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    modelList = it as ArrayList<UserModelRoomDb>
                    userAdapter = UserAdapter(requireContext(),modelList,this)
                    binding.recRecentusers.adapter = userAdapter
                    binding.recRecentusers.itemAnimator = DefaultItemAnimator()
                    binding.recRecentusers.layoutManager = GridLayoutManager(requireContext(), 1)
                }
            })
        }
    }



    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.add_user->{
                val args = Bundle()
                args.putString("id", "")
                arguments = args
                requireActivity().nextFragment(R.id.navigation_add_user,args)
            }
        }
    }

    override fun getUserById(position: Int, id: Int) {
        val args = Bundle()
        args.putString("id", id.toString())
        arguments = args
        requireActivity().nextFragment(R.id.navigation_add_user,args)
    }

    override fun deleteUser(position: Int) {
        CoroutineMethods.main {
            viewModel.deleteUser(modelList[position]).also {
                Toast.makeText(requireContext(),"Delete User from database",Toast.LENGTH_SHORT).show()
            }
        }
    }

}