package app.baianat.com.vanguard.rules

import androidx.annotation.IdRes

/**
 * Rule to be used on RadioGroup to ensure one child is at least checked.
 */
class IsOneChecked : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        if (valueToCompare != -1) {
            return true
        }
        return false
    }
}

/**
 * Rule to be used on RadioGroup to check if a view with a particular id is checked.
 */
class IsThisOneChecked(@IdRes var referenceViewId: Int) : WidgetValidationRule<Int>() {
    override fun validate(valueToCompare: Int): Boolean {
        if (valueToCompare == referenceViewId) {
            return true
        }
        return false
    }
}