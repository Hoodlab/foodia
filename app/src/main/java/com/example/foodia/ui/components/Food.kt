package com.example.foodia.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foodia.R
import com.example.foodia.model.CollectionType
import com.example.foodia.model.FoodCollection
import com.example.foodia.model.FoodItem
import com.example.foodia.ui.theme.Caramel40
import com.example.foodia.ui.theme.Caramel80
import com.example.foodia.ui.theme.Orange40
import com.example.foodia.ui.theme.Orange80
import com.example.foodia.ui.theme.pacificoFont

private val HighlightCardWidth = 170.dp
private val HighlightCardPadding = 16.dp
private val Density.cardWidthWithPaddingPx
    get() = (HighlightCardWidth + HighlightCardPadding).toPx()


@Composable
fun FoodCollectionComp(
    foodCollection: FoodCollection,
    onFoodClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    index: Int = 0,
    highlight: Boolean = true
) {
    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = foodCollection.name,
                style = MaterialTheme.typography.headlineSmall,
                fontFamily = pacificoFont,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(
                    Alignment.CenterVertically
                ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
        if (highlight && foodCollection.type == CollectionType.Highlight) {
            HighlightedFood(
                index = index,
                foodItems = foodCollection.snacks,
                onFoodClick = onFoodClick,
            )
        } else {
            Foods(foodItems = foodCollection.snacks, onFoodClick = onFoodClick)
        }
    }


}

@Composable
fun Foods(
    foodItems: List<FoodItem>,
    onFoodClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
    ) {
        items(foodItems) { foodItem ->
            FoodItemComp(foodItem = foodItem, onFoodClick = onFoodClick)
        }
    }

}


@Composable
fun FoodItemComp(
    foodItem: FoodItem,
    onFoodClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = { onFoodClick(foodItem.id) })
        ) {
            FoodImage(
                imageUrl = foodItem.imageUrl,
                contentDescription = null,
                elevation = 4.dp,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = foodItem.name,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )

        }

    }

}


@Composable
fun HighlightedFood(
    index: Int,
    foodItems: List<FoodItem>,
    onFoodClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val rowState = rememberLazyListState()
    val cardWidthWithPaddingPx = with(LocalDensity.current) {
        cardWidthWithPaddingPx
    }
    val scrollProvide = {
        val offsetFromStart = cardWidthWithPaddingPx * rowState.firstVisibleItemIndex
        offsetFromStart + rowState.firstVisibleItemScrollOffset
    }

    val gradient = when ((index / 2) % 2) {
        0 -> listOf(Orange80, Caramel40, Caramel80, Orange40)
        else -> listOf(Caramel80, Caramel40, Orange40, Orange80)
    }
    LazyRow(
        state = rowState,
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp)
    ) {
        itemsIndexed(foodItems) { index, foodItem ->
            HighlightedFoodItem(
                foodItem = foodItem,
                onFoodClick = onFoodClick,
                index = index,
                gradient = gradient,
                scrollProvider = scrollProvide
            )
        }
    }

}


@Composable
private fun HighlightedFoodItem(
    foodItem: FoodItem,
    onFoodClick: (Long) -> Unit,
    index: Int,
    gradient: List<Color>,
    scrollProvider: () -> Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(
                width = HighlightCardWidth,
                height = 250.dp
            )
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    onFoodClick(foodItem.id)
                }
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .height(100.dp)
                        .fillMaxWidth()
                        .offsetGradientBackground(
                            colors = gradient,
                            width = {
                                6 * cardWidthWithPaddingPx
                            },
                            offset = {
                                val left = index * cardWidthWithPaddingPx
                                val gradientOffset = left - (scrollProvider() / 3f)
                                gradientOffset
                            }
                        )
                )
                FoodImage(
                    imageUrl = foodItem.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.BottomCenter)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = foodItem.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = foodItem.tagline,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }

}

@Composable
fun FoodImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    Surface(
        color = Color.LightGray,
        shadowElevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

    }

}










