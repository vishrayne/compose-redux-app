package com.vishrayne.myfirstreduxapp

import com.vishrayne.myfirstreduxapp.hilt.service.ProductsService
import com.vishrayne.myfirstreduxapp.model.domain.Product
import com.vishrayne.myfirstreduxapp.model.mapper.ProductMapper
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productsService: ProductsService,
    private val productMapper: ProductMapper,
) {
    suspend fun fetchAllProducts(): List<Product> {
        return productsService.getAllProducts().body()
            ?.map(productMapper::buildFrom)
            ?: emptyList()
    }
}