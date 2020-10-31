package app.baianat.com.vanguard.rules

import app.baianat.com.vanguard.utils.print
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Checks if a string is in time format, can be used to validate both hours and date formats
 */
class IsTime(private var pattern:String, var errorMsg: String? = null, var helperMsg: String? = null):
    TextValidationRule(){

    override fun validate(valueToCompare: String): Boolean {
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        return try {
            format.parse(valueToCompare)
            true
        } catch (e: ParseException) {
            false
        }
    }

    override fun getErrorMessage() = errorMsg
    override fun getHelperMessage() = helperMsg
}

/**
 * Checks if a string is in time format, and if its before a reference date.
 */
class IsTimeBefore(private var referenceTime:String, private var pattern:String, var errorMsg: String? = null, var helperMsg: String? = null):
    TextValidationRule(){

    override fun validate(valueToCompare: String): Boolean {
        val format = SimpleDateFormat(pattern, Locale.getDefault())

        return try {
            val currentDate =    format.parse(valueToCompare)
            val referenceDate =    format.parse(referenceTime)

            currentDate?.before(referenceDate)==true
        } catch (e: ParseException) {
            false
        }
    }

    override fun getErrorMessage() = errorMsg
    override fun getHelperMessage() = helperMsg
}


/**
 * Checks if a string is in time format, and if its after a reference date.
 */
class IsTimeAfter(private var referenceTime:String, private var pattern:String, var errorMsg: String? = null, var helperMsg: String? = null):
    TextValidationRule(){

    override fun validate(valueToCompare: String): Boolean {
        val format = SimpleDateFormat(pattern, Locale.getDefault())

        return try {
            val currentDate =    format.parse(valueToCompare)
            val referenceDate =    format.parse(referenceTime)

            currentDate?.after(referenceDate)==true
        } catch (e: ParseException) {
            false
        }
    }

    override fun getErrorMessage() = errorMsg
    override fun getHelperMessage() = helperMsg
}