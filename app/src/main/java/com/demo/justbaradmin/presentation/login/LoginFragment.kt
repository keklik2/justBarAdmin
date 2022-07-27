package com.demo.justbaradmin.presentation.login

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: BaseFragment(R.layout.fragment_login) {
    override val binding: FragmentLoginBinding by viewBinding()
    override val vm: LoginViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupLoginListener()
    }
    override var setupBinds: (() -> Unit)? = {

    }


    /**
     * Binds
     */

    /**
     * Listeners
     */
    private fun setupLoginListener() {
        binding.btnLogin.setOnClickListener {
            vm.login(
                binding.tietLogin.text.toString(),
                binding.tietPassword.text.toString()
            )
        }
    }
}
