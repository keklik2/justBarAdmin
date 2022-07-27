package com.demo.justbaradmin.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cocktail(
    var title: String = "",
    var glassType: GlassType = GlassType.LONG,
    var isIBA: Boolean = false,
    var alcoholPer: Int = 0,
    var volume: Int = 0,
    var alcoholIngredients: Map<String, Int> = mapOf(),
    var otherIngredients: Map<String, Int> = mapOf(),
    var taste: TasteType = TasteType.STRONG,
    var recipe: String = "",
    var commonCocktails: List<String> = listOf(),
    var alternativeIngredients: List<Map<String, Int>> = listOf()
): Parcelable
