package com.demo.justbaradmin.presentation.login

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.justbaradmin.R
import com.demo.justbaradmin.Screens
import com.demo.justbaradmin.data.Login
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    private fun goToMenuScreen() = router.replaceScreen(Screens.MenuFragment())

    fun login(
        email: String,
        password: String
    ) {
        Login.loginUser(
            email,
            password,
            onSuccessListener = {
                showToast(getString(R.string.toast_login))
                goToMenuScreen()
            },
            onErrorListener = {
                showAlert(
                    AppDialogContainer(
                        title = getString(R.string.dialog_title_error),
                        message = it,
                        positiveBtnCallback = { }
                    )
                )
            }
        )
    }
}
