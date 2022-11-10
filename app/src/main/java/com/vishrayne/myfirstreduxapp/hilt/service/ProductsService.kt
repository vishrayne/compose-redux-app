package com.vishrayne.myfirstreduxapp.hilt.service

import com.vishrayne.myfirstreduxapp.model.network.NetworkProduct
import retrofit2.Response
import retrofit2.http.GET

interface ProductsService {
    @GET("products")
    suspend fun getAllProducts(): Response<List<NetworkProduct>>
}