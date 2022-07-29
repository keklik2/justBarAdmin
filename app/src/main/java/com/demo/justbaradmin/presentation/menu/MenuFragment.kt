package com.demo.justbaradmin.presentation.menu

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.justbaradmin.R
import com.demo.justbaradmin.data.JsonPicker
import com.demo.justbaradmin.databinding.FragmentMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment(R.layout.fragment_menu) {
    override val binding: FragmentMenuBinding by viewBinding()
    override val vm: MenuViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupManualBtnListener()
        setupJsonBtnListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupLoadingBind()
    }

    private val jsonPicker =
        JsonPicker(this) {
            vm.addCocktails(it)
        }


    /**
     * Binds
     */
    private fun setupLoadingBind() {
        vm::isLoading.bind {
            binding.loadLayout.setVisibility(it)
        }
    }


    /**
     * Listeners
     */
    private fun setupManualBtnListener() {
        binding.btnManual.setOnClickListener {
            vm.goToListFragment()
        }
    }

    private fun setupJsonBtnListener() {
        binding.btnJson.setOnClickListener {
            jsonPicker.load()
        }
    }
}
