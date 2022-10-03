package com.triibe.myapplication

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.triibe.myapplication.model.FeedModel
import com.triibe.myapplication.network.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel constructor( private  val repository: Repository) :ViewModel(){

    val newsList = MutableLiveData<FeedModel>()
    val errorMessage = MutableLiveData<String>()

    fun getFeed(start: String, end: String) {
        val response = repository.getFeed(start,end)
        response.enqueue(object : Callback<FeedModel> {
            override fun onResponse(call: Call<FeedModel>, response: Response<FeedModel>) {
                Log.e("Response",response.body().toString())
                newsList.postValue(response.body())
            }
            override fun onFailure(call: Call<FeedModel>, t: Throwable) {
                Log.e("erro1r",t.message.toString())
                errorMessage.postValue(t.message)
            }
        })
    }
}