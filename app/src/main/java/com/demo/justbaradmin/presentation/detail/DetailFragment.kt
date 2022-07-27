package com.demo.justbaradmin.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentNoteDetailBinding
import com.demo.justbaradmin.domain.Cocktail
import com.demo.justbaradmin.presentation.detail.adapters.AlternativeRecipesAdapter
import com.demo.justbaradmin.presentation.detail.adapters.CommonCocktailsAdapter
import com.demo.justbaradmin.presentation.detail.adapters.IngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: DetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddButtonsListeners()
    }
    override var setupBinds: (() -> Unit)? = {
        setupAdaptersBinds()
    }

    /**
     * Individual variables
     */
    private val alcoholAdapter by lazy {
        IngredientsAdapter.get {
            vm.deleteAlcoholIngredient(it)
        }
    }
    private val otherIngredientsAdapter by lazy {
        IngredientsAdapter.get {
            vm.deleteOtherIngredient(it)
        }
    }
    private val commonCocktailsAdapter by lazy {
        CommonCocktailsAdapter.get {
            vm.deleteCommonCocktail(it)
        }
    }
    private val alternativeRecipesAdapter by lazy {
        AlternativeRecipesAdapter.get(::vm)
    }

    /**
     * Binds
     */
    private fun setupAdaptersBinds() {
        vm::alcoholIngredients.bind {
            alcoholAdapter.submitList(it)
        }
        vm::otherIngredients.bind {
            otherIngredientsAdapter.submitList(it)
        }
        vm::commonCocktails.bind {
            commonCocktailsAdapter.submitList(it)
        }
//        vm::alternativeRecipes.bind {
//            Log.d("vmtest", "new list: $it")
//            alternativeRecipesAdapter.submitList(it)
//        }
        vm.alternativeRecipes.observe(viewLifecycleOwner) {
            alternativeRecipesAdapter.submitList(it)
            binding.rvExtra.adapter = alternativeRecipesAdapter
        }
    }


    /**
     * Listeners
     */
    private fun setupAddButtonsListeners() {
        with(binding) {
            btnAddAlcohol.setOnClickListener {
                vm.addAlcoholIngredient()
            }
            btnAddIngredient.setOnClickListener {
                vm.addOtherIngredient()
            }
            btnAddCommonCocktail.setOnClickListener {
                vm.addCommonCocktail()
            }
            btnAddExtra.setOnClickListener {
                vm.addAlternativeRecipe()
            }
        }
    }


    companion object {
        private const val COCKTAIL_KEY = "cocktail_key"

        fun newInstance(): DetailFragment = DetailFragment()
        fun newInstance(cocktail: Cocktail): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(COCKTAIL_KEY, cocktail)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvAlcohol.adapter = alcoholAdapter
            rvIngredient.adapter = otherIngredientsAdapter
            rvCommonCocktails.adapter = commonCocktailsAdapter
            rvExtra.adapter = alternativeRecipesAdapter
        }
    }
}
