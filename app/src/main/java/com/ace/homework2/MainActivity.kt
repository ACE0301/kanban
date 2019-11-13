package com.ace.homework2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ace.homework2.view.ui.boards.BoardsView
import com.ace.homework2.view.ui.auth.LoginFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.details.DetailFragment
import com.ace.homework2.view.ui.details.DetailView
import com.github.scribejava.core.model.OAuthConstants.TOKEN

class MainActivity : AppCompatActivity(), DetailView, BoardsView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            if (baseContext.getSharedPreferences(TOKEN, MODE_PRIVATE).getString(
                    TOKEN,
                    ""
                ).isNullOrEmpty()
            ) {
                showLoginFragment()
            } else showBoards()

        }
    }

    private fun showLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment.newInstance(), LoginFragment.TAG)
            .commit()
    }

    override fun openDetailFragment(boardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailFragment.newInstance(boardId), DetailFragment.TAG)
            .addToBackStack(DetailFragment.TAG)
            .commit()
    }

    override fun showBoards() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, BoardsFragment.newInstance(), BoardsFragment.TAG)
            .commit()
    }

}

