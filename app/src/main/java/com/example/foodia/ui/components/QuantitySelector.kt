package com.example.foodia.ui.components

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodia.ui.theme.Caramel80
import com.example.foodia.ui.theme.Orange40

@Composable
fun QuantitySelector(
    count: Int,
    decreaseItemCount: () -> Unit,
    increaseItemCount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = "Qty",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .padding(end = 18.dp)
                .align(Alignment.CenterVertically)
                .alpha(.5f)
        )
        GradientTintedIcon(
            imageVector = Icons.Default.Remove,
            onClick = decreaseItemCount,
            contentDescription = "decrease",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Crossfade(
            targetState = count,
            modifier = Modifier.align(Alignment.CenterVertically), label = "",
        ) {
            Text(
                text = "$it",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(min = 24.dp)
            )
        }
        GradientTintedIcon(
            imageVector = Icons.Default.Add,
            onClick = increaseItemCount,
            contentDescription = "Increase",
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }

}

@Composable
fun GradientTintedIcon(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Caramel80, Orange40)
) {
    val interactionSources = remember {
        MutableInteractionSource()
    }
    val border = Modifier.fadeInGradientBorder(
        showBorder = true,
        colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.primary),
        shape = CircleShape
    )
    val pressed by interactionSources.collectIsPressedAsState()

    val background = if (pressed) {
        Modifier.offsetGradientBackground(colors, 200f, 0f)
    } else {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    }
    val blendMode = if (isSystemInDarkTheme()) BlendMode.Darken else BlendMode.Plus

    val modifierColor = if (pressed) {
        Modifier.diagonalGradientTint(
            colors = listOf(
                MaterialTheme.colorScheme.onPrimaryContainer,
                MaterialTheme.colorScheme.onPrimaryContainer
            ),
            blendMode = blendMode
        )
    } else {
        Modifier.diagonalGradientTint(
            colors, blendMode
        )
    }

    Surface(
        modifier = modifier
            .clickable(
                onClick = onClick,
                interactionSource = interactionSources,
                indication = null
            )
            .clip(CircleShape)
            .then(border)
            .then(background),
        color = Color.Transparent
    ) {

        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifierColor,
        )
    }

}





