package com.vishrayne.myfirstreduxapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vishrayne.myfirstreduxapp.model.domain.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _productsFlow = MutableStateFlow<List<Product>>(listOf())
    val productsFlow: StateFlow<List<Product>> = _productsFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            listOf()
        )

    fun refreshProducts() = viewModelScope.launch {
        val domainProducts = productRepository.fetchAllProducts()
        _productsFlow.update {
            domainProducts
        }
    }
}