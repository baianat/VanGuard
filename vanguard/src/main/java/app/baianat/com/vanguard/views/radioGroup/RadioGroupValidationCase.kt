package app.baianat.com.vanguard.views.radioGroup

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.core.view.forEach
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CaseArgs
import app.baianat.com.vanguard.validationCase.ValidationCase
import java.lang.Exception

class RadioGroupValidationCase(caseArgs: CaseArgs<RadioGroup, Int>) :
    ValidationCase<RadioGroup, Int>(caseArgs) {

    override val valueToValidate: Int
        get() = viewToValidate.checkedRadioButtonId


    private var listener = RadioGroup.OnCheckedChangeListener { group, checkedId ->

        internalListener?.onCheckedChanged(group, checkedId)
        checkValidationNow()
    }

    private var internalListener: RadioGroup.OnCheckedChangeListener? = null
    private fun defineInternalListener() {
        try {
            val field = RadioGroup::class.java.getDeclaredField("mOnCheckedChangeListener")
            field.isAccessible = true

            (field.get(viewToValidate) as? RadioGroup.OnCheckedChangeListener)?.apply {
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