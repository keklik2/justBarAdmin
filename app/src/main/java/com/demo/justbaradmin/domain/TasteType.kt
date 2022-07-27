package com.demo.justbaradmin.domain

import android.os.Parcelable
import com.demo.justbaradmin.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TasteType(
    val titleRes: Int,
    val res: Int
) : Parcelable {
    SWEET(R.string.taste_sweet, R.string.taste_sweet),
    SOUR(R.string.taste_sour, R.string.taste_sour),
    SWEET_SOUR(R.string.taste_sweet_sour, R.string.taste_sweet_sour),
    STRONG(R.string.taste_strong, R.string.taste_strong),
    SALTY(R.string.taste_salty, R.string.taste_salty);
}
