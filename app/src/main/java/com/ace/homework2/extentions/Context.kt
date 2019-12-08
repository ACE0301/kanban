package com.ace.homework2.extentions

import android.content.Context

fun Context.dpToPx(dp:Int): Float {
    return dp.toFloat() * resources.displayMetrics.density
}