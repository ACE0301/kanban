package com.ace.homework2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ace.homework2.view.ui.FragmentView
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.github.scribejava.core.model.OAuthConstants.TOKEN

class MainActivity : AppCompatActivity(), FragmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (baseContext.getSharedPreferences(TOKEN, MODE_PRIVATE).getString(
                    TOKEN,
                    ""
                ).isNullOrEmpty()
            ) {
                openFragment(AuthFragment.newInstance(), AuthFragment.TAG)
            } else openFragmentWithBackstack(BoardsFragment.newInstance(), BoardsFragment.TAG)
        }
    }

    override fun openFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .commit()
    }

    override fun openFragmentWithBackstack(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, tag)
            .addToBackStack(tag)
            .commit()
    }
}

