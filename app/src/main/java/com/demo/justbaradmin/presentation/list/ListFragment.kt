package com.demo.justbaradmin.presentation.list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment: BaseFragment(R.layout.fragment_notes_list) {
    override val binding: FragmentNotesListBinding by viewBinding()
    override val vm: ListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddCocktailListener()
    }
    override var setupBinds: (() -> Unit)? = {
        setupListBind()
    }

    private val adapter by lazy {
        CocktailAdapter.get {
            vm.goToAddCocktailScreen(it)
        }
    }


    /**
     * Binds & Observers
     */
    private fun setupListBind() {
        vm.list.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }


    /**
     * Listeners
     */
    private fun setupAddCocktailListener() {
        binding.fbAddCocktail.setOnClickListener {
            vm.goToAddCocktailScreen()
        }
    }


    /**
     * Overridden functions
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCocktails.adapter = adapter
    }
}
