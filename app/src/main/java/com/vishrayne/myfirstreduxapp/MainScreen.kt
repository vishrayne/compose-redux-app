package com.vishrayne.myfirstreduxapp

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import com.vishrayne.myfirstreduxapp.model.domain.Product
import com.vishrayne.myfirstreduxapp.ui.theme.MyfirstreduxappTheme
import java.math.BigDecimal

@Composable
fun MainScreen() {
    val mainViewModel: MainViewModel = viewModel()
    val products by mainViewModel.productsFlow.collectAsState()

    StoreItems(
        products = products,
        onAddToCart = { id ->
            Log.d("StoreItem", "OnAddToCart for $id clicked!")
        }
    )

    LaunchedEffect(true) {
        mainViewModel.refreshProducts()
    }
}

@Composable
fun StoreItems(
    products: List<Product>,
    onAddToCart: (productId: Int) -> Unit,
) {
    LazyColumn() {
        items(
            products,
            key = { product -> product.id }
        ) { product ->
            val (isFavorite, setFavorite) = rememberSaveable(product.id) {
                mutableStateOf(false)
            }

            StoreItemRow(
                product,
                onAddToCart = onAddToCart,
                isFavorite,
                onFavoriteClick = { setFavorite(!isFavorite) }
            )
        }
    }
}

@Composable
fun StoreItemRow(
    product: Product,
    onAddToCart: (productId: Int) -> Unit,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
                            onFavoriteClick()
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