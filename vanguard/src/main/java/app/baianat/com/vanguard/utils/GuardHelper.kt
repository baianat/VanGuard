package app.baianat.com.vanguard.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.lang.StringBuilder

object GuardHelper {

    fun decorateErrorMessage(error:String) : String{
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n")
        stringBuilder.append("--------VANGUARD ERROR-------")
        stringBuilder.append("\n")
        stringBuilder.append("--> $error <--")
        stringBuilder.append("\n")
        stringBuilder.append("-----------------------------")
        return stringBuilder.toString()
    }



}