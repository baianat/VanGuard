package app.baianat.com.vanguard.views.editables.viewStates

import app.baianat.com.vanguard.views.editables.TextValidationCase
import app.baianat.com.vanguard.views.editables.viewDecorator.ViewDecorator
import app.baianat.com.vanguard.utils.print

abstract class BaseState(var stateArgs: StateArgs) {

    var viewDecorator: ViewDecorator? = null
    var textValidationCase: TextValidationCase? = null

    init {
        viewDecorator = stateArgs.viewDecorator
        textValidationCase = stateArgs.validationCase
    }

    abstract fun onTextChanged(text: String)

    abstract fun onFocusChanged(hasFocus: Boolean)

    open fun onStateChanged() {

    }

    fun needServerValidation() = textValidationCase?.needsFurtherValidation == true

    fun changeStateInto(newState: State) {

        stateArgs.validationCase.apply {

            if (state !is InitState && state !is InvalidState && state === lastState) {

                "---> state is ${state} and was ${lastState}".print()

                return@apply
            }

            lastState = state

            when (newState) {
                State.INITIAL -> {
                    state = initState
                }
                State.INVALID -> {
                    state = inValidState
                }
                State.VALID -> {
                    state = validState
                }
                State.UNDETERMINED -> {
                    state = if (needsFurtherValidation) {
                        serverPendingState
                    } else {
                        validState
                    }
                }
                State.SERVER_PENDING -> {
                    state = serverPendingState
                }

            }


            "state was ${lastState.javaClass.simpleName}".print()
            "state changed to ${state.javaClass.simpleName}".print()
            state.onStateChanged()

            // not to invoke the listener when unnecessary
            checkValidationNow()
        }

    }

    fun areRulesValid(): Boolean {
        return textValidationCase?.areRulesValid() == true
    }


    abstract fun isValid(): Boolean


    fun helperMsg() =
        stateArgs.validationCase.textRulesHelper.helperMsg(viewDecorator?.getText() ?: "")

    fun errorMsg() =
        stateArgs.validationCase.textRulesHelper.errorMsg(viewDecorator?.getText() ?: "")

    enum class State {
        INITIAL, INVALID, VALID, SERVER_PENDING, UNDETERMINED
    }

    fun lastState() = stateArgs.validationCase.lastState


}