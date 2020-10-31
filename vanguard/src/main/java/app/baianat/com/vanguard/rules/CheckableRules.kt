package app.baianat.com.vanguard.rules

/**
 * Rule to be used on Switch or CheckBox to validate if they are checked.
 */
class IsChecked : WidgetValidationRule<Boolean>() {
    override fun validate(valueToCompare: Boolean): Boolean {
        return valueToCompare
    }
}

/**
 * Rule to be used on Switch or CheckBox to validate if they are not checked.
 */
class IsNotChecked : WidgetValidationRule<Boolean>() {
    override fun validate(valueToCompare: Boolean): Boolean {
        return !valueToCompare
    }
}