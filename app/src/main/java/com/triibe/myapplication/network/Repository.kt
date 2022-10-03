package com.triibe.myapplication.network

class Repository constructor( val apiService: ApiService ) {
    fun getFeed(start: String, end: String) = apiService.getFeed(startDate = start, endDate = end)
}