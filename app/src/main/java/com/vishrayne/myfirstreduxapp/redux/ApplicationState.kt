package com.vishrayne.myfirstreduxapp.redux

import com.vishrayne.myfirstreduxapp.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val favoriteProductIds: Set<Int> = emptySet(),
)