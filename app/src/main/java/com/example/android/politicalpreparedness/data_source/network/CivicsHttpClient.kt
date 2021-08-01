package com.example.android.politicalpreparedness.data_source.network

import com.example.android.politicalpreparedness.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class CivicsHttpClient : OkHttpClient() {

    companion object {
        const val API_KEY =
            "AIzaSyB1C8x_33xjEi5WUh98iHGOWE_cEbgZbM8" //TODO: Place your API Key Here //Adding for review purposes

        fun getClient(): OkHttpClient {
            val builder : Builder = Builder()
                .addInterceptor { chain ->
                    val original = chain.request()
                    val url = original
                        .url
                        .newBuilder()
                        .addQueryParameter("key", API_KEY)
                        .build()
                    val request = original
                        .newBuilder()
                        .url(url)
                        .build()
                    chain.proceed(request)
                }

            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(interceptor)
            }

            return builder.build()
        }
    }
}