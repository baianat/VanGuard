package app.baianat.com.vanguard.validationCase

import android.os.Handler
import android.view.View
import app.baianat.com.vanguard.utils.print


abstract class ValidationCase<V : View, T>(internal var caseArgs: CaseArgs<V, T>) {

    private var onValidationCaseConfirmed: ((ValidationCase<*, *>) -> Unit)? = null
    open var viewToValidate: V = caseArgs.viewToWatch!!


    internal open fun startValidating(onValidationConfirmed: (ValidationCase<*, *>) -> Unit) {

        this.onValidationCaseConfirmed = onValidationConfirmed
        attachListener()
        checkValidationNow()
    }

    abstract fun attachListener()

    open fun stopValidating() {}

    abstract val valueToValidate: T


    internal open fun isThisCaseValid(): Boolean {
        return caseArgs.rulesToValidate.first().validate(valueToValidate)
    }


    private var wasValid = false
    var isValid = false
    private var counter = 0
    internal fun checkValidationNow() {

        wasValid = isValid
        isValid = isThisCaseValid()

        if (counter != 0 && isValid == wasValid) {
            return
        }
        caseArgs.stateChangeCallback?.invoke(isValid, (viewToValidate as View))

        onValidationCaseConfirmed?.invoke(this)
        counter++
    }


}