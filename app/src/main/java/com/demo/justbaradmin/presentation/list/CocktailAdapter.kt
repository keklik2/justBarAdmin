package com.demo.justbaradmin.presentation.list

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.demo.architecture.helpers.setVisibility
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.ItemCocktailBinding
import com.demo.justbaradmin.domain.Cocktail
import com.demo.justbaradmin.domain.GlassType
import me.ibrahimyilmaz.kiel.adapterOf
import me.ibrahimyilmaz.kiel.core.RecyclerViewHolder

object CocktailAdapter {
    fun get(onItemClickListener: ((Cocktail) -> Unit)? = null) =
        adapterOf<Cocktail> {
            diff(
                areItemsTheSame = { old, new -> old == new },
                areContentsTheSame = { old, new ->
                    old.title == new.title
                            && old.glassType == new.glassType
                            && old.isIBA == new.isIBA
                            && old.alcoholPer == new.alcoholPer
                            && old.volume == new.volume
                            && old.taste == new.taste
                            && old.alcoholIngredients == new.alcoholIngredients
                            && old.otherIngredients == new.otherIngredients
                }
            )
            register(
                layoutResource = R.layout.item_cocktail,
                viewHolder = ::CocktailViewHolder,
                onBindViewHolder = { vh, _, item ->
                    vh.itemView.setOnClickListener {
                        onItemClickListener?.invoke(item)
                    }

                    with(vh.binding) {
                        tvTitle.text = item.title
                        tvStructure.text = getIngredientsList(vh, item)
                        tvPercent.text =
                            String.format(
                            getString(vh, R.string.format_alc_percentage),
                            item.alcoholPer
                        )
                        tvMilis.text =
                            String.format(
                            getString(vh, R.string.format_millis),
                            item.volume
                        )
                        ivIba.setVisibility(item.isIBA)
                        Glide
                            .with(vh.itemView.context)
                            .load(getGlassResource(vh, item))
                            .centerInside()
                            .into(ivDrink)
                    }
                }
            )
        }

    private fun getIngredientsList(vh: CocktailViewHolder, cocktail: Cocktail): String {
        return StringBuilder().apply {
            append(getString(vh, R.string.format_alc))
            cocktail.alcoholIngredients.keys.forEach { append(" ").append(it) }
            append("\n")
            append(getString(vh, R.string.format_taste)).append(" ").append(getString(vh, cocktail.taste.titleRes))
        }.toString()
    }

    private fun getGlassResource(vh: CocktailViewHolder, cocktail: Cocktail): Int {
        return when (cocktail.glassType) {
            GlassType.SHORT -> R.drawable.ic_short_drink
            GlassType.MIDDLE -> R.drawable.ic_middle_drink
            GlassType.LONG -> R.drawable.ic_long_drink
        }
    }

    private fun getString(vh: CocktailViewHolder, res: Int): String =
        vh.itemView.context.getString(res)

    private fun getDrawable(vh: CocktailViewHolder, res: Int): Drawable? =
        AppCompatResources.getDrawable(vh.itemView.context, res)
}

class CocktailViewHolder(view: View) : RecyclerViewHolder<Cocktail>(view) {
    val binding = ItemCocktailBinding.bind(view)
}
