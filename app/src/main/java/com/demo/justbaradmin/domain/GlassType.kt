package com.demo.justbaradmin.domain

import android.os.Parcelable
import com.demo.justbaradmin.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class GlassType(
    val title: String,
    val res: Int
): Parcelable {
    LONG("LONG", R.drawable.ic_long_drink),
    MIDDLE("MIDDLE", R.drawable.ic_middle_drink),
    SHORT("SHORT", R.drawable.ic_short_drink);
}
