package com.example.resultapp

import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

interface VideoInterface {
    @Multipart
    @POST("upload/")
    fun uploadVideo(@Part video: MultipartBody.Part?): Call<RequestBody?>?


    companion object {
        const val IPADDRESS = "http://192.168.1.35:8000/"
    }
}