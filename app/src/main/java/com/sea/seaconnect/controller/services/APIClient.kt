package com.sea.seaconnect.controller.services

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.conscrypt.Conscrypt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.Security
import java.util.concurrent.TimeUnit

/**
 * Created by Nishant on 03/07/20.
 */
object APIClient {
    fun retrofit(url: String): APIInterface {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        Security.insertProviderAt(Conscrypt.newProvider(), 1);

        val client = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(true)
            .build()

        val service: APIInterface
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create<APIInterface>(APIInterface::class.java)

        return service
    }
}