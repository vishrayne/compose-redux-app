package com.vishrayne.myfirstreduxapp.redux

import com.vishrayne.myfirstreduxapp.model.domain.Filter
import com.vishrayne.myfirstreduxapp.model.domain.Product

data class ApplicationState(
    val products: List<Product> = emptyList(),
    val productFilterInfo: ProductFilterInfo = ProductFilterInfo(),
    val favoriteProductIds: Set<Int> = emptySet(),
) {
    data class ProductFilterInfo(
        val filters: Set<Filter> = emptySet(),
        val selectedFilter: Filter? = null,
    )
}