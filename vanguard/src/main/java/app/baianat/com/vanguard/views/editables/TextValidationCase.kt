package app.baianat.com.vanguard.views.editables

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import app.baianat.com.vanguard.views.GuardView
import app.baianat.com.vanguard.rules.TextValidationRule
import app.baianat.com.vanguard.validationCase.ValidationCase
import app.baianat.com.vanguard.rules.NotRequired
import app.baianat.com.vanguard.views.editables.viewStates.*
import app.baianat.com.vanguard.views.editables.viewDecorator.ViewDecorator
import app.baianat.com.vanguard.views.editables.viewDecorator.ViewDecoratorFactory
import app.baianat.com.vanguard.utils.print
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.Unregistrar

class TextValidationCase(internal var textCaseArgs: TextCaseArgs<View>) :
    ValidationCase<View, String>(textCaseArgs.caseArgs) {


    var viewDecorator: ViewDecorator = ViewDecoratorFactory.create(textCaseArgs)

    private val stateArgs = StateArgs(viewDecorator, this)
    var validState = ValidState(stateArgs)
    var inValidState = InvalidState(stateArgs)
    var initState = InitState(stateArgs)
    var serverPendingState = ServerPendingState(stateArgs)

    var textRulesHelper =
        TextRulesHelper(caseArgs.rulesToValidate as MutableList<TextValidationRule>)

    var state: BaseState = initState
    var lastState: BaseState = initState

    var needsFurtherValidation = textCaseArgs.onLocalValidationSuccess != null


    private val watcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) = Unit
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            state.onTextChanged(input.toString())
        }
    }

    private val focus = View.OnFocusChangeListener { _, hasFocus ->
        state.onFocusChanged(hasFocus)
    }


    // freshStart not to validate populated fields when user returns to the activity
    private var freshStart = true

    override fun attachListener() {
        viewDecorator.attachListeners(watcher, focus)

        if (freshStart) {
            handlePopulatedFields()
            freshStart = false
        }

    }


    private fun handlePopulatedFields() {
        if (valueToValidate.isBlank()) {
            return
        }
        (state as? InitState)?.validateInitialText()
    }

    override fun stopValidating() {
        viewDecorator.detachListeners(watcher)
    }

    fun areRulesValid(): Boolean {
        caseArgs.rulesToValidate.let { rules ->

            if (rules.contains(NotRequired)) {
                if (valueToValidate.isEmpty() || valueToValidate.isBlank()) {
                    return true
                }
            }

            rules.forEach { ruleToValidate ->
                val isValid = ruleToValidate.validate(valueToValidate)

                if (!isValid) {
                    return false
                }
            }
            return true
        }
    }

    var validationStatus = false
    override fun isThisCaseValid(): Boolean {
        return state.isValid()
    }

    override val valueToValidate: String
        get() = viewDecorator.getText()


    fun failValidation(errorMsg: String) {
        (state as? ServerPendingState)?.onServerFailure(errorMsg)
    }

    fun confirmValidation() {
        (state as? ServerPendingState)?.onServerSuccess()
    }

    fun highLightError() {
        state.apply {
            state.changeStateInto(BaseState.State.INVALID)
            (state as? InvalidState)?.highlightErrorsImmediately()
        }
    }

    fun handleKeypadClose() {

        if (viewDecorator.hasFocus) {
            state.onFocusChanged(false)
        }
    }


}
