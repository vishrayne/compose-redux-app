package com.vishrayne.myfirstreduxapp.model.domain

import java.math.BigDecimal

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val image: String,
    val price: BigDecimal,
)