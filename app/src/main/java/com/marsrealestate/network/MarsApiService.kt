package com.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://mars.udacity.com/"

enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create()) // For String Reading Data from API
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // For Object Data Reading from API
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) // ask Retrofit to produce a
    // coroutine base API's with buit in SUSPEND
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the API's method below
 *  forexample :
 *           1)   BASE_URL = https://mars.udacity.com/ + realestate
 */
interface MarsApiService {
    // API https://mars.udacity.com/realestate
    @GET("realestate")
    suspend fun getProperties(): List<MarsProperty> // Call<String> for ScalarsConverterFactory.create()

}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object MarsApi {
    val retrofitService : MarsApiService by lazy {
        // it tells retrofit to make urls for given BASE_URL
        retrofit.create(MarsApiService::class.java)
    }
}
