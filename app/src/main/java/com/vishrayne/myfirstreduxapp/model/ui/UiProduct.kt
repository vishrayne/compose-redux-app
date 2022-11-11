package com.vishrayne.myfirstreduxapp.model.ui

import com.vishrayne.myfirstreduxapp.model.domain.Product

data class UiProduct(
    val product: Product,
    val isFavorite: Boolean,
)