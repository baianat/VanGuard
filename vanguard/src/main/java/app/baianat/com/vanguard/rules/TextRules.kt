package app.baianat.com.vanguard.rules

import android.util.Patterns

class IsLengthGreaterThan(
    private var length: Int,
    private var errorMsg: String? = null,
    var helperMsg: String? = null
) :
    TextValidationRule() {


    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare.length > length
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsEmail(var errorMsg: String? = "", var helperMsg: String? = "") : TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(valueToCompare).matches()
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsNotEmpty(var errorMsg: String? = "", var helperMsg: String? = "") : TextValidationRule() {

    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare.isNotEmpty() && valueToCompare.isNotBlank()
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsPhone(var errorMsg: String? = "", var helperMsg: String? = "") : TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return Patterns.PHONE.matcher(valueToCompare).matches()
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsIdenticalTo(var referenceString: String, var errorMsg: String? = "", var helperMsg: String? = "") :
    TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare == referenceString
        }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsNumberEqual(var referenceNumber: Int, var errorMsg: String? = "", var helperMsg: String? = "") :
    TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare.toIntOrNull()?.compareTo(referenceNumber)?.compareTo(0) == 0
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsNumberLessThan(private var referenceNumber: Int, var errorMsg: String? = "", var helperMsg: String? = "") :
    TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare.toIntOrNull()?.compareTo(referenceNumber)?.compareTo(0) ?: 1 < 0
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

class IsNumberGreaterThan(private var referenceNumber: Int, var errorMsg: String? = "", var helperMsg: String? = "") :
    TextValidationRule() {
    override fun validate(valueToCompare: String): Boolean {
        return valueToCompare.toIntOrNull()?.compareTo(referenceNumber)?.compareTo(0) ?: -1 > 0
    }

    override fun getErrorMessage(): String? {
        return errorMsg
    }


    override fun getHelperMessage(): String? {
        return helperMsg
    }
}

object NotRequired : TextValidationRule() {
    override fun validate(valueToCompare: String) = true
}