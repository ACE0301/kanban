package com.ace.homework2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.boards.BoardsView
import com.ace.homework2.view.ui.cards.CardsFragment
import com.ace.homework2.view.ui.cards.CardsView
import com.github.scribejava.core.model.OAuthConstants.TOKEN


class MainActivity : AppCompatActivity(), CardsView, BoardsView {

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
            .replace(R.id.container, AuthFragment.newInstance(), AuthFragment.TAG)
            .commit()
    }

    override fun openCardsFragment(boardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CardsFragment.newInstance(boardId), CardsFragment.TAG)
            .addToBackStack(CardsFragment.TAG)
            .commit()
    }

    override fun showBoards() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, BoardsFragment.newInstance(), BoardsFragment.TAG)
            .commit()
    }

}

