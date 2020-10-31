package app.baianat.com.vanguard.views.spinner

import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.Spinner
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CaseArgs
import app.baianat.com.vanguard.validationCase.ValidationCase
import java.lang.Exception

class SpinnerValidationCase(
    caseArgs: CaseArgs<Spinner, Int>
) : ValidationCase<Spinner, Int>(caseArgs) {

    override val valueToValidate: Int
        get() = viewToValidate.selectedItemPosition

    private var listener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
            internalListener?.onNothingSelected(parent)
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            internalListener?.onItemSelected(parent, view, position, id)
            checkValidationNow()
        }

    }

    private var internalListener: AdapterView.OnItemSelectedListener? = null
    private fun defineInternalListener() {
        try {
            val field = AdapterView::class.java.getDeclaredField("mOnItemSelectedListener")
            field.isAccessible = true

            (field.get(viewToValidate) as? AdapterView.OnItemSelectedListener)?.apply {
                internalListener = this
            }
        } catch (e: Exception) {
            e.message?.print()
        }

    }

    override fun attachListener() {
        defineInternalListener()
        viewToValidate.onItemSelectedListener = listener
    }

    override fun stopValidating() {
        viewToValidate.onItemSelectedListener = null
    }




}