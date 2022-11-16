package com.vishrayne.myfirstreduxapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishrayne.myfirstreduxapp.model.ui.UiProduct
import com.vishrayne.myfirstreduxapp.redux.ApplicationState
import com.vishrayne.myfirstreduxapp.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val store: Store<ApplicationState>
) : ViewModel() {
    val productsFlow: StateFlow<List<UiProduct>> = combine(
        store.stateFlow.map { it.products },
        store.stateFlow.map { it.favoriteProductIds }
    ) { products, favoriteIds ->
        products.map {
            UiProduct(
                product = it,
                isFavorite = favoriteIds.contains(it.id)
            )
        }
    }
        .distinctUntilChanged()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    fun refreshProducts() = viewModelScope.launch {
        val domainProducts = productRepository.fetchAllProducts()
        store.update { state ->
            state.copy(products = domainProducts)
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
}