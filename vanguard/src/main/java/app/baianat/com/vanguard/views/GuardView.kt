package app.baianat.com.vanguard.views

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import app.baianat.com.vanguard.R
import app.baianat.com.vanguard.utils.print
import com.google.android.material.textfield.TextInputLayout
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.Recolor
import com.transitionseverywhere.extra.Scale
import kotlinx.android.synthetic.main.guard_view.view.*

// todo : to be optimized
class GuardView : ConstraintLayout {


    constructor(context: Context) : this(context, null)


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }


    internal fun stopWatching(watcher: TextWatcher) {
        editText.removeTextChangedListener(watcher)
        editText.onFocusChangeListener = null
    }


    internal fun startWatching(watcher: TextWatcher, focus: OnFocusChangeListener) {

        editText.addTextChangedListener(watcher)
        editText.onFocusChangeListener = focus
    }


    fun addTextChangedListener(watcher: TextWatcher) {
        editText.addTextChangedListener(watcher)
    }

    fun removeTextChangedListener(watcher: TextWatcher) {
        editText.removeTextChangedListener(watcher)
    }


    private var errorColor = Color.RED
    private var errorMessage = ""
    private var helperMessage = ""
    private var helperColor = Color.GRAY
    private var defaultColor = Color.GRAY
    private var errorSign = ContextCompat.getDrawable(this.context,
        R.drawable.ic_invalid
    )
    private var validSign = ContextCompat.getDrawable(this.context,
        R.drawable.ic_valid
    )
    private var highlightColor = ContextCompat.getColor(context,
        R.color.colorAccent
    )
    private var validationIconsAreEnabled = true

    private var backgroundMode = OUTLINED_FIELD


    private fun init(attrs: AttributeSet?) {
        val view = inflate(context, R.layout.guard_view, this).apply {
            val userAttrs = context.obtainStyledAttributes(attrs,
                R.styleable.GuardView
            )

//            backgroundMode = userAttrs.getInt(R.styleable.GuardView_backgroundMode, 1)
            backgroundMode = OUTLINED_FIELD

            backgroundMode.let { backgroundMode ->
                editTextContainer.setBoxBackgroundMode(backgroundMode)
            }

            val hint = userAttrs.getString(R.styleable.GuardView_hint)
            val hintColor = userAttrs.getColor(R.styleable.GuardView_hintColor, Color.GRAY)
            setupHint(hintColor, hint)

            helperMessage = userAttrs.getString(R.styleable.GuardView_helperMsg) ?: ""
            helperColor = userAttrs.getColor(R.styleable.GuardView_helperColor, Color.GRAY)
            auxTextView.setTextColor(helperColor)

            errorMessage = userAttrs.getString(R.styleable.GuardView_errorMsg) ?: ""
            errorColor = userAttrs.getColor(R.styleable.GuardView_errorColor, Color.RED)

            defaultColor = userAttrs.getColor(R.styleable.GuardView_borderColor, Color.GRAY)

            highlightColor = userAttrs.getColor(
                R.styleable.GuardView_highlightColor,
                ContextCompat.getColor(context, R.color.colorAccent)
            )

            setFocusedColor(highlightColor)
            setUnfocusedColor(defaultColor)

            userAttrs.getDrawable(R.styleable.GuardView_errorIcon)?.apply {
                errorSign = this
            }
            userAttrs.getDrawable(R.styleable.GuardView_validIcon)?.apply {
                validSign = this
            }

            isEditable = userAttrs.getBoolean(R.styleable.GuardView_editable, true)

            validationIconsAreEnabled =
                userAttrs.getBoolean(R.styleable.GuardView_showValidationIcon, true)

            val text = userAttrs.getString(R.styleable.GuardView_text)
            if (text?.isNotEmpty() == true) editText.setText(text, TextView.BufferType.EDITABLE)

            userAttrs.getColor(R.styleable.GuardView_textColor, -1)
                .takeIf { textColor -> textColor != -1 }?.apply {
                    editText.setTextColor(this)
                }

            tintProgressBar()

            setupInputType(userAttrs)
            setupImageConstraint(userAttrs)
            setupRadius(userAttrs)

            userAttrs.recycle()

            TransitionManager.beginDelayedTransition(editTextContainer, TransitionSet().apply {
                addTransition(Recolor().apply {
                    duration = 50
                })
                addTransition(ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_IN))
                duration = 100
            })
        }


    }


    private fun setupRadius(userAttrs: TypedArray?) {
        if (backgroundMode == UNDERLINED_FIELD) return

        val defaultValue = 12f
        val radiusTopLeft =
            userAttrs?.getDimension(R.styleable.GuardView_radiusTopLeft, defaultValue)
                ?: defaultValue
        val radiusTopRight =
            userAttrs?.getDimension(R.styleable.GuardView_radiusTopRight, defaultValue)
                ?: defaultValue
        val radiusBottomLeft =
            userAttrs?.getDimension(R.styleable.GuardView_radiusBottomLeft, defaultValue)
                ?: defaultValue
        val radiusBottomRight =
            userAttrs?.getDimension(R.styleable.GuardView_radiusBottomRight, defaultValue)
                ?: defaultValue

        editTextContainer.setBoxCornerRadii(
            radiusTopLeft,
            radiusTopRight,
            radiusBottomLeft,
            radiusBottomRight
        )
    }

    private fun setupImageConstraint(userAttrs: TypedArray) {
        if (backgroundMode == OUTLINED_FIELD) {
            userAttrs.getFloat(R.styleable.GuardView_iconOffset, .9f).apply {
                val params = outlineIndicator.layoutParams as ConstraintLayout.LayoutParams
                params.horizontalBias = this
                outlineIndicator.layoutParams = params
            }

            userAttrs.getInt(R.styleable.GuardView_iconPosition, 1).apply {
                val constraintSet = ConstraintSet()
                constraintSet.clone(parentContainer)
                when (this) {
                    1 -> {
                        constraintSet.connect(
                            R.id.outlineIndicator,
                            ConstraintSet.TOP,
                            R.id.editTextContainer,
                            ConstraintSet.TOP,
                            0
                        )
                        constraintSet.connect(
                            R.id.outlineIndicator,
                            ConstraintSet.BOTTOM,
                            R.id.editTextContainer,
                            ConstraintSet.TOP,
                            0
                        )
                    }
                    2 -> {
                        constraintSet.connect(
                            R.id.outlineIndicator,
                            ConstraintSet.BOTTOM,
                            R.id.editTextContainer,
                            ConstraintSet.BOTTOM,
                            0
                        )
                        constraintSet.connect(
                            R.id.outlineIndicator,
                            ConstraintSet.BOTTOM,
                            R.id.editTextContainer,
                            ConstraintSet.BOTTOM,
                            0
                        )
                    }
                }
                constraintSet.applyTo(parentContainer)

            }


        } else {
            val params = editText.layoutParams as FrameLayout.LayoutParams

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.marginEnd = convertPixelsToDp(16)
            }
            editText.layoutParams = params
        }
    }

    private fun convertPixelsToDp(px: Int): Int {
        return (px * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    private fun setupInputType(userAttrs: TypedArray) {
        val inputType = userAttrs.getInt(R.styleable.GuardView_inputType, 0)
        when (inputType) {
            1 -> editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            2 -> editText.inputType = InputType.TYPE_CLASS_NUMBER
            3 -> {
                editText.inputType =
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                userAttrs.getBoolean(R.styleable.GuardView_showPassToggle, true).takeIf { it }
                    .apply {
                        editTextContainer.isPasswordVisibilityToggleEnabled = true
                        userAttrs.getDrawable(R.styleable.GuardView_togglePasswordIcon)?.apply {
                            editTextContainer.passwordVisibilityToggleDrawable = this
                        } ?: run {
                            editTextContainer.setPasswordVisibilityToggleTintList(
                                getPasswordToggleTint()
                            )
                        }
                    }
            }
            4 -> editText.inputType = InputType.TYPE_CLASS_PHONE
        }
    }


    var text
        get() = editText.text.toString()
        set(value) = editText.setText(value, TextView.BufferType.EDITABLE)

    internal var isEditable: Boolean
        get() = editText.isFocusable
        set(value) {
            editText.isFocusable = value
        }

    private fun getPasswordToggleTint(): ColorStateList {
        return ColorStateList.valueOf(highlightColor)
    }


    private fun setupHint(hintColor: Int, hint: String?) {
        val hintStates = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused)
        )
        val hintColors = intArrayOf(hintColor, Color.GRAY)
        val hintStatesColor = ColorStateList(hintStates, hintColors)

        editTextContainer.defaultHintTextColor = hintStatesColor
        editTextContainer.hint = hint
    }


    private var isErrorMSgVisible = false

    internal fun setErrorMsg(errorMsg: String) {

        isErrorMSgVisible = true

        TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {
            addTransition(ChangeText().apply {
                changeBehavior = ChangeText.CHANGE_BEHAVIOR_IN
                duration = 300
                addTarget(auxTextView)
            })
        })

        if (errorMsg.isNotEmpty()) {
            auxTextView.text = errorMsg
            errorMessage = ""
        } else {
            auxTextView.text = errorMessage
        }

        auxTextView.apply {
            visibility = View.VISIBLE
            setTextColor(errorColor)
        }
    }


    internal fun clearErrorMsg() {
        if (isErrorMSgVisible) {

            TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {
                addTransition(ChangeText().apply {
                    changeBehavior = ChangeText.CHANGE_BEHAVIOR_IN
                    duration = 300
                    addTarget(auxTextView)
                })
            })

            auxTextView.apply {
                visibility = View.INVISIBLE

                // helper color is restored here and not while the helper text is set, this is because
                // while the text is in error state, helper text should be in error color
                setTextColor(helperColor)

            }
            isErrorMSgVisible = false
        }
    }


    private var isHelperMsgVisible = false

    internal fun setHelperMsg(helperMsg: String) {
        isHelperMsgVisible = true
        if (helperMsg.isNotEmpty()) {
            auxTextView.text = helperMsg
        } else {
            auxTextView.text = helperMessage
        }
        TransitionManager.beginDelayedTransition(editTextContainer, Recolor().apply {
            duration = 150
        })
        auxTextView.apply {
            visibility = View.VISIBLE
        }
    }

    internal fun clearHelperMsg() {
        if (isHelperMsgVisible) {
            TransitionManager.beginDelayedTransition(parentContainer, ChangeText().apply {
                changeBehavior = ChangeText.CHANGE_BEHAVIOR_IN
                duration = 300
                addTarget(auxTextView)
            })
            auxTextView.text = ""

            isHelperMsgVisible = false
        }
    }


    internal fun showValidIcon() {
        if (validationIconsAreEnabled) {

            val activeView = when (backgroundMode) {
                OUTLINED_FIELD -> outlineIndicator
                UNDERLINED_FIELD -> underlineIndicator
                else -> return
            }

            activeView.background = validSign
            TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {
                addTransition(Fade().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
                addTransition(Scale().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
            })
            activeView.visibility = View.VISIBLE
        }
    }

    internal fun hideValidIcon() {
        if (validationIconsAreEnabled) {

            val activeView = when (backgroundMode) {
                OUTLINED_FIELD -> outlineIndicator
                UNDERLINED_FIELD -> underlineIndicator
                else -> return
            }
            TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {
                addTransition(Fade().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
                addTransition(Scale().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
            })
            activeView.visibility = View.INVISIBLE

        }
    }

    private var isErrorIconVisible = false
    internal fun showErrorIcon() {

        if (validationIconsAreEnabled || !isErrorIconVisible) {

            val activeView = when (backgroundMode) {
                OUTLINED_FIELD -> outlineIndicator
                UNDERLINED_FIELD -> underlineIndicator
                else -> return
            }

            activeView.background = errorSign

            TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {

                addTransition(Fade().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
                addTransition(Scale().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })

            })

            activeView.visibility = View.VISIBLE
            isErrorIconVisible = true
        }
    }

    internal fun hideErrorIcon() {

        if (validationIconsAreEnabled && isErrorIconVisible) {

            val activeView = when (backgroundMode) {
                OUTLINED_FIELD -> outlineIndicator
                UNDERLINED_FIELD -> underlineIndicator
                else -> return
            }

            TransitionManager.beginDelayedTransition(parentContainer, TransitionSet().apply {
                addTransition(Fade().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
                addTransition(Scale().apply {
                    interpolator = OvershootInterpolator()
                    duration = 250
                    addTarget(activeView)
                })
            })

            activeView.visibility = View.INVISIBLE
            isErrorIconVisible = false
        }
    }


    internal fun changeStrokeColorToError() {
        refreshChanges()
        if (editText.hasFocus()) {
            setFocusedColor(errorColor)
        } else {
            setUnfocusedColor(errorColor)
        }
    }

    internal fun restoreStrokeColor() {
        when (backgroundMode) {
            UNDERLINED_FIELD -> {
                setUnfocusedUnderlineColor(defaultColor)
                setFocusedUnderlineColor(highlightColor)
            }
            OUTLINED_FIELD -> {
                setFocusedBoxColor(highlightColor)
                setUnfocusedBoxColor(defaultColor)
            }
        }
    }


    private fun setUnfocusedColor(color: Int) {
        when (backgroundMode) {
            UNDERLINED_FIELD -> setUnfocusedUnderlineColor(color)
            OUTLINED_FIELD -> setUnfocusedBoxColor(color)
        }
    }


    private fun setUnfocusedBoxColor(color: Int) {
        try {
            val field = TextInputLayout::class.java.getDeclaredField("defaultStrokeColor")
            field.isAccessible = true
            field.set(
                editTextContainer, color
            )
        } catch (e: NoSuchFieldException) {
            ("Failed to change box color, item might look wrong").print()
        } catch (e: IllegalAccessException) {
            ("Failed to change box color, item might look wrong").print()
        }

    }

    private fun setUnfocusedUnderlineColor(color: Int) {
        val focusedStates = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused)
        )
        val focusedColors = intArrayOf(highlightColor, color)
        ViewCompat.setBackgroundTintList(editText, ColorStateList(focusedStates, focusedColors))
    }

    private fun setFocusedColor(color: Int) {
        when (backgroundMode) {
            UNDERLINED_FIELD -> setFocusedUnderlineColor(color)
            OUTLINED_FIELD -> setFocusedBoxColor(color)
        }
    }

    private fun setFocusedBoxColor(color: Int) {
        TransitionManager.beginDelayedTransition(parentContainer, Recolor().apply {
            duration = 250
        })
        editTextContainer.boxStrokeColor = color
    }

    internal fun refreshChanges() {
        editTextContainer.boxStrokeColor = defaultColor
    }


    private fun setFocusedUnderlineColor(color: Int) {
        val focusedStates = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused)
        )
        val focusedColors = intArrayOf(color, defaultColor)
        ViewCompat.setBackgroundTintList(editText, ColorStateList(focusedStates, focusedColors))
    }

    internal fun keepErrorState() {
        when (backgroundMode) {
            OUTLINED_FIELD -> setFocusedBoxColor(errorColor)
            UNDERLINED_FIELD -> {
                val focusedStates = arrayOf(
                    intArrayOf(android.R.attr.state_focused),
                    intArrayOf(-android.R.attr.state_focused)
                )
                val focusedColors = intArrayOf(errorColor, errorColor)
                ViewCompat.setBackgroundTintList(
                    editText,
                    ColorStateList(focusedStates, focusedColors)
                )
            }
        }
    }

    internal fun showLoader() {
        progressBar.visibility = View.VISIBLE
        editText.isEnabled = false
    }

    internal fun hideLoader() {
        TransitionManager.beginDelayedTransition(parentContainer)
        progressBar.visibility = View.INVISIBLE
        editText.isEnabled = true
    }


    private fun tintProgressBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val wrapDrawable = DrawableCompat.wrap(progressBar.indeterminateDrawable)
            DrawableCompat.setTint(wrapDrawable, highlightColor)
            progressBar.indeterminateDrawable = DrawableCompat.unwrap<Drawable>(wrapDrawable)
        } else {
            progressBar.indeterminateDrawable.setColorFilter(
                highlightColor,
                PorterDuff.Mode.SRC_IN
            )
        }
    }

    internal fun focusDefocus() {
        editText.requestFocus()
        editText.clearFocus()
    }

    internal fun setAsStatic() {
        isEditable = false
        editText.isClickable = true
    }

    companion object {
        const val OUTLINED_FIELD = 2
        const val UNDERLINED_FIELD = 1
    }

    override fun setOnClickListener(l: OnClickListener?) {
        editText.setOnClickListener(l)
        auxTextView.setOnClickListener(l)
    }

    override fun setOnLongClickListener(l: OnLongClickListener?) {
        editText.setOnLongClickListener(l)
        auxTextView.setOnLongClickListener(l)
    }

    fun unfocusedLoader() {

        if (backgroundMode == OUTLINED_FIELD) {
            setUnfocusedColor(errorColor)
            setFocusedColor(errorColor)
        }

        val focusedStates = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(-android.R.attr.state_focused)
        )

        val focusedColors = intArrayOf(errorColor, errorColor)
        ViewCompat.setBackgroundTintList(editText, ColorStateList(focusedStates, focusedColors))

        Handler().postDelayed({
            parentContainer.requestLayout()

        }, 200)

    }
}