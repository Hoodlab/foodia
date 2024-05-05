package com.example.foodia.model

import androidx.compose.runtime.Immutable

@Immutable
data class FoodCollection(
    val id: Long,
    val name: String,
    val snacks: List<FoodItem>,
    val type: CollectionType = CollectionType.Normal
)

enum class CollectionType { Normal, Highlight }

/**
 * A fake repo
 */
object FoodRepo {
    fun getFoods(): List<FoodCollection> = snackCollections
    fun getFoodById(snackId: Long) = foodItems.find { it.id == snackId }!!
    fun getRelated(@Suppress("UNUSED_PARAMETER") snackId: Long) = related
    fun getInspiredByCart() = inspiredByCart
    fun getFilters() = filters
    fun getCart() = cart

}

/**
 * Static data
 */

private val tastyTreats = FoodCollection(
    id = 1L,
    name = "Foodie picks",
    type = CollectionType.Highlight,
    snacks = foodItems.subList(0, 13)
)

private val popular = FoodCollection(
    id = 2L,
    name = "Popular on Foodia",
    snacks = foodItems.subList(14, 19)
)

private val wfhFavs = tastyTreats.copy(
    id = 3L,
    name = "Favourites"
)

private val newlyAdded = popular.copy(
    id = 4L,
    name = "Newly Added"
)

private val exclusive = tastyTreats.copy(
    id = 5L,
    name = "Only on Foodia"
)

private val also = tastyTreats.copy(
    id = 6L,
    name = "Customers also bought"
)

private val inspiredByCart = tastyTreats.copy(
    id = 7L,
    name = "Inspired by your cart"
)

private val snackCollections = listOf(
    tastyTreats,
    popular,
    wfhFavs,
    newlyAdded,
    exclusive
)

private val related = listOf(
    also,
    popular
)

private val cart = listOf(
    OrderLine(foodItems[4], 2),
    OrderLine(foodItems[6], 3),
    OrderLine(foodItems[8], 1)
)

@Immutable
data class OrderLine(
    val foodItem: FoodItem,
    val count: Int
)
