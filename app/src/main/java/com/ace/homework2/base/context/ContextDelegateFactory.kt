package com.ace.homework2.base.context

import android.content.Context

object ContextDelegateFactory {
    fun create(context: Context?) = ContextContextDelegate(context)
}