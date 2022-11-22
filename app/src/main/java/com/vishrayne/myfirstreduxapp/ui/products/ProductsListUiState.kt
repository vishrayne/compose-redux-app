package com.vishrayne.myfirstreduxapp.ui.products

import com.vishrayne.myfirstreduxapp.model.ui.UiFilter
import com.vishrayne.myfirstreduxapp.model.ui.UiProduct

data class ProductsListUiState(
    val filters: Set<UiFilter>,
    val products: List<UiProduct>
)
