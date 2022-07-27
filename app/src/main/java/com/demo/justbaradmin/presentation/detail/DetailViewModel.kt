package com.demo.justbaradmin.presentation.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.demo.architecture.BaseViewModel
import com.demo.justbaradmin.domain.Cocktail
import com.demo.justbaradmin.presentation.detail.adapters.CommonCocktail
import com.demo.justbaradmin.presentation.detail.adapters.Ingredient
import com.demo.justbaradmin.presentation.detail.adapters.Recipe
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.autorun
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    var cocktail: Cocktail? by state(null)

    var alcoholIngredients: MutableList<Ingredient> by state(mutableListOf())
    var otherIngredients: MutableList<Ingredient> by state(mutableListOf())
    var commonCocktails: MutableList<CommonCocktail> by state(mutableListOf())
    private val _alternativeRecipes = MutableLiveData<MutableList<Recipe>>(mutableListOf())
    val alternativeRecipes get() = _alternativeRecipes

    init {
        autorun(::cocktail) { item ->
            item?.let { cocktail ->
                alcoholIngredients = cocktail.alcoholIngredients.toList().map { Ingredient(it.first, it.second) }.toMutableList()
                otherIngredients = cocktail.otherIngredients.toList().map { Ingredient(it.first, it.second) }.toMutableList()
                commonCocktails = cocktail.commonCocktails.map { CommonCocktail(it) }.toMutableList()
                _alternativeRecipes.value = cocktail.alternativeIngredients.toMutableList().map {
                    Recipe(it.toList().map { Ingredient(it.first, it.second) }.toMutableList())
                } as MutableList<Recipe>
            }
        }
    }


    /**
     * Adapters usage functions
     */
    fun addAlcoholIngredient() {
        alcoholIngredients = alcoholIngredients.toMutableList().apply {
            add(Ingredient("", 0))
        }
    }
    fun deleteAlcoholIngredient(ingredient: Ingredient) {
        alcoholIngredients = alcoholIngredients.toMutableList().apply {
            remove(ingredient)
        }
    }

    fun addOtherIngredient() {
        otherIngredients = otherIngredients.toMutableList().apply {
            add(Ingredient("", 0))
        }
    }
    fun deleteOtherIngredient(ingredient: Ingredient) {
        otherIngredients = otherIngredients.toMutableList().apply {
            remove(ingredient)
        }
    }

    fun addCommonCocktail() {
        commonCocktails = commonCocktails.toMutableList().apply {
            add(CommonCocktail(""))
        }
    }
    fun deleteCommonCocktail(cocktailTitle: CommonCocktail) {
        commonCocktails = commonCocktails.toMutableList().apply {
            remove(cocktailTitle)
        }
    }

    fun addAlternativeRecipe() {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value = list?.apply { add(Recipe(mutableListOf(Ingredient("", 0)))) }
    }
    fun deleteAlternativeRecipe(recipe: Recipe) {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value = list?.apply { remove(recipe) }
    }

    fun addAlternativeIngredient(recipeIndex: Int) {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value = list?.apply { get(recipeIndex).ingredients.add(Ingredient("", 0)) }
    }
    fun deleteAlternativeIngredient(recipeIndex: Int, ingredient: Ingredient) {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value = list?.apply { get(recipeIndex).ingredients.remove(ingredient) }
    }

}
