package com.ace.homework2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.boards.BoardsView
import com.ace.homework2.view.ui.cards.CardsFragment
import com.ace.homework2.view.ui.cards.CardsView
import com.ace.homework2.view.ui.details.DetailsFragment
import com.ace.homework2.view.ui.details.DetailsView
import com.ace.homework2.view.ui.history.HistoryFragment
import com.ace.homework2.view.ui.history.HistoryView
import com.ace.homework2.view.ui.members.MembersFragment
import com.ace.homework2.view.ui.members.MembersView
import com.github.scribejava.core.model.OAuthConstants.TOKEN


class MainActivity : AppCompatActivity(), CardsView, BoardsView, DetailsView, MembersView,
    HistoryView {

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

    override fun showBoards() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, BoardsFragment.newInstance(), BoardsFragment.TAG)
            .commit()
    }

    override fun openCardsFragment(boardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CardsFragment.newInstance(boardId), CardsFragment.TAG)
            .addToBackStack(CardsFragment.TAG)
            .commit()
    }

    override fun openDetailsFragment(cardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailsFragment.newInstance(cardId), DetailsFragment.TAG)
            .addToBackStack(DetailsFragment.TAG)
            .commit()
    }

    override fun openMembersFragment(boardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MembersFragment.newInstance(boardId), MembersFragment.TAG)
            .addToBackStack(MembersFragment.TAG)
            .commit()
    }

    override fun openHistoryFragment(cardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HistoryFragment.newInstance(cardId), HistoryFragment.TAG)
            .addToBackStack(HistoryFragment.TAG)
            .commit()
    }

}

