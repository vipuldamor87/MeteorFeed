package com.triibe.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.triibe.myapplication.databinding.ActivityMainBinding
import com.triibe.myapplication.network.ApiService
import com.triibe.myapplication.network.Repository

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    private lateinit var  viewModel :MainViewModel
    private val apiService = ApiService.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
//        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MyViewModelFactory(Repository(apiService))).get(
            MainViewModel::class.java)
        viewModel.getFeed()
    }
}