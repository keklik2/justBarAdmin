package com.demo.justbaradmin.presentation.detail.adapters

import android.view.View
import androidx.core.widget.addTextChangedListener
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.ItemCommonCocktailBinding
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object CommonCocktailsAdapter {
    fun get(
        onDeleteClickListener: ((CommonCocktail) -> Unit)? = null
    ) =
        adapterOf<CommonCocktail> {
            diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new -> old == new }
            )
            register(
                layoutResource = R.layout.item_common_cocktail,
                viewHolder = ::CommonCocktailViewHolder,
                onBindViewHolder = { vh, _, item ->
                    with(vh.binding) {
                        etAlcTitle.setText(item.title)
                        etAlcTitle.addTextChangedListener { item.title = it?.toString().orEmpty() }
                        btnDelete.setOnClickListener { onDeleteClickListener?.invoke(item) }
                    }
                }
            )
        }
}

class CommonCocktailViewHolder(view: View) : RecyclerViewHolder<CommonCocktail>(view) {
    val binding = ItemCommonCocktailBinding.bind(view)
}

data class CommonCocktail(
    var title: String
)
