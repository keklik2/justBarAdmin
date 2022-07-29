package com.demo.justbaradmin.presentation.detail.adapters

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.ItemIngredientBinding
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object IngredientsAdapter {
    fun get(onDeleteClickListener: ((Ingredient) -> Unit)? = null) =
        adapterOf<Ingredient> {
            diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new ->
                    old.title == new.title && old.volume == old.volume
                }
            )
            register(
                layoutResource = R.layout.item_ingredient,
                viewHolder = ::IngredientViewHolder,
                onBindViewHolder = { vh, _, item ->
                    with(vh.binding) {
                        etAlcTitle.setText(item.title)
                        etAlcTitle.addTextChangedListener { item.title = it?.toString().orEmpty() }
                        etMilliliters.setText(item.volume.toString())
                        etMilliliters.addTextChangedListener {
                            val newVolume = it?.toString()
                            item.volume =
                                if (!newVolume.isNullOrEmpty() && !newVolume.isNullOrBlank())
                                    newVolume.toInt()
                                else 0
                        }
                        btnDelete.setOnClickListener { onDeleteClickListener?.invoke(item) }
                    }
                }
            )
        }
}

class IngredientViewHolder(view: View) : RecyclerViewHolder<Ingredient>(view) {
    val binding = ItemIngredientBinding.bind(view)
}

data class Ingredient(
    var title: String,
    var volume: Int
)
