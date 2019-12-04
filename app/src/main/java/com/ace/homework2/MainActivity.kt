package com.ace.homework2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ace.homework2.model.cards.Card
import com.ace.homework2.view.ui.action.ActionFragment
import com.ace.homework2.view.ui.FragmentView
import com.ace.homework2.view.ui.auth.AuthFragment
import com.ace.homework2.view.ui.boards.BoardsFragment
import com.ace.homework2.view.ui.cards.CardsFragment
import com.ace.homework2.view.ui.details.DetailsFragment
import com.ace.homework2.view.ui.members.MembersFragment
import com.ace.homework2.view.ui.searchcard.SearchCardFragment
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
                openLoginFragment()
            } else openBoardsFragment()

        }
    }

    private fun openLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, AuthFragment.newInstance(), AuthFragment.TAG)
            .commit()
    }

    override fun openBoardsFragment() {
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

    override fun openMembersFragment(boardId: String, card: Card) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                MembersFragment.newInstance(boardId, card),
                MembersFragment.TAG
            )
            .addToBackStack(MembersFragment.TAG)
            .commit()
    }

    override fun openHistoryFragment(cardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, ActionFragment.newInstance(cardId), ActionFragment.TAG)
            .addToBackStack(ActionFragment.TAG)
            .commit()
    }

    override fun openSearchCardFragment(boardId: String) {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container,
                SearchCardFragment.newInstance(boardId),
                SearchCardFragment.TAG
            )
            .addToBackStack(SearchCardFragment.TAG)
            .commit()
    }
}

