package com.example.foodia.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodia.R
import com.example.foodia.model.FoodCollection
import com.example.foodia.model.FoodRepo
import com.example.foodia.model.OrderLine
import com.example.foodia.ui.components.DestinationBar
import com.example.foodia.ui.components.FoodCollectionComp
import com.example.foodia.ui.components.FoodImage
import com.example.foodia.ui.components.QuantitySelector
import com.example.foodia.ui.components.SwipeToDisMissItem
import com.example.foodia.ui.detail.formatPrice

@Composable
fun Cart(
    onFoodItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CartViewModel = viewModel()
) {
    val orderLines by viewModel.orderLines.collectAsStateWithLifecycle()
    val inspiredByCart = remember {
        FoodRepo.getInspiredByCart()
    }
    Scaffold(modifier) { paddingValues ->
        Cart(
            orderLine = orderLines,
            removeFoodItem = viewModel::removeFoodItem,
            decreaseFoodItem = viewModel::decreaseFoodCount,
            increaseFoodItem = viewModel::decreaseFoodCount,
            inspiredByCart = inspiredByCart,
            onFoodItemClick = onFoodItemClick,
            modifier = Modifier.padding(paddingValues)
        )
    }

}

@Composable
fun Cart(
    orderLine: List<OrderLine>,
    removeFoodItem: (Long) -> Unit,
    decreaseFoodItem: (Long) -> Unit,
    increaseFoodItem: (Long) -> Unit,
    inspiredByCart: FoodCollection,
    onFoodItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            CartContent(
                orderLines = orderLine,
                removeFoodItem = removeFoodItem,
                decreaseFoodItem = decreaseFoodItem,
                increaseFoodItem = increaseFoodItem,
                inspiredByCart = inspiredByCart,
                onFoodItemClick = onFoodItemClick,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            DestinationBar(
                modifier = Modifier.align(Alignment.TopCenter)
            )
            CheckOutBar(modifier = Modifier.align(Alignment.BottomCenter))
        }

    }
}


@Composable
private fun CheckOutBar(
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Divider()
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Check Out",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartContent(
    orderLines: List<OrderLine>,
    removeFoodItem: (Long) -> Unit,
    decreaseFoodItem: (Long) -> Unit,
    increaseFoodItem: (Long) -> Unit,
    inspiredByCart: FoodCollection,
    onFoodItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val resource = LocalContext.current.resources

    val foodCountFormattedString = remember(orderLines.size, resource) {
        resource.getQuantityString(
            R.plurals.cart_order_count,
            orderLines.size,
            orderLines.size
        )
    }
    LazyColumn(
        modifier
    ) {
        item {
            Spacer(
                modifier = Modifier.windowInsetsTopHeight(
                    WindowInsets.statusBars.add(WindowInsets(top = 56.dp))
                )
            )
            Text(
                text = stringResource(id = R.string.cart_order_header, foodCountFormattedString),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 24.dp, vertical = 4.dp)
                    .wrapContentHeight()
            )
        }
        items(orderLines) { orderLine ->
            SwipeToDisMissItem(
                background = { offsetX ->
                    val backgroundColor = if (offsetX < -160.dp) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(backgroundColor)
                    )
                }
            ) {
                CartItem(
                    orderLine = orderLine,
                    removeFoodItem = removeFoodItem,
                    decreaseFoodItem = decreaseFoodItem,
                    increaseFoodItem = increaseFoodItem,
                    onFoodItemClick = onFoodItemClick
                )
            }
        }
        item {
            SummaryItem(
                subTotal = orderLines.map { it.foodItem.price * it.count }.sum(),
                shippingCosts = 369
            )
        }
        item {
            FoodCollectionComp(
                foodCollection = inspiredByCart,
                onFoodClick = onFoodItemClick,
                highlight = false
            )
            Spacer(modifier = Modifier.height(56.dp))
        }
    }

}

@Composable
fun CartItem(
    orderLine: OrderLine,
    removeFoodItem: (Long) -> Unit,
    decreaseFoodItem: (Long) -> Unit,
    increaseFoodItem: (Long) -> Unit,
    onFoodItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val foodItem = orderLine.foodItem
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onFoodItemClick(foodItem.id) }
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 24.dp)
    ) {
        val (divider, image, name, tag, priceSpacer, price, remove, quantity) = createRefs()
        createVerticalChain(name, tag, priceSpacer, price, chainStyle = ChainStyle.Packed)
        FoodImage(
            imageUrl = foodItem.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                }
        )
        Text(
            text = foodItem.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(name) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = remove.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        IconButton(
            onClick = { removeFoodItem(foodItem.id) },
            modifier = Modifier
                .constrainAs(remove) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 12.dp),
        ) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = null)
        }

        Text(
            text = foodItem.tagline,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(tag) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = parent.end,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )

        Spacer(modifier = Modifier
            .height(8.dp)
            .constrainAs(priceSpacer) {
                linkTo(top = tag.bottom, bottom = price.top)
            }
        )
        Text(
            text = formatPrice(foodItem.price),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(price) {
                linkTo(
                    start = image.end,
                    startMargin = 16.dp,
                    end = quantity.start,
                    endMargin = 16.dp,
                    bias = 0f
                )
            }
        )
        QuantitySelector(
            count = orderLine.count,
            decreaseItemCount = { decreaseFoodItem(foodItem.id) },
            increaseItemCount = { increaseFoodItem(foodItem.id) },
            modifier = Modifier.constrainAs(quantity) {
                baseline.linkTo(price.baseline)
                end.linkTo(parent.end)
            }
        )
        Divider(
            Modifier.constrainAs(divider) {
                linkTo(start = parent.start, end = parent.end)
                top.linkTo(parent.bottom)
            }
        )

    }
}

@Composable
fun SummaryItem(
    subTotal: Long,
    shippingCosts: Long,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = "Summary",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .heightIn(min = 56.dp)
                .wrapContentHeight()
        )
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Subtotal",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subTotal),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alignBy(LastBaseline)
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Shipping and Handling",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(shippingCosts),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alignBy(LastBaseline)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Row(
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Total",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
                    .alignBy(LastBaseline)
            )
            Text(
                text = formatPrice(subTotal + shippingCosts),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .alignBy(LastBaseline)
            )
        }
        Divider()
    }

}














