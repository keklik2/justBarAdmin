package com.demo.justbaradmin.data

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.justbaradmin.domain.Cocktail
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Server {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    fun getAllCocktails(): LiveData<List<Cocktail>> {
        val toReturn = MutableLiveData<List<Cocktail>>()

        db.collection(CHILD_COCKTAILS)
            .orderBy("title")
            .addSnapshotListener { value, error ->
                toReturn.value =
                    if (error != null) listOf()
                    else value?.toObjects(Cocktail::class.java) ?: listOf()
            }
        return toReturn
    }

    fun addOrEditCocktail(
        cocktail: Cocktail,
        onSuccessCallback: (() -> Unit)? = null,
        onErrorCallback: ((e: String) -> Unit)? = null
    ) {
        db.collection(CHILD_COCKTAILS).document(cocktail.title)
            .set(cocktail)
            .addOnSuccessListener { onSuccessCallback?.invoke() }
            .addOnFailureListener { onErrorCallback?.invoke(it.toString()) }
    }

    fun deleteCocktail(
        cocktail: Cocktail,
        onSuccessCallback: (() -> Unit)? = null,
        onErrorCallback: ((e: String) -> Unit)? = null
    ) {
        db.collection(CHILD_COCKTAILS).document(cocktail.title)
            .delete()
            .addOnSuccessListener { onSuccessCallback?.invoke() }
            .addOnFailureListener { onErrorCallback?.invoke(it.toString()) }
    }

    private const val CHILD_COCKTAILS = "cocktails"
}
