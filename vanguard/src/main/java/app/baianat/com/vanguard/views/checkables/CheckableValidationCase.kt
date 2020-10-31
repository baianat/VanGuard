package app.baianat.com.vanguard.views.checkables

import android.widget.CompoundButton
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CaseArgs
import app.baianat.com.vanguard.validationCase.ValidationCase
import java.lang.Exception

class CheckableValidationCase(
    caseArgs: CaseArgs<CompoundButton, Boolean>
) : ValidationCase<CompoundButton, Boolean>(caseArgs) {

    override val valueToValidate: Boolean
        get() = viewToValidate.isChecked

    private var listener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        checkValidationNow()
        internalListener?.onCheckedChanged(buttonView, isChecked)
    }

    private var internalListener: CompoundButton.OnCheckedChangeListener? = null
    private fun defineInternalListener() {
        try {
            val field = CompoundButton::class.java.getDeclaredField("mOnCheckedChangeListener")
            field.isAccessible = true

            (field.get(viewToValidate) as? CompoundButton.OnCheckedChangeListener)?.apply {
                internalListener = this
            }
        } catch (e: Exception) {
            e.message?.print()
        }

    }


    override fun attachListener() {
        defineInternalListener()
        viewToValidate.setOnCheckedChangeListener(listener)
    }


    override fun stopValidating() {
        viewToValidate.setOnCheckedChangeListener(null)

    }


}