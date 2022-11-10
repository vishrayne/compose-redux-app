package com.vishrayne.myfirstreduxapp.model.mapper

import com.vishrayne.myfirstreduxapp.model.domain.Product
import com.vishrayne.myfirstreduxapp.model.network.NetworkProduct
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ProductMapper @Inject constructor() {
    fun buildFrom(networkProduct: NetworkProduct): Product {
        return Product(
            name = networkProduct.title,
            category = networkProduct.category,
            id = networkProduct.id,
            image = networkProduct.image,
            price = BigDecimal(networkProduct.price).setScale(2, RoundingMode.HALF_UP)
        )
    }
}