package app.baianat.com.vanguard.validationCase

import android.view.View
import app.baianat.com.vanguard.rules.ValidationRule
import app.baianat.com.vanguard.utils.GuardHelper

data class CaseArgs<V:View,T>(var viewToWatch: V?=null,
                    var stateChangeCallback: ((isValid: Boolean, view: View) -> Unit)?=null,
                    var rulesToValidate: MutableList<ValidationRule<T>> = mutableListOf()) {


    @Throws(IllegalStateException::class)
    fun shouldBuildCase():Boolean{
        when {
            viewToWatch == null -> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("The view to validate must be defined!"))
            }
            rulesToValidate.count() == 0 -> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("A validation rule must be defined!"))
            }
            rulesToValidate.count() > 1 -> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("Only one rule is allowed for ${viewToWatch?.javaClass?.simpleName}!"))
            }
        }
        return true
    }



}