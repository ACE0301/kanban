package com.ace.homework2.view.ui.auth

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ace.homework2.R
import com.ace.homework2.model.network.TrelloHolder
import com.ace.homework2.model.prefs.AppPreferencesHelper
import com.ace.homework2.view.ui.boards.BoardsView
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment : Fragment() {

    @Inject
    lateinit var appPreferencesHelper: AppPreferencesHelper

    companion object {
        const val TAG = "LoginFragment"
        fun newInstance() = LoginFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_login, container, false)

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(this)
            .get(LoginViewModel::class.java)

        btnAuth.setOnClickListener {
            btnAuth.visibility = View.GONE
            wv_auth.visibility = View.VISIBLE
            wv_auth.settings.javaScriptEnabled = true
            wv_auth.loadUrl(TrelloHolder.url)
            wv_auth.webViewClient = object : WebViewClient() {
                @SuppressLint("FragmentLiveDataObserve")
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url!!.startsWith(TrelloHolder.REST_CALLBACK_URL)) {
                        viewModel.authorization(url)
                        viewModel.successAuthorization.observe(this@LoginFragment, Observer {
                            if (it == true) {
                                (activity as? BoardsView)?.showBoards()
                            } else {
                                Toast.makeText(context, getString(R.string.auth_error), Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        view?.loadUrl(url)
                    }
                    return true
                }
            }
        }
    }
}