package com.ace.homework2.base.context

import android.content.Context
import java.lang.ref.WeakReference

class ContextContextDelegate(context: Context?) : ContextDelegate {

    private val contextRef = WeakReference<Context>(context)

    override fun getContext(): Context? = contextRef.get()
}