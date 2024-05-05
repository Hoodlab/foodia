package com.example.foodia.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(.5f)
        ),
        title = {
            Text(
                text = "Delivery to 200 Majengo Road",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Outlined.ExpandMore, contentDescription = null)
            }
        },
        modifier = modifier
    )
    Divider()

}









