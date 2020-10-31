package app.baianat.com.vanguard.views.editables.viewStates

import app.baianat.com.vanguard.views.editables.viewDecorator.CustomViewDecorator
import app.baianat.com.vanguard.utils.print

class InvalidState(stateArgs: StateArgs) : BaseState(stateArgs) {


    var textChanged = false
    override fun onTextChanged(text: String) {


        textChanged = true

        "text change invalid state ${areRulesValid()}".print()

        if (areRulesValid()) {

            changeStateInto(State.UNDETERMINED)

            errorIsShown = false


        } else {
            if (errorIsShown) {
                viewDecorator?.setError(errorMsg())
            } else {
                viewDecorator?.setHelper(helperMsg())
            }
        }


    }

    private var errorIsShown = false

    override fun onFocusChanged(hasFocus: Boolean) {

        if (hasFocus) {
            (viewDecorator as? CustomViewDecorator)?.persistErrorColorWhenFocused()
        } else {
            if (!textChanged) return
            (viewDecorator as? CustomViewDecorator)?.persistErrorColorWhenDeFocused()
            viewDecorator?.setError(errorMsg())
            errorIsShown = true
        }

        textChanged = false
    }

    override fun onStateChanged() {
        textChanged = true

        when (lastState()) {
            is ValidState -> viewDecorator?.setHelper(helperMsg())
            is ServerPendingState -> {
                if (areRulesValid()) {
                    viewDecorator?.hideServerLoader()
                    (viewDecorator as? CustomViewDecorator)?.serverFailedValidation()
                } else {
                    viewDecorator?.setHelper(helperMsg())
                }
            }
            is InitState -> {
                when {
                    viewDecorator?.hasTextInitially == true && viewDecorator?.isStatic() == false -> {
                        (viewDecorator as? CustomViewDecorator)?.persistErrorColorWhenDeFocused()
                        viewDecorator?.setError(errorMsg())
                        errorIsShown = true
                    }
                }

            }
        }

        if (viewDecorator?.isStatic() == true) {
            viewDecorator?.invalidateStaticField(errorMsg())
        }
    }

    fun highlightErrorsImmediately() {
        (viewDecorator as? CustomViewDecorator)?.persistErrorColorWhenDeFocused()
        viewDecorator?.setError(errorMsg())
        errorIsShown = true
    }

    override fun isValid(): Boolean {
       return false
    }

}