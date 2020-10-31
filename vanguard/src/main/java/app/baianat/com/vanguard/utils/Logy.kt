package app.baianat.com.vanguard.utils

import android.util.Log

internal class Logy(s: String) {
    init {

        Log.i("tag", s)
    }
}

fun String.print() {
    Logy(this)
}