package com.vlg.list.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.vlg.list.App
import com.vlg.list.Navigator
import com.vlg.list.SaveConfig

open class BaseFragment : Fragment() {
    protected var navigator: Navigator = Navigator.Empty()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    protected val viewModel: GroupViewModel by activityViewModels {
        GroupViewModelFactory(
            (context?.applicationContext as App).database.itemDao(), SaveConfig(requireContext())
        )
    }
}