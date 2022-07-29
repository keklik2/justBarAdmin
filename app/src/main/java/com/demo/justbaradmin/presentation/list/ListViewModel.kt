package com.demo.justbaradmin.presentation.list

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.justbaradmin.R
import com.demo.justbaradmin.Screens
import com.demo.justbaradmin.data.Server
import com.demo.justbaradmin.domain.Cocktail
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    fun goToAddCocktailScreen() = router.navigateTo(Screens.DetailFragment())
    fun goToAddCocktailScreen(cocktail: Cocktail) =
        router.navigateTo(Screens.DetailFragment(cocktail))

    private val _list = Server.getAllCocktails()
    val list get() = _list

    fun deleteCocktail(cocktail: Cocktail) {
        showAlert(
            AppDialogContainer(
                getString(R.string.dialog_title_delete_cocktail),
                String.format(
                    getString(R.string.dialog_delete_cocktail),
                    cocktail.title
                ),
                positiveBtnCallback = {
                    Server.deleteCocktail(
                        cocktail,
                        onErrorCallback = {
                            showAlert(
                                AppDialogContainer(
                                    getString(R.string.dialog_title_error),
                                    it,
                                    positiveBtnCallback = {}
                                )
                            )
                        }
                    )
                },
                negativeBtnCallback = {}
            )

        )
    }
}
