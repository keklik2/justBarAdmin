package com.demo.justbaradmin.presentation.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.architecture.helpers.refactorInt
import com.demo.architecture.helpers.refactorString
import com.demo.justbaradmin.R
import com.demo.justbaradmin.data.Server
import com.demo.justbaradmin.domain.Cocktail
import com.demo.justbaradmin.domain.GlassType
import com.demo.justbaradmin.domain.TasteType
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
                alcoholIngredients =
                    cocktail.alcoholIngredients.toList().map { Ingredient(it.first, it.second) }
                        .toMutableList()
                otherIngredients =
                    cocktail.otherIngredients.toList().map { Ingredient(it.first, it.second) }
                        .toMutableList()
                commonCocktails =
                    cocktail.commonCocktails.map { CommonCocktail(it) }.toMutableList()
                _alternativeRecipes.value = cocktail.alternativeIngredients.toMutableList().map {
                    Recipe(it.toList().map { Ingredient(it.first, it.second) }.toMutableList())
                } as MutableList<Recipe>
            }
        }
    }

    fun addOrEditCocktail(
        title: String?,
        recipe: String?,
        alcoholPercentage: String?,
        volume: String?,
        isIba: Boolean,
        glassType: GlassType,
        taste: TasteType
    ) {
        val rTitle = refactorString(title)
        val rRecipe = refactorString(recipe)
        val rAlcoholPercentage = refactorInt(alcoholPercentage)
        val rVolume = refactorInt(volume)

        Server.addOrEditCocktail(
            Cocktail(
                rTitle,
                glassType,
                isIba,
                rAlcoholPercentage,
                rVolume,
                getMapFromIngredients(alcoholIngredients),
                getMapFromIngredients(otherIngredients),
                taste,
                rRecipe,
                getListOfCommonCocktails(commonCocktails),
                getListOfAlternativeRecipes(_alternativeRecipes.value ?: mutableListOf())
            ),
            onSuccessCallback = {
                showToast(getString(R.string.toast_write_success)) // replace with resId
                exit()
            },
            onErrorCallback = {
                showAlert(
                    AppDialogContainer(
                        getString(R.string.dialog_title_error), // replace with resId
                        it,
                        positiveBtnCallback = {

                        }
                    )
                )
            }
        )
    }

    private fun getMapFromIngredients(list: List<Ingredient>): Map<String, Int> =
        mutableMapOf<String, Int>().apply {
            list.forEach { put(it.title, it.volume) }
        }

    private fun getListOfCommonCocktails(list: List<CommonCocktail>): List<String> =
        list.map { it.title }

    private fun getListOfAlternativeRecipes(list: List<Recipe>): List<Map<String, Int>> =
        mutableListOf<Map<String, Int>>().apply {
            list.forEach {
                add(mutableMapOf<String, Int>().apply {
                    it.ingredients.forEach { put(it.title, it.volume) }
                })
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
        showAlert(
            AppDialogContainer(
                getString(R.string.dialog_title_delete_alcohol),
                String.format(
                    getString(R.string.dialog_delete_alcohol),
                    ingredient.title
                ),
                positiveBtnCallback = {
                    alcoholIngredients = alcoholIngredients.toMutableList().apply {
                        remove(ingredient)
                    }
                },
                negativeBtnCallback = { }
            )
        )
    }

    fun addOtherIngredient() {
        otherIngredients = otherIngredients.toMutableList().apply {
            add(Ingredient("", 0))
        }
    }

    fun deleteOtherIngredient(ingredient: Ingredient) {
        showAlert(
            AppDialogContainer(
                getString(R.string.dialog_title_delete_ingredient),
                String.format(
                    getString(R.string.dialog_delete_ingredient),
                    ingredient.title
                ),
                positiveBtnCallback = {
                    otherIngredients = otherIngredients.toMutableList().apply {
                        remove(ingredient)
                    }
                },
                negativeBtnCallback = { }
            )
        )
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
        showAlert(
            AppDialogContainer(
                getString(R.string.dialog_title_delete_recipe),
                getString(R.string.dialog_delete_recipe),
                positiveBtnCallback = {
                    val list = _alternativeRecipes.value
                    _alternativeRecipes.value = list?.apply { remove(recipe) }
                },
                negativeBtnCallback = { }
            )
        )
    }

    fun addAlternativeIngredient(recipeIndex: Int) {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value =
            list?.apply { get(recipeIndex).ingredients.add(Ingredient("", 0)) }
    }

    fun deleteAlternativeIngredient(recipeIndex: Int, ingredient: Ingredient) {
        val list = _alternativeRecipes.value
        _alternativeRecipes.value = list?.apply { get(recipeIndex).ingredients.remove(ingredient) }
    }

}
