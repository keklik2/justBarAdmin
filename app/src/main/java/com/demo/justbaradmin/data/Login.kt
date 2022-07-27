package com.demo.justbaradmin.data

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object Login {

    private val auth = Firebase.auth

    fun isLogged(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccessListener: (() -> Unit)? = null,
        onErrorListener: ((e: String) -> Unit)? = null
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onSuccessListener?.invoke()
                else onErrorListener?.invoke(task.exception.toString())
            }
    }

}
