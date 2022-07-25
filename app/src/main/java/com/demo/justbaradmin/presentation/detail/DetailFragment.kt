package com.demo.justbaradmin.presentation.detail

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.demo.architecture.BaseFragment
import com.demo.justbaradmin.R
import com.demo.justbaradmin.databinding.FragmentNoteDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment: BaseFragment(R.layout.fragment_note_detail) {
    override val binding: FragmentNoteDetailBinding by viewBinding()
    override val vm: DetailViewModel by viewModels()
    override var setupListeners: (() -> Unit)? = {

    }
    override var setupBinds: (() -> Unit)? = {

    }
}
