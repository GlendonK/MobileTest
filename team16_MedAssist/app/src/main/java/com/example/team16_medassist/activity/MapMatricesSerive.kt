package com.example.team16_medassist.activity


import com.example.team16_medassist.model.MapMatricesModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * moshi for working with json
 */
private val moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

/**
 * base url for the api
 */
private const val BASE_URL =
    "https://maps.googleapis.com/"

/**
 * use retrofit to make web api request
 */
val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object MapAPI {
    val retrofitService: MapMatricesService by lazy {
        retrofit.create(MapMatricesService::class.java)
    }
}

/**
 * interface for api call to google distance matrix api.
 * GET method.
 */
interface MapMatricesService {
    /**
     * example uri: maps/api/distancematrix/json?units=imperial&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592&key=AIzaSyA9G3NuKvhxjDwWAtIohG1UUhFGp0nIQKw
     */
    @GET("maps/api/distancematrix/json")
    fun getMapMatrices(@Query("units", encoded = true)units: String,   @Query("origins", encoded = true) origins: String, @Query("destinations", encoded = true) destinations: String, @Query("key", encoded = true) key: String ): Call<MapMatricesModel>
}