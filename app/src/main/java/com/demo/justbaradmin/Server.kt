package com.demo.justbaradmin

import android.annotation.SuppressLint
import com.demo.justbaradmin.domain.Cocktail
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Server {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    fun getAllCocktails(
        onSuccessCallback: ((List<Cocktail>) -> Unit)? = null,
        onErrorCallback: ((e: Exception) -> Unit)? = null
    ) {
        db.collection(CHILD_COCKTAILS)
            .orderBy("title")
            .addSnapshotListener { value, error ->
                if (error != null) onErrorCallback?.invoke(error)
                else onSuccessCallback?.invoke(value?.toObjects(Cocktail::class.java) ?: listOf())
            }
    }

    fun addOrEditCocktail(cocktail: Cocktail) {
        db.collection(CHILD_COCKTAILS)
            .add(cocktail)
            .addOnFailureListener { err ->
                throw Exception(err)
            }
    }

    fun deleteCocktail(cocktail: Cocktail) {

    }

    private const val CHILD_VERSION = "version"
    private const val CHILD_COCKTAILS = "cocktails"

    private const val VERSION_KEY = "ver"
}
