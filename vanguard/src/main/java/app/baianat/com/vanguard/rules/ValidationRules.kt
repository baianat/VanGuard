package app.baianat.com.vanguard.rules


/**
 * TextValidationRule class : is responsible for providing the rule to verify against
 */
abstract class TextValidationRule : ValidationRule<String> {

    abstract override fun validate(valueToCompare: String): Boolean

    open fun getErrorMessage(): String? = null

    open fun getHelperMessage(): String? = null

}

abstract class WidgetValidationRule<T> : ValidationRule<T>

interface ValidationRule<T> {

    fun validate(valueToCompare: T): Boolean

}

