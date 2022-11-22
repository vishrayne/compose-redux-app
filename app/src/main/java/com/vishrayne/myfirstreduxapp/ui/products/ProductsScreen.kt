package com.vishrayne.myfirstreduxapp.ui.products

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.vishrayne.myfirstreduxapp.model.domain.Filter
import com.vishrayne.myfirstreduxapp.model.domain.Product
import com.vishrayne.myfirstreduxapp.model.ui.UiFilter
import com.vishrayne.myfirstreduxapp.ui.theme.MyfirstreduxappTheme
import kotlinx.coroutines.launch
import java.math.BigDecimal

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ProductsScreen(productsViewModel: ProductsViewModel) {
    val uiState by productsViewModel.productsFlow.collectAsStateWithLifecycle()

    StoreItems(
        uiState = uiState,
        onAddToCart = { id ->
            Log.d("StoreItem", "OnAddToCart for $id clicked!")
        },
        onFavoriteClick = { id ->
            productsViewModel.onFavoriteClick(id)
        },
        onFilterSelected = {
            productsViewModel.onFilterSelected(it)
        }
    )

    LaunchedEffect(true) {
        productsViewModel.refreshProducts()
    }
}

@Composable
fun StoreItems(
    uiState: ProductsListUiState,
    onAddToCart: (productId: Int) -> Unit,
    onFavoriteClick: (productId: Int) -> Unit,
    onFilterSelected: (filter: Filter) -> Unit,
) {
    val listState = rememberLazyListState()
    // Remember a CoroutineScope to be able to launch
    val coroutineScope = rememberCoroutineScope()

    Column {
        LazyRow() {
            items(
                uiState.filters.toList(),
                key = { uiFilter -> uiFilter.filter.value }
            ) { uiFilter ->
                CategoryChip(
                    uiFilter = uiFilter,
                    onFilterSelected = {
                        onFilterSelected(it)
                        coroutineScope.launch { listState.animateScrollToItem(0) }
                    },
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp),
                )
            }
        }

        LazyColumn(state = listState) {
            items(
                uiState.products,
                key = { uiProduct -> uiProduct.product.id }
            ) { uiProduct ->
                StoreItemRow(
                    uiProduct.product,
                    onAddToCart = onAddToCart,
                    uiProduct.isFavorite,
                    onFavoriteClick = onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun StoreItemRow(
    product: Product,
    onAddToCart: (productId: Int) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: (productId: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .height(150.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .width(120.dp)
                    .padding(4.dp)
                    .wrapContentSize()
            ) {
                Box {
                    SubcomposeAsyncImage(
                        model = product.image,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.size(32.dp)
                            )
                        },
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(4.dp))
                    )

                    IconToggleButton(
                        checked = isFavorite,
                        onCheckedChange = {
                            onFavoriteClick(product.id)
                        },
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.TopEnd)
                    ) {
                        val icon = if (isFavorite)
                            Icons.Rounded.Favorite
                        else
                            Icons.Outlined.FavoriteBorder

                        Icon(
                            icon,
                            contentDescription = "",
                            modifier = Modifier
                                .background(Color.LightGray, CircleShape)
                                .padding(6.dp)
                                .size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.size(4.dp))

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp),
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                    Text(text = product.category, style = MaterialTheme.typography.titleSmall)
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = product.price.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .weight(0.5f)
                    )
                    Button(
                        onClick = { onAddToCart(product.id) },
                        modifier = Modifier
                            .weight(0.5f)
                    ) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "add to cart",
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryChip(
    uiFilter: UiFilter,
    onFilterSelected: (Filter) -> Unit,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = if (uiFilter.isSelected) {
        Pair(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary)
    } else {
        Pair(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
    }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        modifier = modifier.clickable { onFilterSelected(uiFilter.filter) }
    ) {
        Text(
            uiFilter.filter.displayText,
            color = textColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryChipSelectedPreview() {
    val uiFilter = UiFilter(
        Filter("Selected Category", "Selected Category"),
        isSelected = true
    )


    MyfirstreduxappTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            CategoryChip(
                uiFilter = uiFilter,
                onFilterSelected = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryChipNotSelectedPreview() {
    val uiFilter = UiFilter(
        Filter("Deselected Category", "Deselected Category"),
        isSelected = false
    )

    MyfirstreduxappTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            CategoryChip(
                uiFilter = uiFilter,
                onFilterSelected = {}
            )
        }
    }
}

@Preview(
    name = "favorite_store_item",
    widthDp = 480,
    showBackground = true,
    uiMode = UI_MODE_NIGHT_NO
)
@Composable
fun StoreItemRowPreview1() {
    val product = Product(
        id = 1,
        name = "Awesome Product",
        category = "Cat 1",
        image = "",
        price = BigDecimal(10)
    )

    MyfirstreduxappTheme {
        StoreItemRow(
            product,
            onAddToCart = {},
            isFavorite = true,
            onFavoriteClick = {}
        )
    }
}

@Preview(name = "not_favorite_store_item", widthDp = 480, showBackground = true)
@Composable
fun StoreItemRowPreview2() {
    val product = Product(
        id = 1,
        name = "Awesome Product",
        category = "Cat 1",
        image = "",
        price = BigDecimal(100)
    )

    MyfirstreduxappTheme {
        StoreItemRow(
            product,
            onAddToCart = {},
            isFavorite = false,
            onFavoriteClick = {}
        )
    }
}