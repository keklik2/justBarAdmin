package com.demo.justbaradmin.presentation.detail

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentNoteDetailBinding
import com.demo.justbaradmin.domain.Cocktail
import com.demo.justbaradmin.domain.GlassType
import com.demo.justbaradmin.domain.TasteType
import com.demo.justbaradmin.presentation.detail.adapters.AlternativeRecipesAdapter
import com.demo.justbaradmin.presentation.detail.adapters.CommonCocktailsAdapter
import com.demo.justbaradmin.presentation.detail.adapters.IngredientsAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.github.anderscheow.validator.Validator
import io.github.anderscheow.validator.rules.common.NotBlankRule
import io.github.anderscheow.validator.rules.common.NotEmptyRule
import io.github.anderscheow.validator.validation
import io.github.anderscheow.validator.validator

@AndroidEntryPoint
class DetailFragment : BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: DetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddButtonsListeners()
        setupBackButtonListener()
        setupApplyButtonListener()
        setupValidationListeners()
    }
    override var setupBinds: (() -> Unit)? = {
        setupAdaptersBinds()
        setupOnCocktailReceivedBind()
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
     * Validations
     */
    private val titleValidation by lazy {
        validation(binding.tilCocktailTitle) {
            rules {
                +NotEmptyRule(ERR_EMPTY_TITLE)
                +NotBlankRule(ERR_BLANK_TITLE)
            }
        }
    }
    private val recipeValidation by lazy {
        validation(binding.tilRecipe) {
            rules {
                +NotEmptyRule(ERR_EMPTY_RECIPE)
                +NotBlankRule(ERR_BLANK_RECIPE)
            }
        }
    }
    private val alcoholValidation by lazy {
        validation(binding.tilAlcoholPer) {
            rules {
                +NotEmptyRule(ERR_EMPTY_ALCOHOL)
                +NotBlankRule(ERR_BLANK_ALCOHOL)
            }
        }
    }
    private val volumeValidation by lazy {
        validation(binding.tilVolume) {
            rules {
                +NotEmptyRule(ERR_EMPTY_VOLUME)
                +NotBlankRule(ERR_BLANK_VOLUME)
            }
        }
    }


    /**
     * Binds
     */
    private fun setupOnCocktailReceivedBind() {
        vm::cocktail.bind {
            it?.let {
                with(binding) {
                    tietCocktailTitle.setText(it.title)
                    tietRecipe.setText(it.recipe)
                    tietVolume.setText(it.volume.toString())
                    tietAlcoholPer.setText(it.alcoholPer.toString())
                    switchIba.isChecked = it.isIBA
                    switchIba.isActivated = it.isIBA
                    setGlassType(it.glassType)
                    setTasteType(it.taste)
                }
            }
        }
    }

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
        vm.alternativeRecipes.observe(viewLifecycleOwner) {
            alternativeRecipesAdapter.submitList(it)
            binding.rvExtra.adapter = alternativeRecipesAdapter
        }
    }


    /**
     * Listeners
     */
    private fun setupApplyButtonListener() {
        binding.btnSave.setOnClickListener {
            validator(requireActivity()) {
                listener = object : Validator.OnValidateListener {
                    override fun onValidateFailed(errors: List<String>) {}
                    override fun onValidateSuccess(values: List<String>) {
                        with(binding) {
                            vm.addOrEditCocktail(
                                tietCocktailTitle.text.toString(),
                                tietRecipe.text.toString(),
                                tietAlcoholPer.text.toString(),
                                tietVolume.text.toString(),
                                switchIba.isSelected,
                                getGlassType(),
                                getTasteType()
                            )
                        }
                    }
                }
                validate(titleValidation, recipeValidation, alcoholValidation, volumeValidation)
            }
        }
    }

    private fun setupBackButtonListener() {
        binding.btnBack.setOnClickListener {
            vm.exit()
        }
    }

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

    private fun setupValidationListeners() {
        with(binding) {
            tietCocktailTitle.addTextChangedListener {
                validator(requireActivity()) {
                    listener = object : Validator.OnValidateListener {
                        override fun onValidateFailed(errors: List<String>) {}
                        override fun onValidateSuccess(values: List<String>) {}
                    }
                    validate(titleValidation)
                }
            }
            tietRecipe.addTextChangedListener {
                validator(requireActivity()) {
                    listener = object : Validator.OnValidateListener {
                        override fun onValidateFailed(errors: List<String>) {}
                        override fun onValidateSuccess(values: List<String>) {}
                    }
                    validate(recipeValidation)
                }
            }
            tietAlcoholPer.addTextChangedListener {
                validator(requireActivity()) {
                    listener = object : Validator.OnValidateListener {
                        override fun onValidateFailed(errors: List<String>) {}
                        override fun onValidateSuccess(values: List<String>) {}
                    }
                    validate(alcoholValidation)
                }
            }
            tietVolume.addTextChangedListener {
                validator(requireActivity()) {
                    listener = object : Validator.OnValidateListener {
                        override fun onValidateFailed(errors: List<String>) {}
                        override fun onValidateSuccess(values: List<String>) {}
                    }
                    validate(volumeValidation)
                }
            }
        }
    }


    /**
     * Extras
     */
    private fun setGlassType(glass: GlassType) {
        when (glass) {
            GlassType.SHORT -> binding.spinnerGlass.setSelection(0)
            GlassType.MIDDLE -> binding.spinnerGlass.setSelection(1)
            GlassType.LONG -> binding.spinnerGlass.setSelection(2)
        }
    }

    private fun getGlassType(): GlassType {
        return when (binding.spinnerGlass.selectedItemPosition) {
            0 -> GlassType.SHORT
            1 -> GlassType.MIDDLE
            else -> GlassType.LONG
        }
    }

    private fun setTasteType(taste: TasteType) {
        when (taste) {
            TasteType.SWEET -> binding.spinnerTaste.setSelection(0)
            TasteType.SOUR -> binding.spinnerTaste.setSelection(1)
            TasteType.SWEET_SOUR -> binding.spinnerTaste.setSelection(2)
            TasteType.STRONG -> binding.spinnerTaste.setSelection(3)
            TasteType.SALTY -> binding.spinnerTaste.setSelection(4)
        }
    }

    private fun getTasteType(): TasteType {
        return when (binding.spinnerTaste.selectedItemPosition) {
            0 -> TasteType.SWEET
            1 -> TasteType.SOUR
            2 -> TasteType.SWEET_SOUR
            3 -> TasteType.STRONG
            else -> TasteType.SALTY
        }
    }


    /**
     * Base functions
     */
    private fun getArgs() {
        if (arguments != null) {
            with(requireArguments()) {
                if (containsKey(COCKTAIL_KEY)) vm.cocktail = getParcelable(COCKTAIL_KEY)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgs()
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


    companion object {
        private const val ERR_EMPTY_TITLE = R.string.err_empty_title
        private const val ERR_BLANK_TITLE = R.string.err_empty_title
        private const val ERR_EMPTY_RECIPE = R.string.err_empty_recipe
        private const val ERR_BLANK_RECIPE = R.string.err_empty_recipe
        private const val ERR_EMPTY_ALCOHOL = R.string.err_empty_alcohol
        private const val ERR_BLANK_ALCOHOL = R.string.err_empty_alcohol
        private const val ERR_EMPTY_VOLUME = R.string.err_empty_volume
        private const val ERR_BLANK_VOLUME = R.string.err_empty_volume

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
}
