package com.triibe.myapplication.network

class Repository constructor( val apiService: ApiService ) {
    fun getFeed() = apiService.getFeed(startDate = "2015-09-07", endDate = "2015-09-08")
}