package com.example.foodia.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.foodia.model.Filter
import com.example.foodia.ui.theme.Caramel40
import com.example.foodia.ui.theme.primaryLight

@Composable
fun FilterBar(
    filter: List<Filter>,
    onShowFilters: () -> Unit
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(start = 12.dp, end = 8.dp),
        modifier = Modifier.heightIn(min = 56.dp)
    ) {
        item {
            IconButton(onClick = { /*TODO*/ }) {
                Surface(
                    modifier = Modifier.diagonalGradientBorder(
                        colors = listOf(Caramel40, Caramel40),
                        shape = CircleShape
                    )
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = "Filters",
                    )
                }
            }
        }
        items(filter) { filters ->
            FilterChip(filter = filters)
        }
    }

}


@Composable
private fun FilterChip(
    filter: Filter,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small
) {
    val (selected, setSelected) = filter.enabled
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        label = "bg color",
    )
    val border = Modifier.fadeInGradientBorder(
        showBorder = !selected,
        colors = listOf(Caramel40, primaryLight),
        shape = shape
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
        label = "Text color"
    )
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = textColor,
        shape = shape,
        shadowElevation = 2.dp
    ) {
        val interactionSources = remember {
            MutableInteractionSource()
        }
        val pressed by interactionSources.collectIsPressedAsState()
        val backgroundPressed = if (pressed) {
            Modifier.offsetGradientBackground(
                colors = listOf(Caramel40, primaryLight),
                200f,
                0f
            )
        } else {
            Modifier.background(Color.Transparent)
        }
        Box(
            modifier = Modifier
                .toggleable(
                    value = selected,
                    onValueChange = setSelected,
                    interactionSource = interactionSources,
                    indication = null
                )
                .then(backgroundPressed)
                .then(border)
        ) {
            Text(
                text = filter.name,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 6.dp
                )
            )
        }
    }


}