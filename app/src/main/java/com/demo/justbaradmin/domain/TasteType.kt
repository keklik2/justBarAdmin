package com.demo.justbaradmin.domain

import android.os.Parcelable
import com.demo.justbaradmin.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TasteType(
    val title: String,
    val res: Int
) : Parcelable {
    SWEET("SWEET", R.string.taste_sweet),
    SOUR("SOUR", R.string.taste_sour),
    SWEET_SOUR("SWEET_SOUR", R.string.taste_sweet_sour),
    STRONG("STRONG", R.string.taste_strong),
    SALTY("SALTY", R.string.taste_salty);
}
