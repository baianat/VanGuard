package app.baianat.com.vanguard.views.editables.viewStates

import app.baianat.com.vanguard.views.editables.viewDecorator.CustomViewDecorator
import app.baianat.com.vanguard.utils.print

class ValidState(stateArgs: StateArgs) : BaseState(stateArgs) {

    override fun onTextChanged(text: String) {
        "text changed in ValidState".print()


        if (validationConfirmed && viewDecorator?.isStatic()==false) {
            (viewDecorator as? CustomViewDecorator)?.hideValidationIcon()
            validationConfirmed = false
        }

        when {
            areRulesValid() && text.isEmpty()-> {
                changeStateInto(State.VALID)
            }
            needServerValidation() -> {
                changeStateInto(State.SERVER_PENDING)
            }
            !areRulesValid() -> {
                changeStateInto(State.INVALID)
            }
        }


    }


    private var validationConfirmed = false
    override fun onFocusChanged(hasFocus: Boolean) {

        if (!hasFocus) {
            confirmValidation()
        }

    }

    private fun confirmValidation() {
        (viewDecorator as? CustomViewDecorator)?.confirmValidationWithIcon()
        validationConfirmed = true
    }

    override fun onStateChanged() {
        if (viewDecorator?.isStatic() == true) {
            confirmValidation()
        }

        when (stateArgs.validationCase.lastState) {
            is ServerPendingState -> {
                viewDecorator?.apply {
                    confirmValidation()
                    hideServerLoader()
                }
            }
            is InvalidState -> {
                viewDecorator?.apply {
                    clearHelper()
                    clearError()
                }
                (viewDecorator as? CustomViewDecorator)?.apply {
                    if (isStatic()) confirmValidationWithIcon()
                }
            }
            is InitState -> {
                when {
                    viewDecorator?.hasTextInitially == true -> {
                        confirmValidation()
                        viewDecorator?.clearHelper()
                    }
                }

            }
        }
    }

    override fun isValid(): Boolean {
        return true
    }
}