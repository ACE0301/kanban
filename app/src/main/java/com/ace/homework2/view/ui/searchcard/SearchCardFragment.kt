package com.ace.homework2.view.ui.searchcard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ace.homework2.base.BaseFragment
import com.ace.homework2.model.cards.Card
import com.ace.homework2.view.ui.FragmentView
import kotlinx.android.synthetic.main.fragment_search_card.*
import javax.inject.Inject

class SearchCardFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {

        private const val ARGUMENT_BOARD_ID = "ARGUMENT_BOARD_ID"
        const val TAG = "SearchCardFragment"
        fun newInstance(boardId: String) = SearchCardFragment().apply {
            arguments = Bundle().apply {
                putString(ARGUMENT_BOARD_ID, boardId)
            }
        }
    }

    private val boardId: String
        get() = arguments?.getString(ARGUMENT_BOARD_ID) ?: ""
    lateinit var searchCardViewModel: SearchCardViewModel
    lateinit var cards: List<Card>
    private val searchCardAdapter = SearchCardAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(com.ace.homework2.R.layout.fragment_search_card, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchCardViewModel =
            ViewModelProvider(this, viewModelFactory)[SearchCardViewModel::class.java]
        rvSearchCardList.layoutManager = LinearLayoutManager(context)
        rvSearchCardList.adapter = searchCardAdapter
        searchCardViewModel.loadCards(boardId)
        searchCardViewModel.cards.observe(viewLifecycleOwner, Observer {
            searchCardAdapter.setData(it)
            cards = it
            checkIfEditTextHasText()
        })
        searchCardViewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == true) loading() else stopLoading()
        })

        etSearchCardName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                searchCardAdapter.setData(cards.filter {
                    it.name.contains(s.toString())
                })
            }
        })
        searchCardAdapter.onItemClickListener = {
            (activity as? FragmentView)?.openDetailsFragment(it)
        }
    }

    private fun checkIfEditTextHasText() {
        searchCardAdapter.setData(cards.filter {
            it.name.contains(etSearchCardName.text)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rvSearchCardList.adapter = null
    }
}