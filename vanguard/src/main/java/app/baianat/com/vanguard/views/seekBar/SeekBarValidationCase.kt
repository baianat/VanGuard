package app.baianat.com.vanguard.views.seekBar

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import app.baianat.com.vanguard.utils.print
import app.baianat.com.vanguard.validationCase.CaseArgs
import app.baianat.com.vanguard.validationCase.ValidationCase
import java.lang.Exception

class SeekBarValidationCase(caseArgs: CaseArgs<SeekBar, Int>) :
    ValidationCase<SeekBar, Int>(caseArgs) {


    override val valueToValidate: Int
        get() = viewToValidate.progress


    private var listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            internalListener?.onProgressChanged(seekBar, progress, fromUser)
            checkValidationNow()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            internalListener?.onStartTrackingTouch(seekBar)
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            internalListener?.onStopTrackingTouch(seekBar)
        }

    }

    private var internalListener: SeekBar.OnSeekBarChangeListener? = null
    private fun defineInternalListener() {
        try {
            val field = SeekBar::class.java.getDeclaredField("mOnSeekBarChangeListener")
            field.isAccessible = true

            (field.get(viewToValidate) as? SeekBar.OnSeekBarChangeListener)?.apply {
                internalListener = this
            }
        } catch (e: Exception) {
            e.message?.print()
        }

    }

    override fun attachListener() {
        defineInternalListener()
        viewToValidate.setOnSeekBarChangeListener(listener)
    }

    override fun stopValidating() {
        viewToValidate.setOnSeekBarChangeListener(null)
    }

}