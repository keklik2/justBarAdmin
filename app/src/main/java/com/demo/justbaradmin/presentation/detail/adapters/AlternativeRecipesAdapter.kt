package com.demo.justbaradmin.presentation.detail.adapters

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.ItemRecipeBinding
import com.demo.justbaradmin.presentation.detail.DetailViewModel
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder
import kotlin.reflect.KProperty0

object AlternativeRecipesAdapter {
    fun get(
        viewModel: KProperty0<DetailViewModel>
    ) =
        adapterOf<Recipe> {
            diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new ->
                    old.ingredients == new.ingredients
                            && old.ingredients.size == new.ingredients.size
                },
                getChangePayload = { old, new ->
                    if (old.ingredients.size != new.ingredients.size
                        || old.ingredients != new.ingredients
                    ) new.ingredients
                    else null
                }
            )
            register(
                layoutResource = R.layout.item_recipe,
                viewHolder = ::RecipeViewHolder,
                onBindViewHolder = { vh, index, item ->
                    val vm = viewModel.get()

                    with(vh.binding) {
                        btnAdd.setOnClickListener { vm.addAlternativeIngredient(index) }
                        btnDelete.setOnClickListener { vm.deleteAlternativeRecipe(item) }
                    }

                    vh.adapter =
                        IngredientsAdapter.get { vm.deleteAlternativeIngredient(index, it) }
                    vh.binding.rvIngredients.adapter = vh.adapter
                    vh.setIngredients(item.ingredients)
                }
            )
        }
}

class RecipeViewHolder(view: View) : RecyclerViewHolder<Recipe>(view) {
    val binding = ItemRecipeBinding.bind(view)
    var adapter: ListAdapter<Ingredient, RecyclerViewHolder<Ingredient>>? = null

    fun setIngredients(ingredients: List<Ingredient>) {
        adapter?.submitList(ingredients)
    }
}

data class Recipe(
    var ingredients: MutableList<Ingredient>
)
