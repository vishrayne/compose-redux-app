package com.vishrayne.myfirstreduxapp.model.ui

import com.vishrayne.myfirstreduxapp.model.domain.Filter

data class UiFilter(
    val filter: Filter,
    val isSelected: Boolean,
)