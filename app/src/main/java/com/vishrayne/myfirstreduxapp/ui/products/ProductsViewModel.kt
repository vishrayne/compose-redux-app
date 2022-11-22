package com.vishrayne.myfirstreduxapp.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishrayne.myfirstreduxapp.model.domain.Filter
import com.vishrayne.myfirstreduxapp.model.ui.UiFilter
import com.vishrayne.myfirstreduxapp.model.ui.UiProduct
import com.vishrayne.myfirstreduxapp.redux.ApplicationState
import com.vishrayne.myfirstreduxapp.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val store: Store<ApplicationState>
) : ViewModel() {
    val productsFlow: StateFlow<ProductsListUiState> = combine(
        store.stateFlow.map { it.products },
        store.stateFlow.map { it.favoriteProductIds },
        store.stateFlow.map { it.productFilterInfo }
    ) { products, favoriteIds, filterInfo ->
        val uiProducts = products.map {
            UiProduct(
                product = it,
                isFavorite = favoriteIds.contains(it.id)
            )
        }

        val uiFilters = filterInfo.filters.map {
            UiFilter(
                filter = it,
                isSelected = filterInfo.selectedFilter?.equals(it) == true
            )
        }.toSet()

        val filteredProducts = filterInfo.selectedFilter
            ?.let {
                uiProducts.filter {
                    it.product.category == filterInfo.selectedFilter.value
                }
            }
            ?: uiProducts

        ProductsListUiState(uiFilters, filteredProducts)
    }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ProductsListUiState(emptySet(), emptyList())
        )

    fun refreshProducts() = viewModelScope.launch {
        val domainProducts = productRepository.fetchAllProducts()
        store.update { state ->
            state.copy(
                products = domainProducts,
                productFilterInfo = ApplicationState.ProductFilterInfo(
                    filters = domainProducts.map {
                        Filter(it.category, it.category)
                    }.toSet(),
                    selectedFilter = state.productFilterInfo.selectedFilter
                )
            )
        }
    }

    fun onFavoriteClick(selectedProductId: Int) = viewModelScope.launch {
        store.update { state ->
            val newFavoriteIds = if (state.favoriteProductIds.contains(selectedProductId))
                state.favoriteProductIds.filter { it != selectedProductId }.toSet()
            else
                state.favoriteProductIds + selectedProductId

            state.copy(favoriteProductIds = newFavoriteIds)
        }
    }

    fun onFilterSelected(filter: Filter) = viewModelScope.launch {
        store.update { state ->
            val newFilter = if (state.productFilterInfo.selectedFilter != filter) {
                filter
            } else {
                null
            }

            state.copy(
                productFilterInfo = ApplicationState.ProductFilterInfo(
                    filters = state.productFilterInfo.filters,
                    selectedFilter = newFilter
                )
            )
        }
    }
}