package com.ace.homework2.view.ui

import androidx.fragment.app.Fragment

interface FragmentView {
    fun openFragmentWithBackstack(fragment: Fragment, tag: String)
    fun openFragment(fragment: Fragment, tag: String)
}