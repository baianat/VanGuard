package app.baianat.com.vanguard.views.editables.viewStates

import app.baianat.com.vanguard.utils.print

class ServerPendingState(stateArgs: StateArgs) : BaseState(stateArgs) {


    private var isServerLoading = false

    private var serverValidationInterrupted = false

    override fun onTextChanged(text: String) {
        "text change sever pending with status ${areRulesValid()}".print()

        if (!areRulesValid()) {
            changeStateInto(State.INVALID)
        }else{
            if (text.isEmpty()){
                changeStateInto(State.VALID)
            }
        }

        if (isServerLoading) {
            viewDecorator?.hideServerLoader()
            serverValidationInterrupted = true
            isServerLoading = false
        }
    }


    override fun onFocusChanged(hasFocus: Boolean) {

        if (hasFocus) return
        "on focus change server pending with state ${hasFocus} ".print()

        viewDecorator?.showServerLoader()
        serverValidationInterrupted = false

        onLocalValidationSuccess()
    }

    private fun onLocalValidationSuccess() {
        if (isServerLoading) return

        val textToFurtherValidate = viewDecorator?.getText()
        stateArgs.validationCase.apply {
            this.textCaseArgs.onLocalValidationSuccess?.invoke(textToFurtherValidate ?: "", this)
        }

        isServerLoading = true

    }

    override fun onStateChanged() {
        when (lastState()) {

            is InvalidState -> {
                viewDecorator?.apply {
                    clearHelper()
                    clearError()
                }

            }
            is InitState -> {
                if (viewDecorator?.hasTextInitially==true) {
                    onFocusChanged(false)
                }
            }

        }

    }


    fun onServerSuccess() {
        isServerLoading = false
        stateArgs.validationCase.apply {
            if (!serverValidationInterrupted) {
                changeStateInto(State.VALID)
                validationStatus = true
            }
        }
    }

    fun onServerFailure(errorMsg: String) {
        isServerLoading = false
        stateArgs.validationCase.apply {
            if (!serverValidationInterrupted) {
                validationStatus = false
                changeStateInto(State.INVALID)
                viewDecorator.setError(errorMsg)
            }
        }
    }

    override fun isValid(): Boolean {
        return false
    }
}