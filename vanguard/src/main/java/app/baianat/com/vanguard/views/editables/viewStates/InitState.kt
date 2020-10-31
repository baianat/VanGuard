package app.baianat.com.vanguard.views.editables.viewStates

class InitState(stateArgs: StateArgs) : BaseState(stateArgs) {

    override fun onTextChanged(text: String) {
        if (areRulesValid()) {
            viewDecorator?.setHelper(helperMsg())
            changeStateInto(State.UNDETERMINED)
        } else {
            changeStateInto(State.INVALID)
        }
    }

    override fun onFocusChanged(hasFocus: Boolean) {
        viewDecorator?.setHelper(helperMsg())
    }


    override fun onStateChanged() {
        stateArgs.validationCase.apply {
            if (valueToValidate.isNotEmpty()) {
                if (areRulesValid()) {
                    changeStateInto(State.UNDETERMINED)
                } else {
                    changeStateInto(State.INVALID)
                }
            }
        }
    }


    fun validateInitialText() {
        viewDecorator?.hasTextInitially = true

        textValidationCase?.apply {

            if (areRulesValid()) {
                changeStateInto(State.UNDETERMINED)

            } else {
                changeStateInto(State.INVALID)
            }

        }
    }

    override fun isValid(): Boolean {
        return areRulesValid()
    }

}