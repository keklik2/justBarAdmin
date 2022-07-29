package com.demo.justbaradmin.presentation.menu

import android.app.Application
import com.demo.architecture.BaseViewModel
import com.demo.architecture.dialogs.AppDialogContainer
import com.demo.justbaradmin.R
import com.demo.justbaradmin.Screens
import com.demo.justbaradmin.data.Server
import com.demo.justbaradmin.domain.Cocktail
import com.github.terrakok.cicerone.Router
import dagger.hilt.android.lifecycle.HiltViewModel
import me.aartikov.sesame.property.state
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val app: Application,
    override val router: Router
) : BaseViewModel(app) {

    var isLoading by state(false)

    fun goToListFragment() = router.navigateTo(Screens.ListFragment())

    fun addCocktails(list: List<Cocktail>) {
        isLoading = true
        list.forEach {
            Server.addOrEditCocktail(
                it,
                onSuccessCallback = {
                    showToast("Cocktail ${it.title} added successfully")
                    if (it.title == list.last().title) isLoading = false
                },
                onErrorCallback = { err ->
                    if (it.title == list.last().title) isLoading = false
                    showAlert(
                        AppDialogContainer(
                            getString(R.string.dialog_title_error),
                            err,
                            positiveBtnCallback = { }
                        )
                    )
                }
            )
        }
    }
}
