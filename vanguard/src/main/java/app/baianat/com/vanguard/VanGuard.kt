package app.baianat.com.vanguard

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import app.baianat.com.vanguard.views.editables.TextValidationCase
import app.baianat.com.vanguard.validationCase.ValidationCase
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class VanGuard internal constructor(var activity: Activity) : LifecycleObserver {

    private var isValidationStarted = false

    private val casesToValidate = ArrayList<ValidationCase<*, *>?>()
    
    private var formStatusChangeCallback: ((Boolean) -> Unit)? = null
    
    fun validate(vararg validationCases: ValidationCase<*, *>?): VanGuard {

        // clear the previous cases, and validate the newly created ones
        // because cases can't be created as top level properties, so they will be recreated each time activity of fragment is started

        casesToValidate.clear()
        casesToValidate.addAll(validationCases)
        return this
    }

    fun addCase(caseToAdd: ValidationCase<*, *>) {
        casesToValidate.add(caseToAdd)
    }

    fun removeCase(caseToRemove: ValidationCase<*, *>) {
        casesToValidate.remove(caseToRemove)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startValidation() {
        if (!isValidationStarted) {
            isValidationStarted = true
            casesToValidate.forEach {
                it?.startValidating(onValidationConfirmed)
            }
        }
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun attachKeypadListener() {
        KeyboardVisibilityEvent.setEventListener(
            activity
            , keypadVisibilityEventListener
        )
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopValidation() {
        if (isValidationStarted) {

            isValidationStarted = false
            casesToValidate.forEach {
                it?.stopValidating()
            }
        }
    }


    private var keypadVisibilityEventListener = KeyboardVisibilityEventListener { isKeypadVisible ->
        if (!isKeypadVisible) {
            casesToValidate.forEach {
                (it as? TextValidationCase)?.apply {
                    handleKeypadClose()
                }
            }
        }
    }

    /**
     * function to be invoked whenever the state of any view changes,
     * when the status of a view changes, the validator loop over all the views to check their current status..
     * Then the form callbacks are fired
     **/


    private var onValidationConfirmed: (ValidationCase<*, *>) -> Unit = { caseConfirmed ->

        if (!caseConfirmed.isThisCaseValid()) {
            onFormValidationFail()
        } else {
            var formIsValid = true
            run breaker@{

                casesToValidate.forEach { case ->

                    if (case !== caseConfirmed) {
                        if (case?.isThisCaseValid() == false) {
                            formIsValid = false
                            return@breaker
                        }
                    }
                }
            }

            if (formIsValid) {
                onFormValidationSuccess()
            } else {
                onFormValidationFail()
            }
        }

    }

    private var isFormValidated = false

    private fun onFormValidationFail() {
        if (isFormValidated) {
            formStatusChangeCallback?.invoke(false)
            isFormValidated = false
        }
    }

    private fun onFormValidationSuccess() {
        formStatusChangeCallback?.invoke(true)
        isFormValidated = true
    }


    fun doOnFormStatusChange(whatToDo: (Boolean) -> Unit) {
        formStatusChangeCallback = whatToDo
    }

    fun doIfFormIsValid(highlightErrors: Boolean = true, whatToDo: () -> Unit) {

        if (casesToValidate.isEmpty()) return


        if (isFormValid()) {
            whatToDo()
        } else {
            if (highlightErrors) {
                highLightErrors()
            }
        }
    }

    fun isFormValid(): Boolean {

        if (casesToValidate.isEmpty()) return true

        var formIsValid = true
        run breaker@{
            casesToValidate.forEach { case ->
                if (case?.isThisCaseValid() == false) {
                    formIsValid = false
                    return@breaker
                }
            }
        }
        return formIsValid
    }

    fun highLightErrors() {
        casesToValidate.forEach {
            (it as? TextValidationCase)?.takeUnless { case ->
                case.areRulesValid()
            }?.highLightError()
        }
    }

    
    /**
     * Creator Functions
     */

    companion object {

        fun of(fragment: Fragment): VanGuard? {
            return of(fragment, fragment::class.toString())
        }

        fun of(activity: AppCompatActivity): VanGuard? {
            return of(activity, activity::class.toString())
        }

        private var validatorManager = mutableMapOf<String, VanGuard>()

        private fun of(lifecycleOwner: LifecycleOwner, tag: String): VanGuard? {
            return VanGuardFactory.create(lifecycleOwner, tag, validatorManager)
        }

    }


}
