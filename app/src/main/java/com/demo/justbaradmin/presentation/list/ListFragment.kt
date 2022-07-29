package com.demo.justbaradmin.presentation.list

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.architecture.helpers.setVisibility
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentNotesListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : BaseFragment(R.layout.fragment_notes_list) {
    override val binding: FragmentNotesListBinding by viewBinding()
    override val vm: ListViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {
        setupAddCocktailListener()
        setupBackBtnListener()
        setupScrollListener()
        setupOnSwipeListener()
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
            binding.tvEmpty.setVisibility(it.isEmpty())
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

    private fun setupBackBtnListener() {
        binding.btnBack.setOnClickListener {
            vm.exit()
        }
    }

    private fun setupScrollListener() {
        binding.rvCocktails.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) if (isFbVisible()) hideFb()
                if (dy < 0) if (!isFbVisible()) showFb()
            }
        })
    }

    private fun setupOnSwipeListener() {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val currItem =
                    adapter.currentList[viewHolder.absoluteAdapterPosition]
                binding.rvCocktails.adapter?.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                vm.deleteCocktail(currItem)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.rvCocktails)
    }


    /**
     * Extras
     */
    private fun showFb() = binding.fbAddCocktail.show()
    private fun hideFb() = binding.fbAddCocktail.hide()
    private fun isFbVisible(): Boolean = binding.fbAddCocktail.isVisible


    /**
     * Overridden functions
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCocktails.adapter = adapter
    }
}
