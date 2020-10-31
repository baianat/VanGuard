package app.baianat.com.vanguard.validationCase

import android.view.View
import android.widget.*
import app.baianat.com.vanguard.*
import app.baianat.com.vanguard.views.radioGroup.RadioGroupValidationCase
import app.baianat.com.vanguard.views.seekBar.SeekBarValidationCase
import app.baianat.com.vanguard.views.spinner.SpinnerValidationCase
import app.baianat.com.vanguard.views.checkables.CheckableValidationCase
import app.baianat.com.vanguard.views.editables.TextCaseArgs
import app.baianat.com.vanguard.rules.NotRequired
import app.baianat.com.vanguard.views.editables.TextValidationCase
import app.baianat.com.vanguard.rules.ValidationRule
import app.baianat.com.vanguard.utils.GuardHelper
import app.baianat.com.vanguard.views.GuardView


class CasesFactory private constructor() {

    companion object {

        fun watch(view: SeekBar): ForSeekBar {
            return ForSeekBar().apply {
                watch(view)
            }
        }

        fun watch(view: RadioGroup): ForRadioGroup {
            return ForRadioGroup().apply {
                watch(view)
            }
        }

        fun watch(view: Spinner): ForSpinner {
            return ForSpinner().apply {
                watch(view)
            }
        }

        fun watch(view: TextView): ForEditable {
            return ForEditable().apply {
                watch(view)
            }
        }

        fun watch(view: GuardView): ForEditable {
            return ForEditable().apply {
                watch(view)
            }
        }

        fun watch(view: CompoundButton): ForCheckable {
            return ForCheckable().apply {
                watch(view)
            }
        }

    }

}

abstract class Factory<V : View, T> internal constructor() {

    var caseArgs = CaseArgs<V, T>()

    open fun watch(viewToWatch: V) = apply {
        caseArgs.viewToWatch = viewToWatch
    }

    open fun forRule(rule: ValidationRule<T>) = apply {
        caseArgs.rulesToValidate.add(rule)
    }

    open fun doOnValidationStatusChange(whatToDo: ((isValid: Boolean, view: View) -> Unit)) =
        apply {
            caseArgs.stateChangeCallback = whatToDo
        }

    abstract val caseToBuild: ValidationCase<*, *>


    @Throws(IllegalStateException::class)
    open fun create(): ValidationCase<*, *>? {
        return if (caseArgs.shouldBuildCase()) {
            caseToBuild
        } else {
            null
        }
    }

}

class ForCheckable internal constructor() : Factory<CompoundButton, Boolean>() {

    override val caseToBuild: CheckableValidationCase
        get() = CheckableValidationCase(caseArgs)
}

class ForRadioGroup internal constructor() : Factory<RadioGroup, Int>() {

    override val caseToBuild: RadioGroupValidationCase
        get() = RadioGroupValidationCase(caseArgs)

}

class ForSpinner internal constructor() : Factory<Spinner, Int>() {
    override val caseToBuild: SpinnerValidationCase
        get() = SpinnerValidationCase(caseArgs)
}

class ForSeekBar internal constructor() : Factory<SeekBar, Int>() {
    override val caseToBuild: SeekBarValidationCase
        get() = SeekBarValidationCase(caseArgs)
}

class ForEditable internal constructor() : Factory<View, String>() {

    private var textCaseArgs =
        TextCaseArgs(caseArgs = caseArgs)

    fun doOnLocalValidationSuccess(
        animateOnPending: Boolean = true,
        whatToDo: (textToFurtherValidate: String, validationCase: TextValidationCase) -> Unit
    ) =
        apply {
            textCaseArgs.onLocalValidationSuccess = whatToDo
            textCaseArgs.animateOnPending = animateOnPending
        }

    override fun forRule(rule: ValidationRule<String>) = apply {
        super.forRule(rule)
    }

    override fun watch(viewToWatch: View) = apply {
        super.watch(viewToWatch)
    }

    override fun doOnValidationStatusChange(whatToDo: (isValid: Boolean, view: View) -> Unit) =
        apply {
            super.doOnValidationStatusChange(whatToDo)
        }

    override val caseToBuild: TextValidationCase
        get() = TextValidationCase(textCaseArgs)

    override fun create(): ValidationCase<*, *>? {
        return if (textCaseArgs.shouldBuildCase()) {
            caseToBuild
        } else {
            null
        }
    }

    @Throws(IllegalStateException::class)
    fun asStatic() = apply {
        textCaseArgs.handleStaticCase()
    }

    fun notRequired() = apply {
        caseArgs.rulesToValidate.add(NotRequired)
    }

}