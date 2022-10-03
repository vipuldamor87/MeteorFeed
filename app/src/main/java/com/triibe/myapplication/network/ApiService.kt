package com.triibe.myapplication.network

import com.triibe.myapplication.model.FeedModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import  retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
//    @GET("top-headlines?sources=google-news&apiKey=30470afd6fdf44d2b6b220a524d09f76")
//    fun getNews(): Call<NewsData>
    @GET("rest/v1/feed")
    fun getFeed(
    @Query("start_date") startDate:String,
    @Query("end_date") endDate:String,
    @Query("api_key") key:String = "ca8klb2SwWuyXf4MaXa87Wxh44igCNhRFcdI6NTv"
): Call<FeedModel>


    companion object {
        var apiService: ApiService? = null

        fun getInstance() : ApiService {
            if (apiService == null) {
                val retrofit =  Retrofit.Builder()
                    .baseUrl("https://api.nasa.gov/neo/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                apiService = retrofit.create(ApiService::class.java)
            }
            return apiService!!
        }
    }


}