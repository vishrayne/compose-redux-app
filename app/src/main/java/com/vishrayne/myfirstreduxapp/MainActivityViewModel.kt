package com.vishrayne.myfirstreduxapp

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> =
        snapshotFlow { MainActivityUiState.Success("USER") }
            .onStart { delay(2000) }
            .stateIn(
                scope = viewModelScope,
                SharingStarted.WhileSubscribed(2_000),
                MainActivityUiState.Loading
            )
}

sealed interface MainActivityUiState {
    object Loading : MainActivityUiState
    data class Success(val userData: String) : MainActivityUiState
}