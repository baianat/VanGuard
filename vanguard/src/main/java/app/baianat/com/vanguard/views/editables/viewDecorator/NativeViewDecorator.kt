package app.baianat.com.vanguard.views.editables.viewDecorator

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import app.baianat.com.vanguard.utils.print
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NativeViewDecorator(var view: TextView, var animateWhilePending: Boolean = true) :
    ViewDecorator {
    override var identifier: String
        get() = view.tag as? String ?:  "nom"
        set(value) {}


    init {
        handleTextInputHelper()
    }


    override var hasTextInitially: Boolean=false

    override var hasFocus: Boolean
        get() = view.hasFocus()
        set(value) {}

    /**
     * Save and hide helper text if defined in XML and save it..
     * to show it when focus is gained to maintain consistency
     */
    private var tempHelperText: String? = null


    private fun isTextInputEditText() = view.parent?.parent is TextInputLayout
    private fun textInputLayout(): TextInputLayout? = view.parent?.parent as? TextInputLayout


    private fun handleTextInputHelper() {
        if (!(textInputLayout())?.helperText.isNullOrBlank()) {
            tempHelperText = (textInputLayout())?.helperText.toString()
            (textInputLayout())?.helperText = ""
        }
    }

    override fun isStatic(): Boolean {
        return (view !is EditText || !view.isEnabled)
    }

    override fun invalidateStaticField(errorMsg: String) {
        updateErrorMsg(errorMsg)
    }

    override fun setHelper(helperMsg: String) {


        textInputLayout()?.takeIf { helperMsg.isNotEmpty() }
            ?.apply {
                helperText = helperMsg
            } ?: textInputLayout()?.takeIf { tempHelperText?.isBlank() == false }
            ?.apply {
                helperText = tempHelperText
            } ?: apply {
            textInputLayout()?.helperText = ""
        }
//        if (isTextInputEditText()) {
//            (textInputLayout())?.helperText =  if (helperMsg.isNotEmpty()) {
//                helperMsg
//            } else {
//                if (tempHelperText?.isBlank()==false){
//                    tempHelperText
//                }else{
//                    ""
//                }
//            }
//        }
    }

    override fun clearHelper() {
//        if (isTextInputEditText()) {
        (textInputLayout())?.helperText = ""
//        }
    }


    override fun attachListeners(watcher: TextWatcher, focus: View.OnFocusChangeListener) {
        view.addTextChangedListener(watcher)
        view.onFocusChangeListener = focus
    }

    override fun detachListeners(watcher: TextWatcher) {
        view.removeTextChangedListener(watcher)
        view.onFocusChangeListener = null
    }


    override fun setError(errorMsg: String) {
        updateErrorMsg(errorMsg)
    }

    override fun clearError() {
        updateErrorMsg("")
    }

    override fun showServerLoader() {
        "show server loader".print()
        if (animateWhilePending) {
            fadeWhileLoading()
        }
    }

    override fun hideServerLoader() {
        "hide server loader".print()
        stopFading()
    }


    private var runningAnimator: ObjectAnimator? = null

    private fun fadeWhileLoading() {
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, .95f)

        runningAnimator?.cancel()
        runningAnimator = ObjectAnimator.ofPropertyValuesHolder(view, alpha, scaleY).apply {
            repeatCount = ValueAnimator.INFINITE
            duration = 850
            startDelay = 100
            repeatMode = ValueAnimator.REVERSE
            start()
        }

        view.isEnabled = false
    }

    private fun stopFading() {

        view.isEnabled = true
        runningAnimator?.cancel()
        view.animate()?.scaleY(1f)?.alpha(1f)?.apply {
            startDelay = 200
        }?.start()

        runningAnimator = null
    }

    private fun updateErrorMsg(errorMsg: String?) {
        if (view is TextInputEditText) {
            if (isTextInputEditText()) {
                (textInputLayout())?.error = if (errorMsg.isNullOrEmpty()) {
                    null
                } else {
                    errorMsg
                }
            } else {
                (view as TextInputEditText).error = if (errorMsg.isNullOrEmpty()) {
                    null
                } else {
                    errorMsg
                }
            }
        } else {
            view.error = if (errorMsg.isNullOrEmpty()) {
                null
            } else {
                errorMsg
            }
        }
    }

    override fun getText(): String {
        return view.text.toString()
    }


}

