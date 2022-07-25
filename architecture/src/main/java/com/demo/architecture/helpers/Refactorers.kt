package com.demo.architecture.helpers

fun refactorString(title: String?): String {
    return title?.trim() ?: ""
}

fun refactorInt(price: String?): Int {
    return try {
        price?.trim()?.toInt() ?: 0
    } catch (e: Exception) {
        0
    }
}

fun refactorDouble(price: String?): Double {
    return try {
        price?.trim()?.toDouble() ?: 0.0
    } catch (e: Exception) {
        0.0
    }
}
