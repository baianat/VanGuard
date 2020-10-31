package app.baianat.com.vanguard.views.editables

import android.view.View
import android.widget.EditText
import android.widget.TextView
import app.baianat.com.vanguard.R
import app.baianat.com.vanguard.validationCase.CaseArgs
import app.baianat.com.vanguard.utils.GuardHelper
import app.baianat.com.vanguard.views.GuardView

data class TextCaseArgs<V : View>(
    var caseArgs: CaseArgs<V, String>,
    var animateOnPending: Boolean = true,
    var onLocalValidationSuccess: ((textToFurtherValidate: String, validationCase: TextValidationCase) -> Unit)? = null
) {


    @Throws(IllegalStateException::class)
    fun shouldBuildCase(): Boolean {
        when {
            caseArgs.viewToWatch == null -> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("The view to validate must be defined!"))
            }
            caseArgs.viewToWatch !is TextView && caseArgs.viewToWatch !is GuardView-> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("The view to validate must be a Text Field!"))
            }
            caseArgs.rulesToValidate.count() == 0 -> {
                throw IllegalStateException(GuardHelper.decorateErrorMessage("A validation rule must be defined!"))
            }
        }
        return true

    }

    @Throws(IllegalStateException::class)
    fun handleStaticCase() {
        caseArgs.viewToWatch?.apply {
            when (this) {
                is GuardView -> setAsStatic()
                is EditText -> {
                    throw IllegalStateException(
                        GuardHelper.decorateErrorMessage(
                            resources.getString(
                                R.string.error_native_editText
                            )
                        )
                    )
                }
                is TextView -> (this).apply {
                    requestFocus()
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
            }
        }

    }


}