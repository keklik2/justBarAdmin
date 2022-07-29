package com.demo.justbaradmin

import com.demo.justbaradmin.domain.Cocktail
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun ListFragment() = FragmentScreen {
        com.demo.justbaradmin.presentation.list.ListFragment()
    }

    fun DetailFragment() = FragmentScreen {
        com.demo.justbaradmin.presentation.detail.DetailFragment.newInstance()
    }

    fun DetailFragment(cocktail: Cocktail) = FragmentScreen {
        com.demo.justbaradmin.presentation.detail.DetailFragment.newInstance(cocktail)
    }

    fun LoginFragment() = FragmentScreen {
        com.demo.justbaradmin.presentation.login.LoginFragment()
    }

    fun MenuFragment() = FragmentScreen {
        com.demo.justbaradmin.presentation.menu.MenuFragment()
    }
}
